package minijava.symboltable;

/**
 * 存储一个整形变量
 * @author GeniusLJR
 *
 */
public class MInteger extends MType {
	private String name;//名字
	private MType field;//作用域
	public static final String type = "int";//标记是整形
	
	public MInteger() {
		name = "Integer_Literal";
	}
	
	public MInteger(MType integer_field) {
		name = "";
		field = integer_field;
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
