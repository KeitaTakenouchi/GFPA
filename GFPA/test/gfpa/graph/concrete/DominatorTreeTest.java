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

	@Test
	public void test02()
	{
		ControlFlowGraph cfgraph = new ControlFlowGraph(2);
		cfgraph.putEdge(2 ,42);
		cfgraph.putEdge(2, 20);
		cfgraph.putEdge(42, 81);
		cfgraph.putEdge(20, 81);
		cfgraph.putEdge(20, 48);
		cfgraph.putEdge(81, 19);
		cfgraph.putEdge(19, -1);
		cfgraph.putEdge(19, 23);
		cfgraph.putEdge(48, 23);
		cfgraph.putEdge(23, -2);

		DominatorTree domtree = new DominatorTree(cfgraph);
//		domtree.dumpEdges();
		{
			int[] expected = {2};
			int[] actual = domtree.dominator(2);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {2,42};
			int[] actual = domtree.dominator(42);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {2,81};
			int[] actual = domtree.dominator(81);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {2,19,81};
			int[] actual = domtree.dominator(19);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {2,20,48};
			int[] actual = domtree.dominator(48);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {2,23};
			int[] actual = domtree.dominator(23);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
	}

}
