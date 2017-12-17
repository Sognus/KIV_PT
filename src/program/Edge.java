package program;

/**
 * T��da, kter� reprezentuje hranu (spojen�) v grafu.
 * 
 * @author Jitka Poubov�
 * @author Jakub V�tek
 */
public class Edge {

	/** maxim�ln� rychlost p�enosu v grafu, dle zad�n� = 10000 */
	private static final double maxSpeedInGraph = 10000;
	/** po��te�n� vrchol */
	private final Vertex startVertex;
	/** koncov� vrchol */
	private final Vertex endVertex;
	/** maxim�ln� rychlost p�enosu (Mb/s) */
	private final int maximumSpeed;
	/**
	 * stabilita dan�ho spoje v rozsahu 0-1, nap�. stabilita 0.9 znamen�, �e kdy� je
	 * spojen� vyt�en� na v�ce ne� 90 %, tak je 50% �ance, �e se 50 % dat ztrat�
	 */
	private final double stability;
	/**
	 * spolehliv� rychlost, je to maxim�ln� rychlost p�enosu, p�i kter� se zachov�
	 * 100% spolehlivost
	 */
	private final double reliableSpeed;
	/** aktu�ln� pr�tok */
	private double flow;

	/**
	 * Konstuktor, vytvo�� instanci t��dy.
	 * 
	 * @param startVertex
	 *            po��te�n� vrchol
	 * @param endVertex
	 *            koncov� vrchol
	 * @param maximumSpeed
	 *            maxim�ln� rychlost
	 * @param stability
	 *            stabilita dan�ho spoje
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
	 * Zv��� aktu�ln� pr�tok o parametr flow.
	 * 
	 * @param flow
	 *            hodnota, o kterou se pr�tok zv���
	 */
	public void flowInc(double flow) {
		this.flow += flow;
	}

	/**
	 * Vr�t� zb�vaj�c� kapacitu pro pr�tok. Maxim�ln� kapacitou je hodnota atributu
	 * reliableSpeed.
	 * 
	 * @return zb�vaj�c� kapacita
	 */
	public int capacityRemain() {
		return (int) (reliableSpeed - flow);
	}

	// ----METODY KVULI DIJKSTROVI----
	/**
	 * Vr�t� v�hu hrany. Po��t� se jako (maxim�ln� rychlost, kter� se m��e v grafu
	 * vyskytovat - spolehliv� rychlost). Je to kv�li tomu, ze Dijkstra vyhled�v�
	 * nejkrat�� cestu, my jsme ale pot�ebovali tu nejv�ce ohodnocenou, tud� jsme
	 * museli z cesty s nejlep��m (nejv�t��m) ohodnocen�m ud�lat cestu s t�m
	 * nejmen��m ohodnocen�m.
	 * 
	 * @return v�ha hrany
	 */
	public int getWeight() {
		return (int) (maxSpeedInGraph - reliableSpeed);
	}

	// ----- GETTERS, SETTERS, TO STRING -----
	/**
	 * P�evede hranu do textov� podoby.
	 * 
	 * @return textov� podoba hrany
	 */
	@Override
	public String toString() {
		return "[ID1=" + startVertex + ", ID2=" + endVertex + ", maximumSpeed=" + maximumSpeed + ", stability="
				+ stability + ", flow=" + flow + "]";
	}

	/**
	 * Vr�t� po��te�n� vrchol hrany.
	 * 
	 * @return po��te�n� vrchol
	 */
	public Vertex getStartVertex() {
		return startVertex;
	}

	/**
	 * Vr�t� koncov� vrchol hrany.
	 * 
	 * @return koncov� vrchol
	 */
	public Vertex getEndVertex() {
		return endVertex;
	}

	/**
	 * Vr�t� maxim�ln� rychlost.
	 * 
	 * @return maxim�ln� rychlost
	 */
	public int getMaximumSpeed() {
		return maximumSpeed;
	}

	/**
	 * Vr�t� stabilitu (spolehlivost) hrany.
	 * 
	 * @return spolehlivost
	 */
	public double getStability() {
		return stability;
	}

	/**
	 * Vr�t� maxim�ln� spolehlivou rychlost.
	 * 
	 * @return maxim�ln� spolehliv� rychlost
	 */
	public double getReliableSpeed() {
		return this.reliableSpeed;
	}

	/**
	 * Vr�t� aktu�ln� pr�tok hrany.
	 * 
	 * @return aktu�ln� pr�tok
	 */
	public double getFlow() {
		return flow;
	}

}
