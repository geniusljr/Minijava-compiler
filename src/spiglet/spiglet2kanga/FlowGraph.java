package spiglet.spiglet2kanga;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * 每个方法对应一个CFG
 * 保存方法中每条语句的调用关系，USE、DEF、IN、OUT集合的信息
 * @author GeniusLJR
 *
 */
public class FlowGraph extends NewMType{
	
	public static String type = "FlowGraph";//表明自己是FlowGraph类型的字符串
	
	private String name;										//方法名称
	private int paramNum;										//方法参数个数
	private int maxCalledParam;									//方法中调用其他方法的最大参数个数
	private int spillNum;										//方法中需要的栈单元个数
	private int usedColorsNum;									//方法中所用t/s寄存器个数,即需要保存的寄存器个数
	private int spillRegNum;									//溢出单元个数
	private int spillIndex;										//指向下一个可用栈的标号
	public static final int MAX_COLOR = 4;						//可用寄存器个数
	private boolean[] active_color = new boolean[MAX_COLOR];	//标记当前可用寄存器
	public boolean[] used_colors = new boolean[MAX_COLOR];		//标记该方法所有用过的寄存器
	
	//保存所有语句的集合
	public Vector<FlowGraphNode> nodes = new Vector<FlowGraphNode>();
	
	//保存有标号的语句的集合，映射关系（代表标号的字符串---->语句）
	public Hashtable<String, FlowGraphNode> mapping = new Hashtable<String, FlowGraphNode>();
	
	//保存所有TEMP的集合，映射关系（TEMP编号---->该TEMP代表的自定义类的变量）
	public Hashtable<Integer, MyVariable> all_variables = new Hashtable<Integer, MyVariable>();
	
	/**
	 * 构造方法
	 * @param _name	方法名称
	 * @param _paramNum	方法参数个数
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
	 * 返回该方法的名称
	 * @return 方法名称
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 更新方法过程中调用其他方法的最大参数个数
	 * @param num 参数个数
	 */
	public void updateMaxCalledParam(int num) {
		maxCalledParam = (num > maxCalledParam) ? num : maxCalledParam;
	}
	
	/**
	 * 更新方法过程中用过的t/s寄存器个数
	 */
	private void updateUsedColorsNum() {
		for (int i = 0; i < MAX_COLOR; i++) 
			if (used_colors[i]) usedColorsNum++;
	}
	
	/**
	 * 更新方法过程中所用栈单元个数
	 */
	private void updateSpillNum() {
		spillNum += (paramNum > 4) ? paramNum-4 : 0;
		spillNum += usedColorsNum;
		spillNum += spillRegNum;
	}
	
	/**
	 * 返回该方法参数个数，对应kanga中方法声明处第一个参数
	 * @return 方法参数个数
	 */
	public int getParamNum() {
		return paramNum;
	}
	
	/**
	 * 返回该方法过程中需要栈单元的个数，对应kanga中方法声明处第二个参数
	 * @return 方法过程中需要栈单元的个数
	 */
	public int getSpillNum() {
		return spillNum;
	}
	
	/**
	 * 返回该方法过程中调用其他方法的最大参数个数，对应kanga中方法声明处第三个参数
	 * @return 方法过程中调用其他方法的最大参数个数
	 */
	public int getMaxCalledParam() {
		return maxCalledParam;
	}

	//通过语句标号获得语句
	private FlowGraphNode getNodeByLabel(String label) {
		return mapping.get(label);
	}
	
	/**
	 * 向CFG中添加语句节点
	 * @param node
	 */
	public void addNode(FlowGraphNode node) {
		
		nodes.add(node);
		
		//如果该语句具有标号，则同时向mapping集合添加
		if (node.getNodeLabel() != null)
			mapping.put(node.getNodeLabel(), node);
	}
	
	//在CFG中将src,dst两个节点连接起来
	private void linkNodes(FlowGraphNode src, FlowGraphNode dst) {
		src.successors.add(dst);
		dst.predecessors.add(src);
	}
	
