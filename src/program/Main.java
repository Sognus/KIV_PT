package program;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import gui.GUI;

/**
 * Hlavn� t��da, kde se volaj� v�echny ostatn� metody.
 * 
 * @author Jitka Poubov�
 * @author Jakub V�tek
 */
public final class Main {

	/** Data, se kter�mi aplikace pracuje - Graph */
	public static Graph graph;

	/** Data, se kter�mi aplikace pracuje - Po�adavky */
	public static List<Request> requests;

	/** Simula�n� t��da */
	public static Simulation simulation;
	public static int simulationStep;
	public static FileHandler fileHandler;

	public static final String fn = "data/simulace-large.txt";
	public static final String fn_data = "data/data.txt";
	public static final String fn_out_sim = "out/simulationInfo.txt";

	/** Konstanty */

	/**
	 * Soukrom� konstruktor na potla�en� chyby PMD
	 */
	private Main() {

	}

	/**
	 * Hlavn� metoda, ze kter� jsou vol�ny ostatn� metody.
	 * 
	 * @param args
	 *            argumenty, zde nevyu�ito
	 */
	public static void main(String[] args) {

		// No, cosi asi nepot�ebn�
		Scanner sc = new Scanner(System.in);

		// Inicializace FileHandleru
		fileHandler = new FileHandler();

		// Na�ten� grafov� struktury
		graph = fileHandler.loadGraph(fn_data);
		// Na�ten� po�adavk� ze souboru
		requests = (ArrayList<Request>) fileHandler.loadSimulationData(graph, fn);

		// Inicializace simulace
		simulation = new Simulation(requests, graph);
		simulationStep = 0;

		// Grafick� u�ivatelsk� rozhran� - START
		handleGUI();

		// TODO: Teoreticky cokoliv za t�mto bodem nem� smysl
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
	 * Metoda pro grafick� u�ivatelsk� rozhran�
	 * 
	 */
	private static void handleGUI() {
		// Otev�en� Grafick�ho rozhran�
		GUI.start();
	}
}
