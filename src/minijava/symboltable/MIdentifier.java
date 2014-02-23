package minijava.symboltable;

/**
 * 存储自己的类声明的变量，同时帮助获得所有变量的名字
 * @author GeniusLJR
 *
 */
public class MIdentifier extends MType{
	private String name;//名字
	private MType field;//作用域
	private String type;//类型
	private int line_num;//所在行号
	
	public MIdentifier(String identifier_name,  MType identifier_field) {
		name = identifier_name;
		type = name;
		field = identifier_field;
	}
	
	public void setType(String _type) {
		type = _type;
	}
	public void setLineNum(int n) {
		line_num = n;
	}
	
	public int getLineNum() {
		return line_num;
	}	
		
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String _name) {
		name = _name;		
	}
	
	@Override
	public MType getField() {
		return field;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return type;
	}

	@Override
	public boolean insertVar(MType mvar) {
		// TODO Auto-generated method stub
		return false;
	}

}
