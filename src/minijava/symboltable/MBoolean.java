package minijava.symboltable;

/**
 * 存储一个布尔型变量
 * @author GeniusLJR
 *
 */
public class MBoolean extends MType{
	private String name;//名字
	private MType field;//作用域
	public static final String type = "boolean";//标记是布尔型

	public MBoolean() {
		name = "Boolean_Literal";
	}
	
	public MBoolean(MType boolean_field) {
		name = "";
		field = boolean_field;
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
