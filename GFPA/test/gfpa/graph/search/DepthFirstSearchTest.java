package gfpa.graph.search;

import static org.junit.Assert.*;
import gfpa.graph.concrete.ControlFlowGraph;

import java.util.Arrays;

import org.junit.Test;

public class DepthFirstSearchTest {

	@Test
	public void test01() {
		ControlFlowGraph cfgraph = new ControlFlowGraph(0);
		cfgraph.putEdge(0, 1);
		cfgraph.putEdge(1, 2);
		cfgraph.putEdge(1, 5);
		cfgraph.putEdge(1, 3);
		cfgraph.putEdge(2, 4);
		cfgraph.putEdge(5, 4);
		cfgraph.putEdge(4, 1);
		cfgraph.putEdge(4, 3);
		cfgraph.putEdge(3, 6);
		cfgraph.putEdge(6, 3);
		cfgraph.putEdge(6, 200);

		int[] actual = new int[cfgraph.size()];
		DepthFirstSearch.search(cfgraph, 0,	new DepthFirstSearchVisitor()
		{
			int i = 0;
			@Override
			public boolean onVisit(int to)
			{
				actual[i++] = to;
				return true;
			}
			@Override
			public boolean onLeave(int id)
			{
				return true;
			}
		});
		int[] expected = {0, 1, 3, 6, 200, 5, 4, 2};
		Arrays.sort(expected);
//		System.out.println(Arrays.toString(actual));
		Arrays.sort(actual);
		assertArrayEquals(expected, actual);
	}

	@Test
	public void test02() {
		ControlFlowGraph cfgraph = new ControlFlowGraph(0);
		cfgraph.putEdge(0, 1);
		cfgraph.putEdge(0, 2);
		cfgraph.putEdge(1, 2);
		cfgraph.putEdge(1, 3);
		cfgraph.putEdge(2, 3);
		cfgraph.putEdge(3, 4);
		cfgraph.putEdge(3, 5);

		int[] actual = new int[cfgraph.size()];
		DepthFirstSearch.search(cfgraph, 0,	new DepthFirstSearchVisitor()
		{
			int i = 0;
			@Override
			public boolean onVisit(int to)
			{
				actual[i++] = to;
				return true;
			}
			@Override
			public boolean onLeave(int id)
			{
				return true;
			}
		});
		int[] expected = {0, 1,3,4,5,2};
		Arrays.sort(expected);
//		System.out.println(Arrays.toString(actual));
		Arrays.sort(actual);
		assertArrayEquals(expected, actual);
	}

}
