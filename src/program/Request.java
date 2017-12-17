package program;

import java.util.ArrayList;
import java.util.List;

/**
 * Tøída, reprezentující jednotlivé žádosti o poslání dat.
 * 
 * @author Jitka Poubová
 */
public class Request {

	/** èas, kdy se mají data poslat */
	private final int time;
	/** poèáteèní router, ze kterého se data posílají */
	private final Vertex startRouter;
	/** koncový router, kam se mají data poslat */
	private final Vertex endRouter;
	/** množství dat */
	private final double size;
	/**
	 * cesta, kudy se budou data posílat, prvním routerem je již router následující
	 * (poèáteèní router v seznamu není)
	 */
	private List<Vertex> path;
	/** zda-li je žádost již hotová (celá doruèená) */
	private boolean done = false;
	/** množství dat, co je již doruèena */
	private double delivered = 0;
	/** seznam hotových (doruèených) balíkù dané žádosti */
	private final List<Packet> packetsDone;

	/**
	 * Vytvoøí žádost o poslání dat.
	 * 
	 * @param time
	 *            èas
	 * @param startRouter
	 *            poèáteèní router
	 * @param endRouter
	 *            koncový router
	 * @param size
	 *            velikost (objem) dat
	 */
	public Request(int time, Vertex startRouter, Vertex endRouter, double size) {
		this.time = time;
		this.startRouter = startRouter;
		this.endRouter = endRouter;
		this.size = size;
		packetsDone = new ArrayList<Packet>();
	}

	/**
	 * S využitím tøídy DijkstraAlgorithm nalezne nejlepší cestu pro danou žádost.
	 * 
	 * @param dijkstra
	 *            instance tøídy DijkstraAlgorithm, která je nad daným grafem
	 * @return cesta (seznam vrcholù, bez poèáteèního ale vèetnì koncového)
	 */
	public List<Vertex> findPath(DijkstraAlgorithm dijkstra) {
		dijkstra.execute(startRouter);
		List<Vertex> foundPath = dijkstra.getPath(endRouter);
		foundPath.remove(0); // odebereme startRouter, ten prvni
		return foundPath;
	}

	/**
	 * Nastaví velikost doruèených dat o daný parametr (pøipoèítá).
	 * 
	 * @param d
	 *            velikost dat, které se mají pøipoèítat k již doruèeným datùm
	 */
	public void deliveredInc(double d) {
		this.delivered += d;
	}

	/**
	 * Pøidá balík do seznamu již hotových (doruèených) balíkù dané žádosti.
	 * 
	 * @param p
	 *            balík k pøidání do seznamu
	 */
	public void addPacketDone(Packet p) {
		packetsDone.add(p);
	}

	/**
	 * Pøevede žádost do textové podoby.
	 * 
	 * @return textová podoba žádosti
	 */
	@Override
	public String toString() {
		return "Request [time=" + time + ", startRouterId=" + startRouter.getId() + ", endRouterId=" + endRouter.getId()
				+ ", size=" + size + "]";
	}

	public String toStringShort() {
		return String.format("Request(T=%d): %d to %d (%.2f)", this.time, this.startRouter.getId(),
				this.endRouter.getId(), this.getSize());
	}

	// ----GETTERS A SETTERS----
	/**
	 * Vrátí èas, kdy se má žádost vykonat.
	 * 
	 * @return èas
	 */
	public int getTime() {
		return time;
	}

	/**
	 * Vrátí poèáteèní router, ze kterého se mají data poslat.
	 * 
	 * @return poèáteèní router
	 */
	public Vertex getStartRouter() {
		return startRouter;
	}

	/**
	 * Vrátí koncový router, kam se mají data poslat.
	 * 
	 * @return koncový router
	 */
	public Vertex getEndRouter() {
		return endRouter;
	}

	/**
	 * Vrátí velikost dat.
	 * 
	 * @return velikost dat
	 */
	public double getSize() {
		return size;
	}

	/**
	 * Vrátí cestu.
	 * 
	 * @return cesta
	 */
	public List<Vertex> getPath() {
		return path;
	}

	/**
	 * Nastaví cestu dané žádosti.
	 * 
	 * @param path
	 *            cesta (seznam vrcholù)
	 */
	public void setPath(List<Vertex> path) {
		this.path = path;
	}

	/**
	 * Vrátí, jestli je žádost již hotova (vše doruèeno).
	 * 
	 * @return je-li žádost hotova
	 */
	public boolean getDone() {
		return done;
	}

	/**
	 * Nastaví, zda-li je žádost hotova.
	 * 
	 * @param b
	 *            jak ji chceme nastavit (true = je hotova)
	 */
	public void setDone(boolean b) {
		this.done = b;
	}

	/**
	 * Vrátí velikost doruèených dat.
	 * 
	 * @return velikost doruèených dat
	 */
	public double getDelivered() {
		return delivered;
	}

	/**
	 * Vrátí seznam doruèených balíkù dané žádosti.
	 * 
	 * @return seznam doruèených balíkù
	 */
	public List<Packet> getPacketsDone() {
		return packetsDone;
	}

}
