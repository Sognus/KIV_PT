package program;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * T��da, kter� pracuje se soubory. Um� na��st data, kter� reprezentuj�
 * graf, a tak� data, kter� se pou�ij� p�i simulaci.
 * 
 * @author Jitka Poubov�
 * @author Jakub V�tek
 */
public class FileHandler {
	
	/**
	 * Na�te data reprezentuj�c� graf ze souboru s n�zvem filename, 
	 * vrac� na�ten� graf.
	 * 
	 * @param filename	n�zev souboru
	 * @return			v�sledn� graf
	 */
	public Graph loadGraph(String filename) {		
		Graph graph = new Graph();

		try {
			BufferedReader bfr = new BufferedReader(new FileReader(filename));
			
			while (bfr.ready()) {
				String line = bfr.readLine();
				line = line.replaceAll("\\s","");
				String[] parsedLine = line.split("-"); // nebo pomlcka �
				
				int id1 = Integer.valueOf(parsedLine[0]);
				int id2 = Integer.valueOf(parsedLine[1]);
				int maximumSpeed = Integer.valueOf(parsedLine[2]);
				double stability = Double.valueOf(parsedLine[3]);
				
				graph.addEdge(id1, id2, maximumSpeed, stability);				
			}		
			
			bfr.close();
		} catch (IOException e) {
			System.out.println("Chyba p�i na��t�n� souboru.");
		}
		return graph;
	}

	/**
	 * Na�te data pro simulaci (��dosti), ty vrac�.
	 * 
	 * @param graph		graf, ve kter�m se budou bal�ky dat pos�lat
	 * @param filename	n�zev souboru s daty pro simulaci
	 * @return			seznam ��dost� 
	 */
	public List<Request> loadSimulationData(Graph graph, String filename) {		
		ArrayList<Request> requests = new ArrayList<Request>();
		
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(filename));
			
			while (bfr.ready()) {
				String line = bfr.readLine();
				line = line.replaceAll("\\s","");
				String[] parsedLine = line.split("-"); // nebo pomlcka �
				
				int cas = Integer.valueOf(parsedLine[0]);
				int id1 = Integer.valueOf(parsedLine[1]);
				int id2 = Integer.valueOf(parsedLine[2]);
				int size = Integer.valueOf(parsedLine[3]);
				
				Vertex v1 = graph.getVertex(id1);
				Vertex v2 = graph.getVertex(id2);
				
				if (v1 == null || v2 == null) {
					System.out.println("Vrchol nen� v grafu!");
				}
				
				Request p = new Request(cas, v1, v2, size);
				requests.add(p);
			}		
			
			bfr.close();
		} catch (IOException e) {
			System.out.println("Chyba p�i na��t�n� souboru simulace.");
		}
	
		return requests;
	}
	
	/**
	 * Metoda zap�e do souboru informace o simulaci.
	 * 
	 * @param strSimulation	informace o simulaci, kter� se maj� zapsat
	 * @param filename		jm�no souboru
	 */
	public void writeSimulation(String strSimulation, String filename) {
		try {
			PrintStream ps = new PrintStream(filename);
			ps.print(strSimulation);
			ps.close();			
		} catch (FileNotFoundException e) {
			System.out.println("Chyba p�i z�pisu do souboru!");
			e.printStackTrace();
		}
	}
	
}