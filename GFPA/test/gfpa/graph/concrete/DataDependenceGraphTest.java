package gfpa.graph.concrete;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class DataDependenceGraphTest
{

	@Test
	public void test01()
	{
		ControlFlowGraph cfgraph = new ControlFlowGraph(1);
		cfgraph.putEdge(1, 2);
		cfgraph.putEdge(2, 3);
		cfgraph.putEdge(3, 4);
		cfgraph.putEdge(4, 5);
		cfgraph.putEdge(5, 3);
		cfgraph.putEdge(5, 6);
		cfgraph.putEdge(6, 7);
		cfgraph.putEdge(7, 8);
		cfgraph.putEdge(7, 9);
		cfgraph.putEdge(8, 9);
		cfgraph.putEdge(9, 4);

		DataDependenceGraph<String> ddgraph = new DataDependenceGraph<String>(cfgraph);
		ddgraph.def(1, new String("j"));
		ddgraph.def(2, new String("i"));
		ddgraph.use(3, new String("i"));
		ddgraph.def(3, new String("i"));
		ddgraph.use(4, new String("j"));
		ddgraph.def(4, new String("j"));
		ddgraph.use(5, new String("j"));
		ddgraph.def(5, new String("c"));
		ddgraph.use(6, new String("i"));
		ddgraph.def(6, new String("j"));
		ddgraph.use(7, new String("i"));
		ddgraph.def(7, new String("c"));
		ddgraph.def(8, new String("i"));
		ddgraph.buildEdges();

		{
			int[] expected = {4};
			int[] actual = ddgraph.getSuccessors(1);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {3};
			int[] actual = ddgraph.getSuccessors(2);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {3,6,7};
			int[] actual = ddgraph.getSuccessors(3);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {4,5};
			int[] actual = ddgraph.getSuccessors(4);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {};
			int[] actual = ddgraph.getSuccessors(5);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {4};
			int[] actual = ddgraph.getSuccessors(6);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {};
			int[] actual = ddgraph.getSuccessors(7);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {3,6,7};
			int[] actual = ddgraph.getSuccessors(8);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
	}

	@Test
	public void test02()
	{
		ControlFlowGraph cfgraph = new ControlFlowGraph(1);
		cfgraph.putEdge(1, 2);
		cfgraph.putEdge(1, 3);
		cfgraph.putEdge(2, 4);
		cfgraph.putEdge(3, 4);
		cfgraph.putEdge(4, 5);

		DataDependenceGraph<String> ddgraph = new DataDependenceGraph<String>(cfgraph);
		ddgraph.def(1, new String("i"));
		ddgraph.def(2, new String("i"));
		ddgraph.use(4, new String("i"));

		ddgraph.buildEdges();
		{
			int[] expected = {4};
			int[] actual = ddgraph.getSuccessors(1);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {1,2};
			int[] actual = ddgraph.getPredecessors(4);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
	}

	@Test
	public void test03()
	{
		ControlFlowGraph cfgraph = new ControlFlowGraph(-1);
		cfgraph.putEdge(-1, 1);
		cfgraph.putEdge(1, -2);

		DataDependenceGraph<String> ddgraph = new DataDependenceGraph<String>(cfgraph);
		ddgraph.def(-1, new String("i"));
		ddgraph.def(-1, new String("j"));
		ddgraph.def(1, new String("i"));
		ddgraph.use(-2, new String("i"));
		ddgraph.use(-2, new String("j"));

		ddgraph.buildEdges();
		{
			int[] expected = {-2};
			int[] actual = ddgraph.getSuccessors(1);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {-1,1};
			int[] actual = ddgraph.getPredecessors(-2);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
	}

	@Test
	public void test04()
	{
		ControlFlowGraph cfgraph = new ControlFlowGraph(0);
		cfgraph.putEdge(0, 1);
		cfgraph.putEdge(1, 3);
		cfgraph.putEdge(0, 2);
		cfgraph.putEdge(2, 3);
		cfgraph.putEdge(3, 4);

		DataDependenceGraph<String> ddgraph = new DataDependenceGraph<String>(cfgraph);
		ddgraph.def(0, new String("i"));
		ddgraph.def(0, new String("j"));
		ddgraph.def(0, new String("k"));
		ddgraph.use(1, new String("i"));
		ddgraph.def(1, new String("i"));
		ddgraph.use(2, new String("k"));
		ddgraph.def(2, new String("k"));
		ddgraph.use(3, new String("i"));
		ddgraph.use(3, new String("k"));
		ddgraph.def(3, new String("j"));
		ddgraph.use(4, new String("j"));
		ddgraph.use(4, new String("k"));

		ddgraph.buildEdges();
		{
			int[] expected = {0,1,2};
			int[] actual = ddgraph.getPredecessors(3);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {0,2,3};
			int[] actual = ddgraph.getPredecessors(4);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
	}


}
