package program;

import java.util.ArrayList;
import java.util.List;

/**
 * T��da reprezentuj�c� jednotliv� bal�ky dat, kter� se pos�laj� skrz s�.
 * 
 * @author Jitka Poubov�
 */
public class Packet {

	/** ��dost, ke kter� bal�k pat�� */
	private Request request;
	/** velikost dat */
	private double size;
	/** sou�asn� router, kde se bal�k nach�z� */
	private Vertex currentRouter;
	/**
	 * cesta, po kter� se bal�k po�le (zb�vaj�c� cesta, prvn�m routerem je
	 * router n�sleduj�c�)
	 */
	private List<Vertex> path;
	/** zda-li je bal�k ji� doru�en (hotov) */
	private boolean done;
	/** zda-li se bal�k nach�z� v n�jak�m smartstacku (u n�jak�ho routeru) */
	private boolean inStack;

	/**
	 * Vytvo�� nov� bal�k.
	 * 
	 * @param r
	 *            ��dost, ke kter� bal�k pat�� (kter� je sou��st�)
	 * @param size
	 *            velikost dat
	 * @param curr
	 *            sou�asn� router, kde se bal�k nach�z�
	 */
	public Packet(Request r, double size, Vertex curr) {
		this.request = r;
		this.size = size;
		this.currentRouter = curr;
		this.done = false;
		this.inStack = false;
		this.path = createPath();
	}

	/**
	 * Vytvo�� nov� bal�k.
	 * 
	 * @param p
	 *            bal�k, ze kter�ho se tvo�� nov� bal�k
	 * @param size
	 *            velikost dat
	 * @param curr
	 *            sou�asn� router
	 */
	public Packet(Packet p, double size, Vertex curr) {
		this(p.getRequest(), size, curr);
	}

	/**
	 * Vytvo�� cestu pro bal�k. Pou�ije se cesta, kter� u� je vygenerovan� pro
	 * danou ��dost, ke kter� bal�k pat��. Z t�to cesty se jen odebere prvn�
	 * router (na tom se bal�k nach�z�, tud� cesta za��n� a� n�sleduj�c�m
	 * routerem).
	 * 
	 * @return cesta pro bal�k
	 */
	private List<Vertex> createPath() {
		return new ArrayList<Vertex>(
				request.getPath().subList(request.getPath().indexOf(this.currentRouter) + 1, request.getPath().size()));

	}

	/**
	 * Posl�n� bal�ku. Parametrem je router, na kter� se
	 * bal�k po�le, a hrana, p�es kterou se bal�k po�le.
	 * 
	 * @param v	router, kam se m� bal�k poslat
	 * @param e hrana, p�es kterou se bal�k po�le
	 */
	public void send(Vertex v, Edge e) {
		currentRouter = v;
		path.get(0).addPacket(this);
		path.remove(0);		
		e.flowInc(size);
		
		// ov���me, zda-li nen� bal�k u� na konci (hotov)
		if (currentRouter.getId() == request.getEndRouter().getId()) {
			request.deliveredInc(size);
			setDone(true);
			request.addPacketDone(this);
			
			// ov���me, zda-li nen� cel� ��dost ji� hotova
			if (request.getDelivered() == request.getSize()) {
				request.setDone(true); 
			} 
		}
	}

	/**
	 * Rozd�l� bal�k na v�ce men��ch bal�k�.
	 * 
	 * @param size
	 *            velikost, kterou bude m�t prvn� ��st bal�ku, dal�� ��sti budou
	 *            stejn� velk� nebo men��
	 * @return seznam men��ch bal�k�
	 */
	public List<Packet> cut(double size) {
		double cutSize = 0;
		List<Packet> parts = new ArrayList<Packet>();
		while (this.size != cutSize) {
			Packet p;
			if (size <= (this.size - cutSize)) {
				p = new Packet(this, size, this.getCurrentRouter());
				cutSize += size;
			} else {
				p = new Packet(this, (this.size - cutSize), this.getCurrentRouter());
				cutSize += (this.size - cutSize);
			}
			parts.add(p);
		}

		return parts;
	}

	/**
	 * Vr�t� bal�k na za��tek, na router, ze kter�ho se pos�lal (po��te�n�
	 * router ��dosti, ke kter� bal�k pat��). Cesta bal�ku se p�epo��t�.
	 */
	public void putPacketToStart() {
		currentRouter = request.getStartRouter();
		path = new ArrayList<Vertex>(request.getPath());
	}

	/**
	 * P�evede bal�k na textovou podobu.
	 * 
	 * @return textov� podoba bal�ku
	 */
	@Override
	public String toString() {
		return "Packet [size=" + size + ", currentRouterId=" + currentRouter.getId() + "]";
	}

	// ---GETTERS A SETTERS---
	/**
	 * Vr�t� sou�asn� router.
	 * 
	 * @return sou�asn� router
	 */
	public Vertex getCurrentRouter() {
		return currentRouter;
	}

	/**
	 * Vr�t� ��dost, ke kter� bal�k pat��.
	 * 
	 * @return ��dost
	 */
	public Request getRequest() {
		return request;
	}

	/**
	 * Vr�t� velikost bal�ku.
	 * 
	 * @return velikost
	 */
	public double getSize() {
		return size;
	}

	/**
	 * Vr�t� cestu (seznam router�).
	 * 
	 * @return cesta
	 */
	public List<Vertex> getPath() {
		return path;
	}

	/**
	 * Vr�t�, jestli je bal�k ji� hotov (doru�en).
	 * 
	 * @return zda-li je bal�k doru�en
	 */
	public boolean getDone() {
		return done;
	}

	/**
	 * Nastav�, jestli je bal�k doru�en.
	 * 
	 * @param b
	 *            stav doru�en� (true=doru�en)
	 */
	public void setDone(boolean b) {
		this.done = b;
	}

	/**
	 * Vr�t�, zda-li je bal�k v n�jak�m smartstacku.
	 * 
	 * @return jestli je bal�k v n�jak�m smartstacku
	 */
	public boolean getInStack() {
		return inStack;
	}

	/**
	 * Nastav�, jestli je bal�k v n�jak�m smartstacku.
	 * 
	 * @param b
	 *            parametr, true=je ve smartstacku
	 */
	public void setInStack(boolean b) {
		this.inStack = b;
	}

}
