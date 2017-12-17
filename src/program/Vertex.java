package program;

import java.util.ArrayList;
import java.util.List;

/**
 * Tøída, která reprezentuje uzly (routery) v grafu.
 * 
 * @author Jitka Poubová
 * @author Jakub Vítek
 */
public class Vertex {
	
	/** id uzlu */
	private final int id;
	/** zásobník */
	private final SmartStack smartstack;
	/** hrana do uzlu, kvùli DijkstraAlgorithm */
	public Edge edgeTo;
	/** seznam balíkù dat, které prošly pøes daný router, 
	 * jsou tam jen ty, které pøes nìj prošly nebo v nìm konèí, 
	 * nejsou tam ty, které v nìm zaèínají */
	private final List<Packet> packets;
	/** celkový souèet všech velikostí balíkù dat, které prošly
	 * pøes daný router */
	private double size = 0;
	
	/**
	 * Vytvoøí uzel s daným id.
	 * 
	 * @param id	id	uzlu
	 */
	public Vertex(int id) {
		this.id = id;
		this.smartstack = new SmartStack(this);
		this.packets = new ArrayList<Packet>();
	}	
	
	/**
	 * Pøidá balík do seznamu balíkù pro daný router, 
	 * které pøes router prošly.
	 * 
	 * @param p	balík, který prošel pøes router
	 */
	public void addPacket(Packet p) {
		packets.add(p);
		size += p.getSize();
	}
	
	/**
	 * Vrátí obsáhlejší  textovou reprezentaci routeru.
	 * 
	 * @return	textová reprezentace routeru
	 */
	public String printVertex() {
		return "[" + this.toString() + ", number of packets = " + packets.size() + ", size = " + size + "]\n";
	}
	
	/**
	 * Pøevede uzel do textové podoby.
	 * @return textová podoba uzlu
	 */
	@Override
	public String toString() {
		return "VertexId=" + id;
	}

	/**
	 * Vrátí id uzlu.
	 * 
	 * @return	id uzlu
	 */
	public int getId() {
		return id;
	}

	/**
	 * Vrátí smartstack uzlu.
	 * 
	 * @return	smartstack uzlu	
	 */
	public SmartStack getSmartstack() {
		return smartstack;
	}
}
