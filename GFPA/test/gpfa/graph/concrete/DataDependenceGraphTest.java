package gpfa.graph.concrete;

import static org.junit.Assert.*;
import gpfa.graph.concrete.ControlFlowGraph;
import gpfa.graph.concrete.DataDependenceGraph;
import gpfa.graph.info.Variable;

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

		DataDependenceGraph dfgraph = new DataDependenceGraph(cfgraph);
		dfgraph.def(1, new Variable("j"));
		dfgraph.def(2, new Variable("i"));
		dfgraph.use(3, new Variable("i"));
		dfgraph.def(3, new Variable("i"));
		dfgraph.use(4, new Variable("j"));
		dfgraph.def(4, new Variable("j"));
		dfgraph.use(5, new Variable("j"));
		dfgraph.def(5, new Variable("c"));
		dfgraph.use(6, new Variable("i"));
		dfgraph.def(6, new Variable("j"));
		dfgraph.use(7, new Variable("i"));
		dfgraph.def(7, new Variable("c"));
		dfgraph.def(8, new Variable("i"));
		dfgraph.buildEdges();

		{
			int[] expected = {4};
			int[] actual = dfgraph.getSuccessors(1);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {3};
			int[] actual = dfgraph.getSuccessors(2);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {3,6,7};
			int[] actual = dfgraph.getSuccessors(3);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {4,5};
			int[] actual = dfgraph.getSuccessors(4);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {};
			int[] actual = dfgraph.getSuccessors(5);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {4};
			int[] actual = dfgraph.getSuccessors(6);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {};
			int[] actual = dfgraph.getSuccessors(7);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {3,6,7};
			int[] actual = dfgraph.getSuccessors(8);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
	}

}
