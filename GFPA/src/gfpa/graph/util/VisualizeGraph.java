package gfpa.graph.util;

import gfpa.graph.common.DirectedGraph;
import gfpa.graph.common.LabeledDirectedGraph;
import gfpa.graph.info.Variable;
import gfpa.graph.search.EdgeVisitor;

import java.io.File;
import java.util.HashSet;

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
		addEdgesWithLabel(graph, gv);
		gv.addln(gv.end_graph());
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
	}

	public static void exportDirectedEdgesSideways(LabeledDirectedGraph<Variable> graph, File out, String type)
	{
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		gv.addln(" graph [rankdir = LR];");
		addEdgesWithLabel(graph, gv);
		gv.addln(gv.end_graph());
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
	}

	private static void addEdgesWithLabel(LabeledDirectedGraph<Variable> graph, GraphViz gv)
	{
		graph.forEachEdge(new EdgeVisitor()
		{
			@Override
			public boolean perform(int from, int to)
			{
				HashSet<Variable> labels = graph.getLabels(from, to);
				if(labels == null)
				{
					gv.addln(from + " -> " + to);
				}
				else
				{
					StringBuffer bf = new StringBuffer();
					for(Variable v : labels)
						bf.append(v.toString()+",");
					bf.deleteCharAt(bf.length()-1);
					gv.addln(from + " -> " + to + "[label = \"" + bf.toString() +"\"];");
				}
				return true;
			}
		});
	}

}
