package spiglet.spiglet2kanga;

/**
 * 表示代码中每个变量的类
 * @author GeniusLJR
 *
 */
public class MyVariable {

	private int value;//Spiglet代码中对应的TEMP编号
	private int live_interval_begin;//LSRA算法中变量的活跃起始位置
	private int live_interval_ending;//LSRA算法中变量的活跃终止位置
	private boolean isSpilled;//标记是否溢出
	private boolean isColored;//标记是否已被染色
	private int color;//如果被染色，记录其颜色（即分配的寄存器）
	private int spillNum;//如果溢出，记录其在运行栈中位置
	
	public MyVariable (Integer _value) {
		value = _value.intValue();
		live_interval_begin = Integer.MAX_VALUE;
		live_interval_ending = -1;
		isSpilled = false;
		isColored = false;
		color = -1;
	}
	
	public MyVariable() {}

	public void setBegin(int _begin) {
		live_interval_begin = _begin;
	}
	
	public void setEnding(int _ending) {
		live_interval_ending = _ending;
	}
	
	public int getBegin() {
		return live_interval_begin;
	}
	
	public int getEnding() {
		return live_interval_ending;
	}
	
	public int getValue() {
		return value;
	}
	
	/**
	 * 把变量标记为溢出
	 * @param _spillNum 代表该变量的栈单元的位置
	 */
	public void setSpill(int _spillNum) {
		isSpilled = true;
		spillNum = _spillNum;
	}
	
	public int getSpillNum() {
		return spillNum;
	}
	
	public boolean isSpilled() {
		return isSpilled;
	}
	
	/**
	 * 把变量标记为已被染色
	 * @param _color 代表该变量的颜色
	 */
	public void setColor(int _color) {
		color = _color;
		isColored = true;
	}
	
	public int getColor() {
		return color;
	}
	
	public boolean isColored() {
		return isColored;
	}
	
	/**
	 * 重写equals方法，如果两个变量对应SPIGLET代码中TEMP编号值一样，则相等
	 */
	public boolean equals(Object variable) {
		return this.value == ((MyVariable)variable).value;
	}
	
	/**
	 * 重写hashCode方法，返回变量对应SPIGLET代码中的TEMP编号值
	 */
	public int hashCode() {
		return value;
	}	
}
