package gfpa.graph.util;

import gfpa.graph.common.DirectedGraph;
import gfpa.graph.common.LabeledDirectedGraph;
import gfpa.graph.concrete.DataDependenceGraph;
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
		addEdges(graph, gv);
		gv.addln(gv.end_graph());
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
	}

	public static void exportDirectedEdgesSideways(DirectedGraph graph, File out, String type)
	{
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		gv.addln(" graph [rankdir = LR];");
		addEdges(graph, gv);
		gv.addln(gv.end_graph());
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
	}

	public static void exportDirectedEdges(LabeledDirectedGraph<?> graph, File out, String type)
	{
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		addEdgesWithLabel(graph, gv);
		gv.addln(gv.end_graph());
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
	}

	public static void exportDirectedEdgesSideways(LabeledDirectedGraph<?> graph, File out, String type)
	{
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		gv.addln(" graph [rankdir = LR];");
		addEdgesWithLabel(graph, gv);
		gv.addln(gv.end_graph());
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
	}

	public static void exportAllDirectedEdges(DataDependenceGraph<?> graph, File out, String type)
	{
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		addAllEdgesWithLabel(graph, gv);
		gv.addln(gv.end_graph());
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
	}

	public static void exportAllDirectedEdgesSideways(DataDependenceGraph<?> graph, File out, String type)
	{
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		gv.addln(" graph [rankdir = LR];");
		addAllEdgesWithLabel(graph, gv);
		gv.addln(gv.end_graph());
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
	}

	private static void addEdges(DirectedGraph graph, GraphViz gv)
	{
		graph.forEachEdge(new EdgeVisitor()
		{
			@Override
			public boolean perform(int from, int to)
			{
				gv.addln(from + " -> " + to);
				return true;
			}
		});
	}

	private static void addEdgesWithLabel(LabeledDirectedGraph<?> graph, GraphViz gv)
	{
		graph.forEachEdge(new EdgeVisitor()
		{
			@Override
			public boolean perform(int from, int to)
			{
				HashSet<?> labels = graph.getLabels(from, to);
				if(labels == null)
				{
					gv.addln(from + " -> " + to +" [color = red];");
				}
				else
				{
					StringBuffer bf = new StringBuffer();
					for(Object v : labels)
						bf.append(v.toString()+",");
					bf.deleteCharAt(bf.length()-1);
					gv.addln(from + " -> " + to + "[label = \"" + bf.toString() +"\", color = darkgreen];");
				}
				return true;
			}
		});
	}

	private static void addAllEdgesWithLabel(DataDependenceGraph<?> ddgraph, GraphViz gv)
	{
		ddgraph.forAllEachEdge(new EdgeVisitor()
		{
			@Override
			public boolean perform(int from, int to)
			{
				HashSet<?> labels = ddgraph.getLabels(from, to);
				if(labels == null)
				{
					gv.addln(from + " -> " + to +" [color = red];");
				}
				else
				{
					StringBuffer bf = new StringBuffer();
					for(Object v : labels)
						bf.append(v.toString()+",");
					bf.deleteCharAt(bf.length()-1);
					if(ddgraph.getCFG().isSuccessor(from, to))
						gv.addln(from + " -> " + to + "[label = \"" + bf.toString() +"\", color = blue];");
					else
//						gv.addln(from + " -> " + to + "[label = \"" + bf.toString() +"\", color = darkgreen];");
						gv.addln(from + " -> " + to + "[color = darkgreen];");
				}
				return true;
			}
		});
	}


}
