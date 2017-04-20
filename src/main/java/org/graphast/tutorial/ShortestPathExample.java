package org.graphast.tutorial;

import org.graphast.config.Configuration;
import org.graphast.importer.OSMImporterImpl;
import org.graphast.model.Graph;
import org.graphast.query.route.shortestpath.dijkstra.Dijkstra;
import org.graphast.query.route.shortestpath.dijkstra.DijkstraConstantWeight;
import org.graphast.query.route.shortestpath.model.Instruction;
import org.graphast.query.route.shortestpath.model.Path;

public class ShortestPathExample {

	public static void main(String[] args) {

		//Define Open Street Map file location.
		String osmFile = ShortestPathExample.class.getResource("/monaco-150112.osm.pbf").getPath();
		//Define the local Graphast directory where the framework will store the Monaco graph information.
		String graphastMonacoDir = Configuration.USER_HOME + "/graphast/monaco";

		//Import the road network from Monaco.
		Graph graphMonaco = new OSMImporterImpl(osmFile, graphastMonacoDir).execute();

		//Get the source and target nodes for the Shortest Path execution.
		Long source = graphMonaco.getNodeId(43.740174,7.424376);
		Long target = graphMonaco.getNodeId(43.735554,7.416147);

		//Instantiate the Dijkstra algorithm implementation 
		Dijkstra dijkstra = new DijkstraConstantWeight(graphMonaco);
		//Run the shortest path from the source and target nodes defined previously.
		//The result is stored in a Path that shows the shortest path.
		Path sp = dijkstra.shortestPath(source, target);

		//The Path contains a List of instructions to go from the source to the target node.
		//The Instruction type stores the street name (Label), distance (Distance) and travel time (Cost).
		System.out.println("Detailed Path: ");
		for(Instruction instruction : sp.getPath()) {
			System.out.println("\t Street Name: " + instruction.getLabel() + 
					", Distance: " + instruction.getDistance() + ", Travel Time: " + instruction.getCost());
		}
		
		//The Path type also stores the total distance and total travel time.
		System.out.println("Path Total Distance: " + sp.getTotalDistance());
		System.out.println("Path Total Travel Time: " + sp.getTotalCost());
		
	}

}
