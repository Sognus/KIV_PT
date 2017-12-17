package gui;

import program.Edge;

/**
 * Mezitøída objektu Edge pro vytvoøení tabulky v JavaFX
 * 
 * @author Jakub Vítek
 *
 */
public class EdgeGUI {

	/** Formátovaný poèátek hrany */
	public String source;
	/** Formátovaný smìr hrany */
	public String target;
	/** Formátovaná maximální rychlost */
	public String maximumSpeed;
	/** Formátovaná spolehlivost hrany */
	public String reliability;
	/** Formátovaný aktuální datový tok */
	public String currentFlow;
	/** Formátované zatížení hrany */
	public String load;
	/** Cheat na vytvoøení tlaèítka */
	public EdgeGUI action;

	/**
	 * Getter pro poèátek hrany
	 * 
	 * @return poèátek hrany
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Getter pro smìr hrany
	 * 
	 * @return cíl hrany
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * Getter pro maximální rychlost
	 * 
	 * @return maximální rychlost
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
	 * Getter pro aktuální datový tok
	 * 
	 * @return Aktuální datový tok
	 */
	public String getCurrentFlow() {
		return currentFlow;
	}

	/**
	 * Getter pro aktuální vytížení
	 * 
	 * @return aktuální vytížení
	 */
	public String getLoad() {
		return load;
	}

	/**
	 * Konstruktor, který instance typu Edge vytvoøí mezitøídu pro zobrazení dat
	 * uložených ve tøídì Edge do tabulky
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
	 * Cheatovací getter
	 * 
	 * @return cheat
	 */
	public EdgeGUI getAction() {
		return this.action;
	}

}
