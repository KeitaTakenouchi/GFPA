package gfpa.graph.util;

import gfpa.graph.common.DirectedGraph;
import gfpa.graph.search.EdgeVisitor;

import java.io.File;

public class VisualizeGraph
{
	private VisualizeGraph()
	{
	}

	public static void exportDirectedEdges(DirectedGraph graph, File out, String type)
	{
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		graph.forEachEdge(new EdgeVisitor()
		{
			@Override
			public boolean perform(int from, int to)
			{
				gv.addln(from + " -> " + to);
				return true;
			}
		});
		gv.addln(gv.end_graph());
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
	}
}
