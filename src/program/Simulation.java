package program;

import java.util.ArrayList;
import java.util.List;

/**
 * Tøída, ze které se øídí simulace.
 * 
 * @author Jitka Poubová
 */
public class Simulation {

	/** seznam žádostí */
	private final List<Request> requests;
	/** seznam balíkù */
	private final List<Packet> packets;
	/** graf sítì */
	private Graph graph;
	/** zda-li je simulace již hotová */
	private boolean done;
	/** velikost dat, která se musela poslat znovu od zaèátku */
	private double sentAgain;
	/** velikost dat, která se mají poslat sítí */
	private double requestsDataSize;

	/**
	 * Vytvoøí novou simulaci.
	 * 
	 * @param requests
	 *            seznam žádostí
	 * @param graph
	 *            graf
	 */
	public Simulation(List<Request> requests, Graph graph) {
		this.requests = requests;
		this.graph = graph;
		this.packets = new ArrayList<Packet>();
		this.done = false;
	}

	/**
	 * Pøedpøipraví data pro posílání. Všem žádostem nastaví, že ještì nejsou
	 * hotové, že ještì nic nebylo doruèeno a vypoèítá jim cesty. Každé žádosti
	 * vymaže seznam již hotových balíkù. Ze všech žádostí vytvoøí balíky o stejné
	 * velikosti a pøidá je do seznamu balíkù. Nastaví, že simulace není hotová
	 * (done=false), vynuluje atribut sentAgain. Vypoèítá hodnotu atributu
	 * requestsDataSize.
	 */
	public void prepare() {
		requestsDataSize = 0;
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
		for (Request request : requests) {
			request.setDone(false);
			request.deliveredInc(-(request.getDelivered()));
			request.getPacketsDone().clear();
			request.setPath(request.findPath(dijkstra));
			requestsDataSize += request.getSize();

			Packet p = new Packet(request, request.getSize(), request.getStartRouter());
			packets.add(p);
		}

		done = false;
		sentAgain = 0;
	}

	/**
	 * Smyèka celé simulace. Smyèka konèí, když je celá simulace hotová.
	 */
	public void start() {
		prepare();
		for (int i = 0; !done; i++) {
			simulate(i);

		}
	}

	/**
	 * Jeden krok simulace. V jednom kroku balík pøejde pouze po jedné hranì.
	 * 
	 * @param time
	 *            èas
	 */
	public void simulate(int time) {
		edgesFlowToZero();
		
		
		double toSend;
		List<Packet> toAdd = new ArrayList<Packet>();
		List<Packet> toRemove = new ArrayList<Packet>();
		// VYPIS
		// System.out.println("---Simulation time: " + time + " ---");
		for (Packet packet : packets) {
			// uz je hotovo, dame k odstraneni
			if (packet.getDone()) {
				toRemove.add(packet);
				continue;
			}

			// je cas
			if (time >= packet.getRequest().getTime()) {
				List<Vertex> path = packet.getPath();
				Edge edge = graph.getEdge(packet.getCurrentRouter(), path.get(0));
				double maxSize = edge.capacityRemain();

				// hrana je uplne vytizena, musi se pockat
				if (maxSize == 0) {
					continue;

					// balik se muze poslat cely
				} else if (maxSize >= packet.getSize()) {
					toSend = packet.getSize();

					// nejdriv se odebere ze stacku, pokud tam byl
					if (packet.getInStack()) {
						packet.getCurrentRouter().getSmartstack().removePacket(packet);
					}
					packet.send(path.get(0), edge);

					// -- VYPISY
					// System.out.println("Sent: " + packet);
					// System.out.println("Delivered size: " +
					// packet.getRequest().getDelivered());

					// paket uz nemuze projit danou hranou, je moc vytizena / ma
					// maly objem
					// paket se musi rozdelit - nìco se musí dát do stacku,
					// zbytek se musí poslat znovu
				} else {
					toSend = edge.capacityRemain();
					List<Packet> parts = packet.cut(toSend);

					// odeberu balík ze stacku, do stacku pøidám èásti, které mi
					// z tohoto balíku vznikly
					if (packet.getInStack()) {
						packet.getCurrentRouter().getSmartstack().removePacket(packet);
						addToStack(packet.getCurrentRouter(), parts);
					}

					Packet sentPart = parts.get(0);

					// cast se posle
					// nejdriv se odebere ze stacku, pokud tam byla
					if (sentPart.getInStack()) {
						sentPart.getCurrentRouter().getSmartstack().removePacket(sentPart);
					}
					sentPart.send(path.get(0), edge);

					toAdd.addAll(parts);
					toRemove.add(packet);

					// pøidám do stacku daného routeru, co se nemohlo poslat
					parts.remove(sentPart);
					addToStack(packet.getCurrentRouter(), parts);

					// -- VYPISY
					// System.out.println("Sent: " + sentPart);
					// System.out.println("Delivered size: " +
					// sentPart.getRequest().getDelivered());
				}

				
				 // System.out.println(graph.getVertex(11) + " " +
				  //graph.getVertex(11).getSmartstack());
				  //System.out.println(graph.getVertex(16).getSmartstack());
				 

			}
		}

		// System.out.println();

		// hotovo?
		checkDone();

		// vynuluje se flow u kazde hrany
		//edgesFlowToZero();

		// vše k pøidání se pøidá, vše k odebrání se odebere
		packets.addAll(toAdd);
		packets.removeAll(toRemove);
	}

