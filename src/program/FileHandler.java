package program;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Tøída, která pracuje se soubory. Umí naèíst data, která reprezentují
 * graf, a také data, která se použijí pøi simulaci.
 * 
 * @author Jitka Poubová
 * @author Jakub Vítek
 */
public class FileHandler {
	
	/**
	 * Naète data reprezentující graf ze souboru s názvem filename, 
	 * vrací naètený graf.
	 * 
	 * @param filename	název souboru
	 * @return			výsledný graf
	 */
	public Graph loadGraph(String filename) {		
		Graph graph = new Graph();

		try {
			BufferedReader bfr = new BufferedReader(new FileReader(filename));
			
			while (bfr.ready()) {
				String line = bfr.readLine();
				line = line.replaceAll("\\s","");
				String[] parsedLine = line.split("-"); // nebo pomlcka –
				
				int id1 = Integer.valueOf(parsedLine[0]);
				int id2 = Integer.valueOf(parsedLine[1]);
				int maximumSpeed = Integer.valueOf(parsedLine[2]);
				double stability = Double.valueOf(parsedLine[3]);
				
				graph.addEdge(id1, id2, maximumSpeed, stability);				
			}		
			
			bfr.close();
		} catch (IOException e) {
			System.out.println("Chyba pøi naèítání souboru.");
		}
		return graph;
	}

	/**
	 * Naète data pro simulaci (žádosti), ty vrací.
	 * 
	 * @param graph		graf, ve kterém se budou balíky dat posílat
	 * @param filename	název souboru s daty pro simulaci
	 * @return			seznam žádostí 
	 */
	public List<Request> loadSimulationData(Graph graph, String filename) {		
		ArrayList<Request> requests = new ArrayList<Request>();
		
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(filename));
			
			while (bfr.ready()) {
				String line = bfr.readLine();
				line = line.replaceAll("\\s","");
				String[] parsedLine = line.split("-"); // nebo pomlcka –
				
				int cas = Integer.valueOf(parsedLine[0]);
				int id1 = Integer.valueOf(parsedLine[1]);
				int id2 = Integer.valueOf(parsedLine[2]);
				int size = Integer.valueOf(parsedLine[3]);
				
				Vertex v1 = graph.getVertex(id1);
				Vertex v2 = graph.getVertex(id2);
				
				if (v1 == null || v2 == null) {
					System.out.println("Vrchol není v grafu!");
				}
				
				Request p = new Request(cas, v1, v2, size);
				requests.add(p);
			}		
			
			bfr.close();
		} catch (IOException e) {
			System.out.println("Chyba pøi naèítání souboru simulace.");
		}
	
		return requests;
	}
	
	/**
	 * Metoda zapíše do souboru informace o simulaci.
	 * 
	 * @param strSimulation	informace o simulaci, které se mají zapsat
	 * @param filename		jméno souboru
	 */
	public void writeSimulation(String strSimulation, String filename) {
		try {
			PrintStream ps = new PrintStream(filename);
			ps.print(strSimulation);
			ps.close();			
		} catch (FileNotFoundException e) {
			System.out.println("Chyba pøi zápisu do souboru!");
			e.printStackTrace();
		}
	}
	
}