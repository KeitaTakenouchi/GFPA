package gfpa.graph.concrete;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class ProgramDependeGraphTest
{

	/**
	 *0:int main(){
	 *1:	int sum = 0;
	 *2:	int i = 1;
	 *3:	while (i<11) {
	 *4:		sum = sum+i;
	 *5:		i = i + 1;
	 *6:	}
	 *7:	printf("%d \n", sum);
	 *8:	printf("%d \n", i);
	 *9:}
	 */
	@Test
	public void test01()
	{
		String[] source = new String[10];
		source[0] = "int main(){";
		source[1] = "\tint sum = 0;";
		source[2] = "\tint i = 1;";
		source[3] = "\twhile (i<11) {";
		source[4] = "\t\tsum = sum + i;";
		source[5] = "\t\ti = i + 1;";
		source[6] = "\t}";
		source[7] = "\tprintf(\"%d \\n\", sum);";
		source[8] = "\tprintf(\"%d \\n\", i);";
		source[9] = "}";

		ControlFlowGraph cfgraph = new ControlFlowGraph(0);
		cfgraph.putEdge(0, 1);
		cfgraph.putEdge(1, 2);
		cfgraph.putEdge(2, 3);
		cfgraph.putEdge(3, 4);
		cfgraph.putEdge(3, 6);
		cfgraph.putEdge(4, 5);
		cfgraph.putEdge(5, 6);
		cfgraph.putEdge(6, 3);
		cfgraph.putEdge(6, 7);
		cfgraph.putEdge(7, 8);
		cfgraph.putEdge(8, 9);

		ControlDependenceGraph cdgraph = new ControlDependenceGraph(cfgraph);
		DataDependenceGraph<String> dfgraph = new DataDependenceGraph<String>(cfgraph);
		dfgraph.def(1, new String("sum"));
		dfgraph.def(2, new String("i"));
		dfgraph.use(3, new String("i"));
		dfgraph.use(4, new String("sum"));
		dfgraph.def(4, new String("sum"));
		dfgraph.use(5, new String("i"));
		dfgraph.def(5, new String("i"));
		dfgraph.use(7, new String("sum"));
		dfgraph.use(8, new String("i"));
		dfgraph.buildEdges();

		ProgramDependenceGraph<String> pdgraph = new ProgramDependenceGraph<String>(dfgraph, cdgraph);
		{
			int[] expected = {0,2,3,5,6,8};
			int[] actual = pdgraph.backwardSlice(8);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			int[] expected = {0,1,2,3,4,5,6,7};
			int[] actual = pdgraph.backwardSlice(7);
			Arrays.sort(actual);
			assertArrayEquals(expected, actual);
		}
		{
			assertTrue(pdgraph.hasDataDependent(1, 4));
			assertTrue(pdgraph.hasDataDependent(5, 8));
			assertTrue(pdgraph.hasControlDependent(3, 4));
			assertTrue(pdgraph.hasControlDependent(3, 5));
		}
//		pdgraph.dumpEdges("Program Dependency Graph");
	}

	@Test
	public void test02()
	{
		ProgramDependenceGraph<String> pdgraph = new ProgramDependenceGraph<String>();
		pdgraph.putEdge(0, 1, new String("a"));
		pdgraph.putEdge(0, 2);
		pdgraph.putEdge(1, 3);
		pdgraph.putEdge(2, 3);
		pdgraph.putEdge(2, 3, new String("b"));

		assertTrue(pdgraph.hasDataDependent(0, 1));
		assertTrue(pdgraph.hasDataDependent(2, 3));
		assertTrue(pdgraph.hasControlDependent(0, 2));
		assertTrue(pdgraph.hasControlDependent(1, 3));
		assertTrue(pdgraph.hasControlDependent(2, 3));

		assertFalse(pdgraph.hasControlDependent(89, 3));
		assertFalse(pdgraph.hasControlDependent(0, 0));
	}

}