	/**
	 * Is done?
	 * 
	 * @return the done
	 */
	public boolean isDone() {
		return done;
	}

	/**
	 * Vynuluje prùtok (flow) u všech hran v grafu
	 */
	public void edgesFlowToZero() {
		for (Edge e : graph.getEdges()) {
			e.flowInc(-(e.getFlow()));
		}
	}

	/**
	 * Ovìøí, jestli už není celá simulace hotová. Je hotová tehdy, pokud je hotová
	 * každá žádost ze seznamu žádostí.
	 */
	public void checkDone() {
		int count = 0;
		for (Request p : requests) {
			if (p.getDone()) {
				count++;
			}
		}
		if (count == requests.size()) {
			done = true;
		}
	}

	/**
	 * Pøidá seznam balíkù do smartstacku daného routeru. Pokud se balík do stacku
	 * nevejde, vrátím ho na poèáteèní router (pošle se znovu). Pokud balík již je
	 * ve stacku, znovu není pøidán.
	 * 
	 * @param v
	 *            router, do jehož smartstacku se mají balíky pøidat
	 * @param parts
	 *            balíky, které se mají vložit do smartstacku
	 */
	public void addToStack(Vertex v, List<Packet> parts) {
		for (Packet part : parts) {
			if (!part.getDone() && !part.getInStack()) {
				// vrati paket, pokud se nevejde, jinak vrati null
				Packet p = v.getSmartstack().addPacket(part);

				// znovu pošlu, nevejde se totiž do smartstacku (vratim na
				// zacatek)
				if (p != null) {
					p.putPacketToStart();
					sentAgain += p.getSize();
				}
			}
		}
	}

	// ---VYPISUJÍCÍ METODY---
	/**
	 * Vytvoøí øetìzec, reprezentující základní údaje o simulaci. Je uvedena
	 * velikost dat, která se mìla poslat sítí, dále také celková velikost dat,
	 * která se poslala sítí (souèet dat, která se mìla poslat + data, která se
	 * poslala znovu) a je také uvedena velikost dat, která se musela poslat znovu.
	 * Je také uvedena ztrátovost (podíl dat, co se musela poslat znovu / data,
	 * která se mìla poslat).
	 * 
	 * @return øetìzec s údaji o simulaci
	 */
	public String printSimulationInfo() {
		String s = "";
		s += "Total size of data to send = " + requestsDataSize + "\nTotal size of sent data = "
				+ (requestsDataSize + sentAgain) + "\nTotal size of data which were sent again = " + sentAgain
				+ "\nFail = " + (sentAgain / requestsDataSize) + "\n\n";
		return s;
	}

	/**
	 * Vytvoøí øetezec, reprezentující všechny žádosti s jejich cestami a poètem
	 * balíkù, do kterých se daná žádost rozdìlila.
	 * 
	 * @return øetìzec reprezentující žádosti
	 */
	public String printRequests() {
		String s = "";
		s += "--------REQUESTS--------\n";
		for (Request request : requests) {
			s += "--" + request + "--\n";
			s += "Path=" + request.getPath() + "\n";
			s += "Number of packets=" + request.getPacketsDone().size() + "\n\n";
		}
		return s;
	}

	/**
	 * Vypíše na konzoli seznam balíkù.
	 */
	public void printPackets() {
		System.out.println("--Packets--");
		for (Packet packet : packets) {
			System.out.println(packet);
		}
		System.out.println();
	}

	// ----GETTERS A SETTERS----
	/**
	 * Vrátí seznam balíkù.
	 * 
	 * @return seznam balíkù
	 */
	public List<Request> getPackets() {
		return requests;
	}

	/**
	 * Nastaví graf.
	 * 
	 * @param g
	 *            nový graf, který se má nastavit
	 */
	public void setGraph(Graph g) {
		this.graph = g;
	}

}
