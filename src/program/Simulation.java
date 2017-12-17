package program;

import java.util.ArrayList;
import java.util.List;

/**
 * T��da, ze kter� se ��d� simulace.
 * 
 * @author Jitka Poubov�
 */
public class Simulation {

	/** seznam ��dost� */
	private final List<Request> requests;
	/** seznam bal�k� */
	private final List<Packet> packets;
	/** graf s�t� */
	private Graph graph;
	/** zda-li je simulace ji� hotov� */
	private boolean done;
	/** velikost dat, kter� se musela poslat znovu od za��tku */
	private double sentAgain;
	/** velikost dat, kter� se maj� poslat s�t� */
	private double requestsDataSize;

	/**
	 * Vytvo�� novou simulaci.
	 * 
	 * @param requests
	 *            seznam ��dost�
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
	 * P�edp�iprav� data pro pos�l�n�. V�em ��dostem nastav�, �e je�t� nejsou
	 * hotov�, �e je�t� nic nebylo doru�eno a vypo��t� jim cesty. Ka�d� ��dosti
	 * vyma�e seznam ji� hotov�ch bal�k�. Ze v�ech ��dost� vytvo�� bal�ky o stejn�
	 * velikosti a p�id� je do seznamu bal�k�. Nastav�, �e simulace nen� hotov�
	 * (done=false), vynuluje atribut sentAgain. Vypo��t� hodnotu atributu
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
	 * Smy�ka cel� simulace. Smy�ka kon��, kdy� je cel� simulace hotov�.
	 */
	public void start() {
		prepare();
		for (int i = 0; !done; i++) {
			simulate(i);

		}
	}

	/**
	 * Jeden krok simulace. V jednom kroku bal�k p�ejde pouze po jedn� hran�.
	 * 
	 * @param time
	 *            �as
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
					// paket se musi rozdelit - n�co se mus� d�t do stacku,
					// zbytek se mus� poslat znovu
				} else {
					toSend = edge.capacityRemain();
					List<Packet> parts = packet.cut(toSend);

					// odeberu bal�k ze stacku, do stacku p�id�m ��sti, kter� mi
					// z tohoto bal�ku vznikly
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

					// p�id�m do stacku dan�ho routeru, co se nemohlo poslat
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

		// v�e k p�id�n� se p�id�, v�e k odebr�n� se odebere
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
	 * Vynuluje pr�tok (flow) u v�ech hran v grafu
	 */
	public void edgesFlowToZero() {
		for (Edge e : graph.getEdges()) {
			e.flowInc(-(e.getFlow()));
		}
	}

	/**
	 * Ov���, jestli u� nen� cel� simulace hotov�. Je hotov� tehdy, pokud je hotov�
	 * ka�d� ��dost ze seznamu ��dost�.
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
	 * P�id� seznam bal�k� do smartstacku dan�ho routeru. Pokud se bal�k do stacku
	 * nevejde, vr�t�m ho na po��te�n� router (po�le se znovu). Pokud bal�k ji� je
	 * ve stacku, znovu nen� p�id�n.
	 * 
	 * @param v
	 *            router, do jeho� smartstacku se maj� bal�ky p�idat
	 * @param parts
	 *            bal�ky, kter� se maj� vlo�it do smartstacku
	 */
	public void addToStack(Vertex v, List<Packet> parts) {
		for (Packet part : parts) {
			if (!part.getDone() && !part.getInStack()) {
				// vrati paket, pokud se nevejde, jinak vrati null
				Packet p = v.getSmartstack().addPacket(part);

				// znovu po�lu, nevejde se toti� do smartstacku (vratim na
				// zacatek)
				if (p != null) {
					p.putPacketToStart();
					sentAgain += p.getSize();
				}
			}
		}
	}

	// ---VYPISUJ�C� METODY---
	/**
	 * Vytvo�� �et�zec, reprezentuj�c� z�kladn� �daje o simulaci. Je uvedena
	 * velikost dat, kter� se m�la poslat s�t�, d�le tak� celkov� velikost dat,
	 * kter� se poslala s�t� (sou�et dat, kter� se m�la poslat + data, kter� se
	 * poslala znovu) a je tak� uvedena velikost dat, kter� se musela poslat znovu.
	 * Je tak� uvedena ztr�tovost (pod�l dat, co se musela poslat znovu / data,
	 * kter� se m�la poslat).
	 * 
	 * @return �et�zec s �daji o simulaci
	 */
	public String printSimulationInfo() {
		String s = "";
		s += "Total size of data to send = " + requestsDataSize + "\nTotal size of sent data = "
				+ (requestsDataSize + sentAgain) + "\nTotal size of data which were sent again = " + sentAgain
				+ "\nFail = " + (sentAgain / requestsDataSize) + "\n\n";
		return s;
	}

	/**
	 * Vytvo�� �etezec, reprezentuj�c� v�echny ��dosti s jejich cestami a po�tem
	 * bal�k�, do kter�ch se dan� ��dost rozd�lila.
	 * 
	 * @return �et�zec reprezentuj�c� ��dosti
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
	 * Vyp�e na konzoli seznam bal�k�.
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
	 * Vr�t� seznam bal�k�.
	 * 
	 * @return seznam bal�k�
	 */
	public List<Request> getPackets() {
		return requests;
	}

	/**
	 * Nastav� graf.
	 * 
	 * @param g
	 *            nov� graf, kter� se m� nastavit
	 */
	public void setGraph(Graph g) {
		this.graph = g;
	}

}
