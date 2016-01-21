package gfpa.graph.util;

import gfpa.graph.common.DirectedGraph;
import gfpa.graph.common.LabeledDirectedGraph;
import gfpa.graph.info.Variable;
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

	public static void exportDirectedEdgesSideways(DirectedGraph graph, File out, String type)
	{
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		gv.addln(" graph [rankdir = LR];");
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

	public static void exportDirectedEdges(LabeledDirectedGraph<Variable> graph, File out, String type)
	{
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		graph.forEachEdge(new EdgeVisitor()
		{
			@Override
			public boolean perform(int from, int to)
			{
				gv.addln(from + " -> " + to + "[label = \"" + graph.getLabels(from, to) +"];");
				return true;
			}
		});
		gv.addln(gv.end_graph());
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
	}

	public static void exportDirectedEdgesSideways(LabeledDirectedGraph<Variable> graph, File out, String type)
	{
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		gv.addln(" graph [rankdir = LR];");
		graph.forEachEdge(new EdgeVisitor()
		{
			@Override
			public boolean perform(int from, int to)
			{
				gv.addln(from + " -> " + to + "[label = \"" + graph.getLabels(from, to) +"];");
				return true;
			}
		});
		gv.addln(gv.end_graph());
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
	}

}
