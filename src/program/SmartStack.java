package program;

import java.util.Stack;

/**
 * Tøída {@code Smartstack} reprezentuje chytrý zásobník, který má každý uzel v
 * grafu.
 * 
 * @author Jitka Poubová
 */
public class SmartStack {

	/** kapacita smartstacku, všechny mají kapacitu 100 */
	public static final int capacity = 100;
	/** zásobník pro balíky dat */
	private final Stack<Packet> stack;
	/** volné místo */
	private double freeSpace;
	/** uzel, ke kterému smartstack patøí */
	private final Vertex router;

	/**
	 * Vytvoøí nový SmartStack.
	 * 
	 * @param v
	 *            uzel, ke kterému smartstack patøí
	 */
	public SmartStack(Vertex v) {
		this.stack = new Stack<Packet>();
		this.freeSpace = capacity;
		this.router = v;
	}

	/**
	 * Pøidá packet do zásobníku. Zároveò tím sníží volné místo o velikost
	 * paketu. Pokud se packet nevejde do zásobníku, vrátí se v návratové
	 * hodnotì. Pokud se vejde, vrátí se null.
	 * 
	 * @param p
	 *            packet k pøidání
	 * @return null, pokud se packet vejde do zásobníkù; jinak packet
	 */
	public Packet addPacket(Packet p) {
		// pokud se nevejde
		if (freeSpace < p.getSize()) {
			return p;
		} else {
			this.stack.add(p);
			this.freeSpace -= p.getSize();
			p.setInStack(true);
			return null;
		}
	}

	/**
	 * Odebere packet ze zásobníku.
	 * 
	 * @param p
	 *            packet k odebrání
	 */
	public void removePacket(Packet p) {
		this.stack.remove(p);
		this.freeSpace += p.getSize();
		p.setInStack(false);
	}

	/**
	 * Pøevede smartstack na jeho øetìzcovou reprezentaci.
	 * 
	 * @return øetìzcová reprezentace smartstacku
	 */
	@Override
	public String toString() {
		return "SmartStack [freeSpace=" + freeSpace + "]";
	}

	// ----GETTERS A SETTERS----
	/**
	 * Vrátí zásobník.
	 * 
	 * @return zásobník
	 */
	public Stack<Packet> getStack() {
		return stack;
	}

	/**
	 * Vrátí velikost volného místa v zásobníku.
	 * 
	 * @return velikost volného místa
	 */
	public double getFreeSpace() {
		return freeSpace;
	}

	/**
	 * Vrátí uzel (router), ke kterému smartstack patøí.
	 * 
	 * @return router
	 */
	public Vertex getRouter() {
		return router;
	}
}
