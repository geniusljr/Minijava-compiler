package spiglet.spiglet2kanga;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * ÿ��������Ӧһ��CFG
 * ���淽����ÿ�����ĵ��ù�ϵ��USE��DEF��IN��OUT���ϵ���Ϣ
 * @author GeniusLJR
 *
 */
public class FlowGraph extends NewMType{
	
	public static String type = "FlowGraph";//�����Լ���FlowGraph���͵��ַ���
	
	private String name;										//��������
	private int paramNum;										//������������
	private int maxCalledParam;									//�����е�����������������������
	private int spillNum;										//��������Ҫ��ջ��Ԫ����
	private int usedColorsNum;									//����������t/s�Ĵ�������,����Ҫ����ļĴ�������
	private int spillRegNum;									//�����Ԫ����
	private int spillIndex;										//ָ����һ������ջ�ı��
	public static final int MAX_COLOR = 4;						//���üĴ�������
	private boolean[] active_color = new boolean[MAX_COLOR];	//��ǵ�ǰ���üĴ���
	public boolean[] used_colors = new boolean[MAX_COLOR];		//��Ǹ÷��������ù��ļĴ���
	
	//�����������ļ���
	public Vector<FlowGraphNode> nodes = new Vector<FlowGraphNode>();
	
	//�����б�ŵ����ļ��ϣ�ӳ���ϵ�������ŵ��ַ���---->��䣩
	public Hashtable<String, FlowGraphNode> mapping = new Hashtable<String, FlowGraphNode>();
	
	//��������TEMP�ļ��ϣ�ӳ���ϵ��TEMP���---->��TEMP������Զ�����ı�����
	public Hashtable<Integer, MyVariable> all_variables = new Hashtable<Integer, MyVariable>();
	
	/**
	 * ���췽��
	 * @param _name	��������
	 * @param _paramNum	������������
	 */
	public FlowGraph(String _name, int _paramNum) {
		name = _name;
		paramNum = _paramNum;
		maxCalledParam = 0;
		spillNum = 0;
		usedColorsNum = 0;
		spillRegNum = 0;
		spillIndex = 0;
	}
	
	/**
	 * ���ظ÷���������
	 * @return ��������
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * ���·��������е�����������������������
	 * @param num ��������
	 */
	public void updateMaxCalledParam(int num) {
		maxCalledParam = (num > maxCalledParam) ? num : maxCalledParam;
	}
	
	/**
	 * ���·����������ù���t/s�Ĵ�������
	 */
	private void updateUsedColorsNum() {
		for (int i = 0; i < MAX_COLOR; i++) 
			if (used_colors[i]) usedColorsNum++;
	}
	
	/**
	 * ���·�������������ջ��Ԫ����
	 */
	private void updateSpillNum() {
		spillNum += (paramNum > 4) ? paramNum-4 : 0;
		spillNum += usedColorsNum;
		spillNum += spillRegNum;
	}
	
	/**
	 * ���ظ÷���������������Ӧkanga�з�����������һ������
	 * @return ������������
	 */
	public int getParamNum() {
		return paramNum;
	}
	
	/**
	 * ���ظ÷�����������Ҫջ��Ԫ�ĸ�������Ӧkanga�з����������ڶ�������
	 * @return ������������Ҫջ��Ԫ�ĸ���
	 */
	public int getSpillNum() {
		return spillNum;
	}
	
	/**
	 * ���ظ÷��������е���������������������������Ӧkanga�з�������������������
	 * @return ���������е�����������������������
	 */
	public int getMaxCalledParam() {
		return maxCalledParam;
	}

	//ͨ������Ż�����
	private FlowGraphNode getNodeByLabel(String label) {
		return mapping.get(label);
	}
	
	/**
	 * ��CFG��������ڵ�
	 * @param node
	 */
	public void addNode(FlowGraphNode node) {
		
		nodes.add(node);
		
		//����������б�ţ���ͬʱ��mapping�������
		if (node.getNodeLabel() != null)
			mapping.put(node.getNodeLabel(), node);
	}
	
	//��CFG�н�src,dst�����ڵ���������
	private void linkNodes(FlowGraphNode src, FlowGraphNode dst) {
		src.successors.add(dst);
		dst.predecessors.add(src);
	}
	
	/**
	 * ���Է�����������LinearScan�㷨����Ĵ���
	 */
	public void make() {
		
		int size = nodes.size();
		
		//���ӿ�����ͼ�е����нڵ�
		for (int i = 0; i < size; i++) {
			FlowGraphNode current = nodes.elementAt(i);
			//�������ת��䣬�������ӵ���ת���������伴��
			if (current.ifJump()) {
				linkNodes(current, this.getNodeByLabel(current.getJumpLabel()));
			}
			//����
			else {
				//�����������ת��䣬�Ȱ������ӵ���ת����������
				if (current.ifCJump())
					linkNodes(current, this.getNodeByLabel(current.getJumpLabel()));
				//����ת���������һ������⣬��������䵽���ӵ�����nodes�е���һ��Ԫ��
				if (i < size-1)
					linkNodes(current, this.nodes.elementAt(i+1));
			}
		}
		
		//���Է���
		liveAnylasis();		
		//�Ĵ�������
		linearScan();
		
		//�������üĴ�������
		this.updateUsedColorsNum();
		//���������Ԫ����
		this.updateSpillNum();
		//�������������Ԫ����ջ��Ԫ�ı��
		this.updateSpillIndex();
		
	}
	
