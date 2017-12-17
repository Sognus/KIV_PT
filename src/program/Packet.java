package program;

import java.util.ArrayList;
import java.util.List;

/**
 * Tøída reprezentující jednotlivé balíky dat, které se posílají skrz sí.
 * 
 * @author Jitka Poubová
 */
public class Packet {

	/** ádost, ke které balík patøí */
	private Request request;
	/** velikost dat */
	private double size;
	/** souèasnı router, kde se balík nachází */
	private Vertex currentRouter;
	/**
	 * cesta, po které se balík pošle (zbıvající cesta, prvním routerem je
	 * router následující)
	 */
	private List<Vertex> path;
	/** zda-li je balík ji doruèen (hotov) */
	private boolean done;
	/** zda-li se balík nachází v nìjakém smartstacku (u nìjakého routeru) */
	private boolean inStack;

	/**
	 * Vytvoøí novı balík.
	 * 
	 * @param r
	 *            ádost, ke které balík patøí (které je souèástí)
	 * @param size
	 *            velikost dat
	 * @param curr
	 *            souèasnı router, kde se balík nachází
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
	 * Vytvoøí novı balík.
	 * 
	 * @param p
	 *            balík, ze kterého se tvoøí novı balík
	 * @param size
	 *            velikost dat
	 * @param curr
	 *            souèasnı router
	 */
	public Packet(Packet p, double size, Vertex curr) {
		this(p.getRequest(), size, curr);
	}

	/**
	 * Vytvoøí cestu pro balík. Pouije se cesta, která u je vygenerovaná pro
	 * danou ádost, ke které balík patøí. Z této cesty se jen odebere první
	 * router (na tom se balík nachází, tudí cesta zaèíná a následujícím
	 * routerem).
	 * 
	 * @return cesta pro balík
	 */
	private List<Vertex> createPath() {
		return new ArrayList<Vertex>(
				request.getPath().subList(request.getPath().indexOf(this.currentRouter) + 1, request.getPath().size()));

	}

	/**
	 * Poslání balíku. Parametrem je router, na kterı se
	 * balík pošle, a hrana, pøes kterou se balík pošle.
	 * 
	 * @param v	router, kam se má balík poslat
	 * @param e hrana, pøes kterou se balík pošle
	 */
	public void send(Vertex v, Edge e) {
		currentRouter = v;
		path.get(0).addPacket(this);
		path.remove(0);		
		e.flowInc(size);
		
		// ovìøíme, zda-li není balík u na konci (hotov)
		if (currentRouter.getId() == request.getEndRouter().getId()) {
			request.deliveredInc(size);
			setDone(true);
			request.addPacketDone(this);
			
			// ovìøíme, zda-li není celá ádost ji hotova
			if (request.getDelivered() == request.getSize()) {
				request.setDone(true); 
			} 
		}
	}

	/**
	 * Rozdìlí balík na více menších balíkù.
	 * 
	 * @param size
	 *            velikost, kterou bude mít první èást balíku, další èásti budou
	 *            stejnì velké nebo menší
	 * @return seznam menších balíkù
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
	 * Vrátí balík na zaèátek, na router, ze kterého se posílal (poèáteèní
	 * router ádosti, ke které balík patøí). Cesta balíku se pøepoèítá.
	 */
	public void putPacketToStart() {
		currentRouter = request.getStartRouter();
		path = new ArrayList<Vertex>(request.getPath());
	}

	/**
	 * Pøevede balík na textovou podobu.
	 * 
	 * @return textová podoba balíku
	 */
	@Override
	public String toString() {
		return "Packet [size=" + size + ", currentRouterId=" + currentRouter.getId() + "]";
	}

	// ---GETTERS A SETTERS---
	/**
	 * Vrátí souèasnı router.
	 * 
	 * @return souèasnı router
	 */
	public Vertex getCurrentRouter() {
		return currentRouter;
	}

	/**
	 * Vrátí ádost, ke které balík patøí.
	 * 
	 * @return ádost
	 */
	public Request getRequest() {
		return request;
	}

	/**
	 * Vrátí velikost balíku.
	 * 
	 * @return velikost
	 */
	public double getSize() {
		return size;
	}

	/**
	 * Vrátí cestu (seznam routerù).
	 * 
	 * @return cesta
	 */
	public List<Vertex> getPath() {
		return path;
	}

	/**
	 * Vrátí, jestli je balík ji hotov (doruèen).
	 * 
	 * @return zda-li je balík doruèen
	 */
	public boolean getDone() {
		return done;
	}

	/**
	 * Nastaví, jestli je balík doruèen.
	 * 
	 * @param b
	 *            stav doruèení (true=doruèen)
	 */
	public void setDone(boolean b) {
		this.done = b;
	}

	/**
	 * Vrátí, zda-li je balík v nìjakém smartstacku.
	 * 
	 * @return jestli je balík v nìjakém smartstacku
	 */
	public boolean getInStack() {
		return inStack;
	}

	/**
	 * Nastaví, jestli je balík v nìjakém smartstacku.
	 * 
	 * @param b
	 *            parametr, true=je ve smartstacku
	 */
	public void setInStack(boolean b) {
		this.inStack = b;
	}

}
