package spiglet.spiglet2kanga;

import java.util.HashSet;
import java.util.Vector;

/**
 * CFG中的节点，对应SPIGLET的一条语句
 * @author GeniusLJR
 *
 */
public class FlowGraphNode extends NewMType{

	//当前SPIGLET语句中使用变量的集合
	public HashSet<Integer> USE = new HashSet<Integer>();
	//当前SPIGLET语句中赋值变量的集合
	public HashSet<Integer> DEF = new HashSet<Integer>();
	//当前SPIGLET语句运行前的活跃变量集合
	public HashSet<Integer> IN = new HashSet<Integer>();
	//当前SPIGLET语句运行之后的活跃变量集合
	public HashSet<Integer> OUT = new HashSet<Integer>();
	//当前SPIGLET语句的前驱，即通过顺序执行或跳转代码可以到达当前位置的SPIGLET语句集合
	public Vector<FlowGraphNode> predecessors = new Vector<FlowGraphNode>();
	//当前SPIGLET语句的后继，即通过顺序执行或跳转代码可以到达的SPIGLET语句集合
	public Vector<FlowGraphNode> successors = new Vector<FlowGraphNode>();
	
	private String nodeLabel = null;	//该条语句标号，没有则为null
	private String jumpLabel = null;	//该条语句可能跳转到（包括JUMP和CJUMP）的语句标号，没有则为null
	private boolean cjump = false;		//如果是CJUMP语句则记为true
	private boolean jump = false;		//如果是JUMP语句则记为true
	private FlowGraph field;			//当前语句所在CFG
	
	public FlowGraphNode(FlowGraph _field) {
		field = _field;
	}
	
	public void setField(FlowGraph _field) {
		field = _field;
	}
	
	public FlowGraph getField() {
		return field;
	}
	
	public void setJump() {
		jump = true;
	}
	
	public void setCJump() {
		cjump = true;
	}
	
	public boolean ifJump() {
		return jump;
	}
	
	public boolean ifCJump() {
		return cjump;
	}
	
	public void setNodeLabel(String _nodeLabel) {
		nodeLabel = _nodeLabel;
	}
	
	public void setJumpLabel(String _jumpLabel) {
		jumpLabel = _jumpLabel;
	}
	
	public String getNodeLabel() {
		return nodeLabel;
	}
	
	public String getJumpLabel() {
		return jumpLabel;
	}
	
	public void addPredecessor(FlowGraphNode pred) {
		predecessors.add(pred);
	}
	
	public void addSuccessor(FlowGraphNode succ) {
		successors.add(succ);
	}
	
	public void addUSE(String tempNum) {
		USE.add(Integer.parseInt(tempNum));
	}
	
	public void addDEF(String tempNum) {
		DEF.add(Integer.parseInt(tempNum));
	}
	
	public void addLiveVariables(Integer _live) {
		IN.add(_live);
	}
	
	public void deleteLiveVariables(Integer _live) {
		IN.remove(_live);
	}
	
	public void updateOUT() {
		for (FlowGraphNode x : this.successors) {
			OUT.addAll(x.IN);
		}
	}

	@Override
	public String getType() {
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
