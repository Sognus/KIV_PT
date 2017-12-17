package gui;

import program.Edge;

/**
 * Mezit��da objektu Edge pro vytvo�en� tabulky v JavaFX
 * 
 * @author Jakub V�tek
 *
 */
public class EdgeGUI {

	/** Form�tovan� po��tek hrany */
	public String source;
	/** Form�tovan� sm�r hrany */
	public String target;
	/** Form�tovan� maxim�ln� rychlost */
	public String maximumSpeed;
	/** Form�tovan� spolehlivost hrany */
	public String reliability;
	/** Form�tovan� aktu�ln� datov� tok */
	public String currentFlow;
	/** Form�tovan� zat�en� hrany */
	public String load;
	/** Cheat na vytvo�en� tla��tka */
	public EdgeGUI action;

	/**
	 * Getter pro po��tek hrany
	 * 
	 * @return po��tek hrany
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Getter pro sm�r hrany
	 * 
	 * @return c�l hrany
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * Getter pro maxim�ln� rychlost
	 * 
	 * @return maxim�ln� rychlost
	 */
	public String getMaximumSpeed() {
		return maximumSpeed;
	}

	/**
	 * Getter pro spolehlivost hrany
	 * 
	 * @return spolehlivost hrany
	 */
	public String getReliability() {
		return reliability;
	}

	/**
	 * Getter pro aktu�ln� datov� tok
	 * 
	 * @return Aktu�ln� datov� tok
	 */
	public String getCurrentFlow() {
		return currentFlow;
	}

	/**
	 * Getter pro aktu�ln� vyt�en�
	 * 
	 * @return aktu�ln� vyt�en�
	 */
	public String getLoad() {
		return load;
	}

	/**
	 * Konstruktor, kter� instance typu Edge vytvo�� mezit��du pro zobrazen� dat
	 * ulo�en�ch ve t��d� Edge do tabulky
	 * 
	 * @param e
	 *            hrana k transformaci
	 */
	public EdgeGUI(Edge e) {
		this.source = new String("" + e.getStartVertex().getId());
		this.target = new String("" + e.getEndVertex().getId());
		this.maximumSpeed = new String("" + e.getMaximumSpeed());
		this.reliability = new String("" + (e.getStability() * 100) + "%");
		this.currentFlow = new String("" + e.getFlow());
		this.load = new String("" + (e.getFlow() / e.getMaximumSpeed() * 100) + "%");
		this.action = this;
	}

	/**
	 * Cheatovac� getter
	 * 
	 * @return cheat
	 */
	public EdgeGUI getAction() {
		return this.action;
	}

}
