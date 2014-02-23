package spiglet.spiglet2kanga;

/**
 * 翻译时自底向上传递结果，记录当前节点应输出的字符串
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
