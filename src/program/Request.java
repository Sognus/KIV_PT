package program;

import java.util.ArrayList;
import java.util.List;

/**
 * T��da, reprezentuj�c� jednotliv� ��dosti o posl�n� dat.
 * 
 * @author Jitka Poubov�
 */
public class Request {

	/** �as, kdy se maj� data poslat */
	private final int time;
	/** po��te�n� router, ze kter�ho se data pos�laj� */
	private final Vertex startRouter;
	/** koncov� router, kam se maj� data poslat */
	private final Vertex endRouter;
	/** mno�stv� dat */
	private final double size;
	/**
	 * cesta, kudy se budou data pos�lat, prvn�m routerem je ji� router n�sleduj�c�
	 * (po��te�n� router v seznamu nen�)
	 */
	private List<Vertex> path;
	/** zda-li je ��dost ji� hotov� (cel� doru�en�) */
	private boolean done = false;
	/** mno�stv� dat, co je ji� doru�ena */
	private double delivered = 0;
	/** seznam hotov�ch (doru�en�ch) bal�k� dan� ��dosti */
	private final List<Packet> packetsDone;

	/**
	 * Vytvo�� ��dost o posl�n� dat.
	 * 
	 * @param time
	 *            �as
	 * @param startRouter
	 *            po��te�n� router
	 * @param endRouter
	 *            koncov� router
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
	 * S vyu�it�m t��dy DijkstraAlgorithm nalezne nejlep�� cestu pro danou ��dost.
	 * 
	 * @param dijkstra
	 *            instance t��dy DijkstraAlgorithm, kter� je nad dan�m grafem
	 * @return cesta (seznam vrchol�, bez po��te�n�ho ale v�etn� koncov�ho)
	 */
	public List<Vertex> findPath(DijkstraAlgorithm dijkstra) {
		dijkstra.execute(startRouter);
		List<Vertex> foundPath = dijkstra.getPath(endRouter);
		foundPath.remove(0); // odebereme startRouter, ten prvni
		return foundPath;
	}

	/**
	 * Nastav� velikost doru�en�ch dat o dan� parametr (p�ipo��t�).
	 * 
	 * @param d
	 *            velikost dat, kter� se maj� p�ipo��tat k ji� doru�en�m dat�m
	 */
	public void deliveredInc(double d) {
		this.delivered += d;
	}

	/**
	 * P�id� bal�k do seznamu ji� hotov�ch (doru�en�ch) bal�k� dan� ��dosti.
	 * 
	 * @param p
	 *            bal�k k p�id�n� do seznamu
	 */
	public void addPacketDone(Packet p) {
		packetsDone.add(p);
	}

	/**
	 * P�evede ��dost do textov� podoby.
	 * 
	 * @return textov� podoba ��dosti
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
	 * Vr�t� �as, kdy se m� ��dost vykonat.
	 * 
	 * @return �as
	 */
	public int getTime() {
		return time;
	}

	/**
	 * Vr�t� po��te�n� router, ze kter�ho se maj� data poslat.
	 * 
	 * @return po��te�n� router
	 */
	public Vertex getStartRouter() {
		return startRouter;
	}

	/**
	 * Vr�t� koncov� router, kam se maj� data poslat.
	 * 
	 * @return koncov� router
	 */
	public Vertex getEndRouter() {
		return endRouter;
	}

	/**
	 * Vr�t� velikost dat.
	 * 
	 * @return velikost dat
	 */
	public double getSize() {
		return size;
	}

	/**
	 * Vr�t� cestu.
	 * 
	 * @return cesta
	 */
	public List<Vertex> getPath() {
		return path;
	}

	/**
	 * Nastav� cestu dan� ��dosti.
	 * 
	 * @param path
	 *            cesta (seznam vrchol�)
	 */
	public void setPath(List<Vertex> path) {
		this.path = path;
	}

	/**
	 * Vr�t�, jestli je ��dost ji� hotova (v�e doru�eno).
	 * 
	 * @return je-li ��dost hotova
	 */
	public boolean getDone() {
		return done;
	}

	/**
	 * Nastav�, zda-li je ��dost hotova.
	 * 
	 * @param b
	 *            jak ji chceme nastavit (true = je hotova)
	 */
	public void setDone(boolean b) {
		this.done = b;
	}

	/**
	 * Vr�t� velikost doru�en�ch dat.
	 * 
	 * @return velikost doru�en�ch dat
	 */
	public double getDelivered() {
		return delivered;
	}

	/**
	 * Vr�t� seznam doru�en�ch bal�k� dan� ��dosti.
	 * 
	 * @return seznam doru�en�ch bal�k�
	 */
	public List<Packet> getPacketsDone() {
		return packetsDone;
	}

}
