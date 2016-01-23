package gfpa.graph.concrete;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class DominatorTreeTest
{

	@Test
	public void test01()
	{
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
		cfgraph.putEdge(6, 7);

		DominatorTree domtree = new DominatorTree(cfgraph);
//		domtree.dumpEdges();
		{
			int[] expected = {0,1,3,6};
			int[] actual = domtree.dominator(6);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {0,1,4};
			int[] actual = domtree.dominator(4);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int expected = 1;
			int actual = domtree.immediateDominator(4);
			assertEquals(expected, actual);
		}
		{
			int expected = 3;
			int actual = domtree.immediateDominator(6);
			assertEquals(expected, actual);
		}
		{
			int actual = domtree.immediateDominator(0);
			assertTrue(actual < 0);
		}
	}

	/**
	 * Speed test.
	 */
	@Test
	public void test02()
	{
		ControlFlowGraph cfgraph = new ControlFlowGraph(1);
		cfgraph.putEdge(1 ,2);
		cfgraph.putEdge(2, 3);
		cfgraph.putEdge(2, 4);
		cfgraph.putEdge(3, 5);
		cfgraph.putEdge(4, 5);
		cfgraph.putEdge(5, 6);

		DominatorTree domtree = new DominatorTree(cfgraph);
//		domtree.dumpEdges();
	}

}
