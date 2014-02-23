package spiglet.spiglet2kanga;

import java.util.HashSet;
import java.util.Vector;

/**
 * CFG�еĽڵ㣬��ӦSPIGLET��һ�����
 * @author GeniusLJR
 *
 */
public class FlowGraphNode extends NewMType{

	//��ǰSPIGLET�����ʹ�ñ����ļ���
	public HashSet<Integer> USE = new HashSet<Integer>();
	//��ǰSPIGLET����и�ֵ�����ļ���
	public HashSet<Integer> DEF = new HashSet<Integer>();
	//��ǰSPIGLET�������ǰ�Ļ�Ծ��������
	public HashSet<Integer> IN = new HashSet<Integer>();
	//��ǰSPIGLET�������֮��Ļ�Ծ��������
	public HashSet<Integer> OUT = new HashSet<Integer>();
	//��ǰSPIGLET����ǰ������ͨ��˳��ִ�л���ת������Ե��ﵱǰλ�õ�SPIGLET��伯��
	public Vector<FlowGraphNode> predecessors = new Vector<FlowGraphNode>();
	//��ǰSPIGLET���ĺ�̣���ͨ��˳��ִ�л���ת������Ե����SPIGLET��伯��
	public Vector<FlowGraphNode> successors = new Vector<FlowGraphNode>();
	
	private String nodeLabel = null;	//��������ţ�û����Ϊnull
	private String jumpLabel = null;	//������������ת��������JUMP��CJUMP��������ţ�û����Ϊnull
	private boolean cjump = false;		//�����CJUMP������Ϊtrue
	private boolean jump = false;		//�����JUMP������Ϊtrue
	private FlowGraph field;			//��ǰ�������CFG
	
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
