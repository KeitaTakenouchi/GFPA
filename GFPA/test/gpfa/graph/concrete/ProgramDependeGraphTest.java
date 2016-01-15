package gpfa.graph.concrete;

import gnu.trove.set.hash.TIntHashSet;
import gpfa.graph.info.Variable;

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


		DataDependenceGraph dfgraph = new DataDependenceGraph(cfgraph);
		dfgraph.def(1, new Variable("sum"));
		dfgraph.def(2, new Variable("i"));
		dfgraph.use(3, new Variable("i"));
		dfgraph.use(4, new Variable("sum"));
		dfgraph.def(4, new Variable("sum"));
		dfgraph.use(5, new Variable("i"));
		dfgraph.def(5, new Variable("i"));
		dfgraph.use(7, new Variable("sum"));
		dfgraph.use(8, new Variable("i"));
		dfgraph.buildEdges();

		ProgramDependenceGraph pdgraph = new ProgramDependenceGraph(dfgraph, cdgraph);

//		cdgraph.dumpEdges("Control Dependence Graph");
//		dfgraph.dumpEdges("Data Flow Graph");
//		pdgraph.dumpEdges("Program Dependency Graph");

//		dumpCode(source, new TIntHashSet(pdgraph.backwardSlice(8)));
//		dumpCode(source, new TIntHashSet(pdgraph.backwardSlice(7)));
//		dumpCode(source, new TIntHashSet(pdgraph.backwardSlice(5)));
	}

	private void dumpCode(String[] source, TIntHashSet set)
	{
		System.out.println("-----------------------------");
		for(int i = 0 ; i < source.length ; i++)
		{
			if(set.contains(i))
				System.out.println(i+":"+source[i]);
		}
		System.out.println("-----------------------------");
	}

}
