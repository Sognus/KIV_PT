package program;

/**
 * Tøída, která reprezentuje hranu (spojení) v grafu.
 * 
 * @author Jitka Poubová
 * @author Jakub Vítek
 */
public class Edge {

	/** maximální rychlost pøenosu v grafu, dle zadání = 10000 */
	private static final double maxSpeedInGraph = 10000;
	/** poèáteèní vrchol */
	private final Vertex startVertex;
	/** koncový vrchol */
	private final Vertex endVertex;
	/** maximální rychlost pøenosu (Mb/s) */
	private final int maximumSpeed;
	/**
	 * stabilita daného spoje v rozsahu 0-1, napø. stabilita 0.9 znamená, že když je
	 * spojení vytížené na více než 90 %, tak je 50% šance, že se 50 % dat ztratí
	 */
	private final double stability;
	/**
	 * spolehlivá rychlost, je to maximální rychlost pøenosu, pøi které se zachová
	 * 100% spolehlivost
	 */
	private final double reliableSpeed;
	/** aktuální prùtok */
	private double flow;

	/**
	 * Konstuktor, vytvoøí instanci tøídy.
	 * 
	 * @param startVertex
	 *            poèáteèní vrchol
	 * @param endVertex
	 *            koncový vrchol
	 * @param maximumSpeed
	 *            maximální rychlost
	 * @param stability
	 *            stabilita daného spoje
	 */
	public Edge(Vertex startVertex, Vertex endVertex, int maximumSpeed, double stability) {
		this.startVertex = startVertex;
		this.endVertex = endVertex;
		this.maximumSpeed = maximumSpeed;
		this.stability = stability;
		this.reliableSpeed = (this.stability * this.maximumSpeed);
		this.flow = 0;
	}

	/**
	 * Zvýší aktuální prùtok o parametr flow.
	 * 
	 * @param flow
	 *            hodnota, o kterou se prùtok zvýší
	 */
	public void flowInc(double flow) {
		this.flow += flow;
	}

	/**
	 * Vrátí zbývající kapacitu pro prùtok. Maximální kapacitou je hodnota atributu
	 * reliableSpeed.
	 * 
	 * @return zbývající kapacita
	 */
	public int capacityRemain() {
		return (int) (reliableSpeed - flow);
	}

	// ----METODY KVULI DIJKSTROVI----
	/**
	 * Vrátí váhu hrany. Poèítá se jako (maximální rychlost, která se mùže v grafu
	 * vyskytovat - spolehlivá rychlost). Je to kvùli tomu, ze Dijkstra vyhledává
	 * nejkratší cestu, my jsme ale potøebovali tu nejvíce ohodnocenou, tudíž jsme
	 * museli z cesty s nejlepším (nejvìtším) ohodnocením udìlat cestu s tím
	 * nejmenším ohodnocením.
	 * 
	 * @return váha hrany
	 */
	public int getWeight() {
		return (int) (maxSpeedInGraph - reliableSpeed);
	}

	// ----- GETTERS, SETTERS, TO STRING -----
	/**
	 * Pøevede hranu do textové podoby.
	 * 
	 * @return textová podoba hrany
	 */
	@Override
	public String toString() {
		return "[ID1=" + startVertex + ", ID2=" + endVertex + ", maximumSpeed=" + maximumSpeed + ", stability="
				+ stability + ", flow=" + flow + "]";
	}

	/**
	 * Vrátí poèáteèní vrchol hrany.
	 * 
	 * @return poèáteèní vrchol
	 */
	public Vertex getStartVertex() {
		return startVertex;
	}

	/**
	 * Vrátí koncový vrchol hrany.
	 * 
	 * @return koncový vrchol
	 */
	public Vertex getEndVertex() {
		return endVertex;
	}

	/**
	 * Vrátí maximální rychlost.
	 * 
	 * @return maximální rychlost
	 */
	public int getMaximumSpeed() {
		return maximumSpeed;
	}

	/**
	 * Vrátí stabilitu (spolehlivost) hrany.
	 * 
	 * @return spolehlivost
	 */
	public double getStability() {
		return stability;
	}

	/**
	 * Vrátí maximální spolehlivou rychlost.
	 * 
	 * @return maximální spolehlivá rychlost
	 */
	public double getReliableSpeed() {
		return this.reliableSpeed;
	}

	/**
	 * Vrátí aktuální prùtok hrany.
	 * 
	 * @return aktuální prùtok
	 */
	public double getFlow() {
		return flow;
	}

}
