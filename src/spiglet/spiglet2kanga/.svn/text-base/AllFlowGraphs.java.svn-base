package spiglet.spiglet2kanga;

import java.util.Hashtable;

/**
 * 存储所有CFG的类
 * @author GeniusLJR
 *
 */
public class AllFlowGraphs extends NewMType{
	
	//存储所有CFG的Hashtable，第一个参数表示CFG的名称，第二个参数对应该CFG
	private Hashtable<String, FlowGraph> graphs = new Hashtable<String, FlowGraph>();
		
	/**
	 * 添加一个CFG
	 * @param graph
	 */
	public void addGraph(FlowGraph graph) {
		graphs.put(graph.getName(), graph);
	}

	/**
	 * 通过名称来得到对应的CFG
	 * @param name CFG名称
	 * @return
	 */
	public FlowGraph getGraphByName(String name) {
		return graphs.get(name);
	}
	
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String _name) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
}
