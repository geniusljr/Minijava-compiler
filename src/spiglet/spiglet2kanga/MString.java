package spiglet.spiglet2kanga;

/**
 * ����ʱ�Ե����ϴ��ݽ������¼��ǰ�ڵ�Ӧ������ַ���
 * @author GeniusLJR
 *
 */
public class MString extends NewMType{
	
	private String name;

	public MString(){
		name = "";
	}
	
	public MString(String _name) {
		name = _name;
	}
		
	public void setName(String _name) {
		name = _name;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