	/**
	 * 活性分析、并按照LinearScan算法分配寄存器
	 */
	public void make() {
		
		int size = nodes.size();
		
		//连接控制流图中的所有节点
		for (int i = 0; i < size; i++) {
			FlowGraphNode current = nodes.elementAt(i);
			//如果是跳转语句，将其连接到跳转标号所在语句即可
			if (current.ifJump()) {
				linkNodes(current, this.getNodeByLabel(current.getJumpLabel()));
			}
			//否则
			else {
				//如果是条件跳转语句，先把它连接到跳转标号所在语句
				if (current.ifCJump())
					linkNodes(current, this.getNodeByLabel(current.getJumpLabel()));
				//除跳转语句外和最后一条语句外，把所有语句到连接到它在nodes中的下一个元素
				if (i < size-1)
					linkNodes(current, this.nodes.elementAt(i+1));
			}
		}
		
		//活性分析
		liveAnylasis();		
		//寄存器分配
		linearScan();
		
		//更新所用寄存器个数
		this.updateUsedColorsNum();
		//更新溢出单元个数
		this.updateSpillNum();
		//更新所有溢出单元所用栈单元的编号
		this.updateSpillIndex();
		
	}
	
	private void liveAnylasis() {
		int size = nodes.size();
		
		for(Integer x : nodes.lastElement().USE) {
			all_variables.put(x, new MyVariable(x));
			nodes.lastElement().IN.add(x);
		}
		
		
		//循环计算每条语句的IN集合
		//直到每条语句IN集合所含元素都不变为止
		//IN[i] = (∪IN[s]-DEF[i])+USE[i],s∈i.successors
		boolean isEnd = false;
		while (!isEnd) {
			isEnd = true;
			for (int j = size-2; j >= 0; j--) {//最后一句一定不是跳转 所以没有后继
				FlowGraphNode temp = nodes.elementAt(j);
				
				//先保存该条语句上次计算结果，以便判断是否需要下次循环
				HashSet<Integer> last_time = new HashSet<Integer>();
				last_time.addAll(temp.IN);
				
				//∪IN[s]
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
				
				//如果已经标记为需要下次循环计算，则不用再判断一次
				if (!isEnd) continue;
				//否则如果该条语句在这次计算之后有所改变，标记isEnd为false，即还需要下次循环
				if (!last_time.containsAll(temp.IN) || !temp.IN.containsAll(last_time))
					isEnd = false;
			}
		}
		
		//更新OUT集合
		//OUT[i] = ∪IN[s],s∈i.successors
		for (int i = 0; i < size; i++)
			nodes.elementAt(i).updateOUT();
	}
	
	private void linearScan() {
		int size = nodes.size();
		
		//计算每个变量的活性区间
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
		
		//线性扫描算法，扫描到某一条语句时当前活跃变量的集合
		HashSet<MyVariable> currentLive = new HashSet<MyVariable>();
		for (int i = 0; i < size; i++) {
			
			FlowGraphNode temp = nodes.elementAt(i);
			
			//先求出当前活跃变量的集合
			for (Integer x : temp.IN)
				currentLive.add(all_variables.get(x));
			Vector<MyVariable> to_delete = new Vector<MyVariable>(); 
			for (MyVariable x : currentLive) {
				//将活性区间终点在当前语句之前的变量去掉
				if (x.getEnding() < i) { 
					//如果该变量之前没有被溢出，则将其使用的寄存器标记为可用
					if (!x.isSpilled())	active_color[x.getColor()] = false;
					to_delete.add(x);
				}
			}
			currentLive.removeAll(to_delete);
			
			//分配寄存器
			for (MyVariable x : currentLive) {
				
				//如果已被染色或已经溢出则不再考虑
				if (x.isSpilled()) continue;
				if (x.isColored()) continue;
				
				int freecolor = getFreeColor();
				//如果有多余寄存器可用，则分配
				if (freecolor != -1) {
					active_color[freecolor] = true;
					x.setColor(freecolor);
					used_colors[freecolor] = true;
				}
				//否则，进行溢出处理，将在当前所有活跃变量(没有溢出且已被染色)中活性区间终点最大的变量标记为溢出
				else {
					//计算活性区间终点最大的变量
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
					//如果最晚结束的变量的区间终点与自己一样，则将自己标记为溢出
					if (max == x.getEnding()) x.setSpill(spillIndex++);
					//否则把最晚结束的变量的寄存器分配给自己，并且将其标记为溢出
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
	
	//返回下一个可用寄存器编号，没有则返回1
	private int getFreeColor() {
		for (int i = 0; i < MAX_COLOR; i++) {
			if (!active_color[i]) return i;
		}
		return -1;
	}
	
	//更新所有溢出单元的所用栈单元编号
	private void updateSpillIndex() {
		
		//栈的前几位分配给多余的参数个数，即需要保存的寄存器
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
