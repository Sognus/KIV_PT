package program;

import java.util.ArrayList;
import java.util.List;

/**
 * T��da, kter� reprezentuje uzly (routery) v grafu.
 * 
 * @author Jitka Poubov�
 * @author Jakub V�tek
 */
public class Vertex {
	
	/** id uzlu */
	private final int id;
	/** z�sobn�k */
	private final SmartStack smartstack;
	/** hrana do uzlu, kv�li DijkstraAlgorithm */
	public Edge edgeTo;
	/** seznam bal�k� dat, kter� pro�ly p�es dan� router, 
	 * jsou tam jen ty, kter� p�es n�j pro�ly nebo v n�m kon��, 
	 * nejsou tam ty, kter� v n�m za��naj� */
	private final List<Packet> packets;
	/** celkov� sou�et v�ech velikost� bal�k� dat, kter� pro�ly
	 * p�es dan� router */
	private double size = 0;
	
	/**
	 * Vytvo�� uzel s dan�m id.
	 * 
	 * @param id	id	uzlu
	 */
	public Vertex(int id) {
		this.id = id;
		this.smartstack = new SmartStack(this);
		this.packets = new ArrayList<Packet>();
	}	
	
	/**
	 * P�id� bal�k do seznamu bal�k� pro dan� router, 
	 * kter� p�es router pro�ly.
	 * 
	 * @param p	bal�k, kter� pro�el p�es router
	 */
	public void addPacket(Packet p) {
		packets.add(p);
		size += p.getSize();
	}
	
	/**
	 * Vr�t� obs�hlej��  textovou reprezentaci routeru.
	 * 
	 * @return	textov� reprezentace routeru
	 */
	public String printVertex() {
		return "[" + this.toString() + ", number of packets = " + packets.size() + ", size = " + size + "]\n";
	}
	
	/**
	 * P�evede uzel do textov� podoby.
	 * @return textov� podoba uzlu
	 */
	@Override
	public String toString() {
		return "VertexId=" + id;
	}

	/**
	 * Vr�t� id uzlu.
	 * 
	 * @return	id uzlu
	 */
	public int getId() {
		return id;
	}

	/**
	 * Vr�t� smartstack uzlu.
	 * 
	 * @return	smartstack uzlu	
	 */
	public SmartStack getSmartstack() {
		return smartstack;
	}
}
