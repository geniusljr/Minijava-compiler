package minijava.symboltable;

/**
 * �洢һ�������ͱ���
 * @author GeniusLJR
 *
 */
public class MBoolean extends MType{
	private String name;//����
	private MType field;//������
	public static final String type = "boolean";//����ǲ�����

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
