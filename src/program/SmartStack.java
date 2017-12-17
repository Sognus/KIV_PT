package program;

import java.util.Stack;

/**
 * T��da {@code Smartstack} reprezentuje chytr� z�sobn�k, kter� m� ka�d� uzel v
 * grafu.
 * 
 * @author Jitka Poubov�
 */
public class SmartStack {

	/** kapacita smartstacku, v�echny maj� kapacitu 100 */
	public static final int capacity = 100;
	/** z�sobn�k pro bal�ky dat */
	private final Stack<Packet> stack;
	/** voln� m�sto */
	private double freeSpace;
	/** uzel, ke kter�mu smartstack pat�� */
	private final Vertex router;

	/**
	 * Vytvo�� nov� SmartStack.
	 * 
	 * @param v
	 *            uzel, ke kter�mu smartstack pat��
	 */
	public SmartStack(Vertex v) {
		this.stack = new Stack<Packet>();
		this.freeSpace = capacity;
		this.router = v;
	}

	/**
	 * P�id� packet do z�sobn�ku. Z�rove� t�m sn�� voln� m�sto o velikost
	 * paketu. Pokud se packet nevejde do z�sobn�ku, vr�t� se v n�vratov�
	 * hodnot�. Pokud se vejde, vr�t� se null.
	 * 
	 * @param p
	 *            packet k p�id�n�
	 * @return null, pokud se packet vejde do z�sobn�k�; jinak packet
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
	 * Odebere packet ze z�sobn�ku.
	 * 
	 * @param p
	 *            packet k odebr�n�
	 */
	public void removePacket(Packet p) {
		this.stack.remove(p);
		this.freeSpace += p.getSize();
		p.setInStack(false);
	}

	/**
	 * P�evede smartstack na jeho �et�zcovou reprezentaci.
	 * 
	 * @return �et�zcov� reprezentace smartstacku
	 */
	@Override
	public String toString() {
		return "SmartStack [freeSpace=" + freeSpace + "]";
	}

	// ----GETTERS A SETTERS----
	/**
	 * Vr�t� z�sobn�k.
	 * 
	 * @return z�sobn�k
	 */
	public Stack<Packet> getStack() {
		return stack;
	}

	/**
	 * Vr�t� velikost voln�ho m�sta v z�sobn�ku.
	 * 
	 * @return velikost voln�ho m�sta
	 */
	public double getFreeSpace() {
		return freeSpace;
	}

	/**
	 * Vr�t� uzel (router), ke kter�mu smartstack pat��.
	 * 
	 * @return router
	 */
	public Vertex getRouter() {
		return router;
	}
}
