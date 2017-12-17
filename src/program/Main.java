package program;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import gui.GUI;

/**
 * Hlavní tøída, kde se volají všechny ostatní metody.
 * 
 * @author Jitka Poubová
 * @author Jakub Vítek
 */
public final class Main {

	/** Data, se kterými aplikace pracuje - Graph */
	public static Graph graph;

	/** Data, se kterými aplikace pracuje - Požadavky */
	public static List<Request> requests;

	/** Simulaèní tøída */
	public static Simulation simulation;
	public static int simulationStep;
	public static FileHandler fileHandler;

	public static final String fn = "data/simulace-large.txt";
	public static final String fn_data = "data/data.txt";
	public static final String fn_out_sim = "out/simulationInfo.txt";

	/** Konstanty */

	/**
	 * Soukromý konstruktor na potlaèení chyby PMD
	 */
	private Main() {

	}

	/**
	 * Hlavní metoda, ze které jsou volány ostatní metody.
	 * 
	 * @param args
	 *            argumenty, zde nevyužito
	 */
	public static void main(String[] args) {

		// No, cosi asi nepotøebné
		Scanner sc = new Scanner(System.in);

		// Inicializace FileHandleru
		fileHandler = new FileHandler();

		// Naètení grafové struktury
		graph = fileHandler.loadGraph(fn_data);
		// Naètení požadavkù ze souboru
		requests = (ArrayList<Request>) fileHandler.loadSimulationData(graph, fn);

		// Inicializace simulace
		simulation = new Simulation(requests, graph);
		simulationStep = 0;

		// Grafické uživatelské rozhraní - START
		handleGUI();

		// TODO: Teoreticky cokoliv za tímto bodem nemá smysl
		/*
		 * 
		 * ****************************************************
		 ***************************************************** 
		 * ****************************************************
		 * 
		 */

		// sledovani uzlu
		System.out.println("Id of router (number between 10 and 15) you want to watch (0=none): ");
		int id = sc.nextInt();
		simulation.start();
		if (id > 0) {
			System.out.println(graph.getVertex(id).printVertex());
		}
		System.out.println(simulation.printSimulationInfo());

		// zruseni hrany
		System.out.println("Ids of edge you want to remove (numbers between 10 and 15): ");
		int id1 = sc.nextInt();
		int id2 = sc.nextInt();
		graph.removeEdge(id1, id2);
		simulation.start();
		if (id > 0) {
			System.out.println(graph.getVertex(id).printVertex());
		}
		System.out.println(simulation.printSimulationInfo());

		// zapis do souboru
		fileHandler.writeSimulation(simulation.printSimulationInfo() + simulation.printRequests(), fn_out_sim);

		sc.close();
	}

	/**
	 * Metoda pro grafické uživatelské rozhraní
	 * 
	 */
	private static void handleGUI() {
		// Otevøení Grafického rozhraní
		GUI.start();
	}
}