	private void liveAnylasis() {
		int size = nodes.size();
		
		for(Integer x : nodes.lastElement().USE) {
			all_variables.put(x, new MyVariable(x));
			nodes.lastElement().IN.add(x);
		}
		
		
		//ѭ������ÿ������IN����
		//ֱ��ÿ�����IN��������Ԫ�ض�����Ϊֹ
		//IN[i] = (��IN[s]-DEF[i])+USE[i],s��i.successors
		boolean isEnd = false;
		while (!isEnd) {
			isEnd = true;
			for (int j = size-2; j >= 0; j--) {//���һ��һ��������ת ����û�к��
				FlowGraphNode temp = nodes.elementAt(j);
				
				//�ȱ����������ϴμ��������Ա��ж��Ƿ���Ҫ�´�ѭ��
				HashSet<Integer> last_time = new HashSet<Integer>();
				last_time.addAll(temp.IN);
				
				//��IN[s]
				for (int k = 0; k < temp.successors.size(); k++) {
					FlowGraphNode succ = temp.successors.elementAt(k);
					for (Integer x : succ.IN) {
						all_variables.put(x, new MyVariable(x));
						temp.IN.add(x);
					}
				}
				
				//-DEF[i]
				for (Integer x : temp.DEF)
					temp.IN.remove(x);
				
				//+USE[i]
				for (Integer x : temp.USE) {
					all_variables.put(x, new MyVariable(x));
					temp.IN.add(x);
				}
				
				//����Ѿ����Ϊ��Ҫ�´�ѭ�����㣬�������ж�һ��
				if (!isEnd) continue;
				//������������������μ���֮�������ı䣬���isEndΪfalse��������Ҫ�´�ѭ��
				if (!last_time.containsAll(temp.IN) || !temp.IN.containsAll(last_time))
					isEnd = false;
			}
		}
		
		//����OUT����
		//OUT[i] = ��IN[s],s��i.successors
		for (int i = 0; i < size; i++)
			nodes.elementAt(i).updateOUT();
	}
	
	private void linearScan() {
		int size = nodes.size();
		
		//����ÿ�������Ļ�������
		for (int i = 0; i < size; i++) {
			FlowGraphNode current = nodes.elementAt(i);
			for (Integer x : current.IN) {
				MyVariable tmp = all_variables.get(x);
				if (i < tmp.getBegin())
					tmp.setBegin(i);
				if (i > tmp.getEnding())
					tmp.setEnding(i);
			}
		}
		
		//����ɨ���㷨��ɨ�赽ĳһ�����ʱ��ǰ��Ծ�����ļ���
		HashSet<MyVariable> currentLive = new HashSet<MyVariable>();
		for (int i = 0; i < size; i++) {
			
			FlowGraphNode temp = nodes.elementAt(i);
			
			//�������ǰ��Ծ�����ļ���
			for (Integer x : temp.IN)
				currentLive.add(all_variables.get(x));
			Vector<MyVariable> to_delete = new Vector<MyVariable>(); 
			for (MyVariable x : currentLive) {
				//�����������յ��ڵ�ǰ���֮ǰ�ı���ȥ��
				if (x.getEnding() < i) { 
					//����ñ���֮ǰû�б����������ʹ�õļĴ������Ϊ����
					if (!x.isSpilled())	active_color[x.getColor()] = false;
					to_delete.add(x);
				}
			}
			currentLive.removeAll(to_delete);
			
			//����Ĵ���
			for (MyVariable x : currentLive) {
				
				//����ѱ�Ⱦɫ���Ѿ�������ٿ���
				if (x.isSpilled()) continue;
				if (x.isColored()) continue;
				
				int freecolor = getFreeColor();
				//����ж���Ĵ������ã������
				if (freecolor != -1) {
					active_color[freecolor] = true;
					x.setColor(freecolor);
					used_colors[freecolor] = true;
				}
				//���򣬽�������������ڵ�ǰ���л�Ծ����(û��������ѱ�Ⱦɫ)�л��������յ����ı������Ϊ���
				else {
					//������������յ����ı���
					int max = x.getEnding();
					MyVariable t = new MyVariable();
					for (MyVariable y : currentLive) {
						if (y.isSpilled()) continue;
						if (!y.isColored()) continue;
						if (y.getEnding() > max) {
							max = y.getEnding();
							t = y;
						}
					}
					//�����������ı����������յ����Լ�һ�������Լ����Ϊ���
					if (max == x.getEnding()) x.setSpill(spillIndex++);
					//�������������ı����ļĴ���������Լ������ҽ�����Ϊ���
					else {
						x.setColor(t.getColor());
						used_colors[t.getColor()] = true;
						t.setSpill(spillIndex++);
					}
					spillRegNum++;
				}
			}
		}
	}
	
	//������һ�����üĴ�����ţ�û���򷵻�1
	private int getFreeColor() {
		for (int i = 0; i < MAX_COLOR; i++) {
			if (!active_color[i]) return i;
		}
		return -1;
	}
	
	//�������������Ԫ������ջ��Ԫ���
	private void updateSpillIndex() {
		
		//ջ��ǰ��λ���������Ĳ�������������Ҫ����ļĴ���
		int spillOffset = (this.paramNum > 4 ? this.paramNum-4 : 0)+this.usedColorsNum;
		
		Enumeration<Integer> keys = all_variables.keys();
		while (keys.hasMoreElements()) {
			MyVariable variable = all_variables.get(keys.nextElement());
			if (variable.isSpilled())
				variable.setSpill(variable.getSpillNum()+spillOffset);
		}
	}
	
	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setName(String _name) {
		// TODO Auto-generated method stub
	}
	
}
