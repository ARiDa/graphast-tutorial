package org.graphast.tutorial;

import java.util.Date;

import org.graphast.config.Configuration;
import org.graphast.importer.CostGenerator;
import org.graphast.importer.OSMImporterImpl;
import org.graphast.model.Graph;
import org.graphast.query.route.shortestpath.dijkstra.Dijkstra;
import org.graphast.query.route.shortestpath.dijkstra.DijkstraLinearFunction;
import org.graphast.query.route.shortestpath.model.Instruction;
import org.graphast.query.route.shortestpath.model.Path;
import org.graphast.util.DateUtils;

public class TimeDependentShortestPathExample {

	public static void main(String[] args) {

		//Define Open Street Map file location.
		String osmFile = TimeDependentShortestPathExample.class.getResource("/monaco-150112.osm.pbf").getPath();
		//Define the local Graphast directory where the framework will store the Monaco graph information.
		String graphastMonacoDir = Configuration.USER_HOME + "/graphast/monaco";

		//Import the road network from Monaco.
		Graph graphMonaco = new OSMImporterImpl(osmFile, graphastMonacoDir).execute();
		//Generate synthetic costs for the time-dependent edges
		CostGenerator.generateAllSyntheticEdgesCosts(graphMonaco);

		//Get the source and target nodes for the Shortest Path execution.
		Long source = graphMonaco.getNodeId(43.740174,7.424376);
		Long target = graphMonaco.getNodeId(43.735554,7.416147);

		//Create the starting time that will be used in the time-dependent algorithm
		Date time = DateUtils.parseDate(12, 0, 0);
		
		//Instantiate the Time-dependent Dijkstra algorithm implementation 
		Dijkstra dijkstra =  new DijkstraLinearFunction(graphMonaco);
		//Run the time-dependent shortest path from the source and target nodes at the starting time defined previously.
		//The result is stored in a Path that shows the shortest path.
		Path sp = dijkstra.shortestPath(source, target, time);
		
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
