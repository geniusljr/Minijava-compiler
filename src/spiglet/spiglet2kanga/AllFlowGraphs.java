package spiglet.spiglet2kanga;

import java.util.Hashtable;

/**
 * �洢����CFG����
 * @author GeniusLJR
 *
 */
public class AllFlowGraphs extends NewMType{
	
	//�洢����CFG��Hashtable����һ��������ʾCFG�����ƣ��ڶ���������Ӧ��CFG
	private Hashtable<String, FlowGraph> graphs = new Hashtable<String, FlowGraph>();
		
	/**
	 * ���һ��CFG
	 * @param graph
	 */
	public void addGraph(FlowGraph graph) {
		graphs.put(graph.getName(), graph);
	}

	/**
	 * ͨ���������õ���Ӧ��CFG
	 * @param name CFG����
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
