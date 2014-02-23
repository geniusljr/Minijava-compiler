package minijava.symboltable;

/**
 * 存储一个数组类型
 * @author GeniusLJR
 */
public class MArray extends MType{
	private String name;//名字
	private MType field;//作用域
	public static final String type = "int[]";//类型标记

	public MArray(MType array_field) {
		name = "";//名字为空
		field = array_field;
	}
	
	@Override
	public void setName(String _name) {
		name = _name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public MType getField() {
		return field;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public boolean insertVar(MType mvar) {
		// TODO Auto-generated method stub
		return false;
	}
}
