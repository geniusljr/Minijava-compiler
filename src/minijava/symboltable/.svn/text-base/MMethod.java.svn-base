package minijava.symboltable;

import java.util.Hashtable;
import java.util.Vector;

/**
 * 存储一个方法
 * @author GeniusLJR
 *
 */
public class MMethod extends MType{
	
	private String name;//名字
	private String returntype;//返回值类型
	private MClass field;//作用域
	
	public Vector<MType> paramlist = new Vector<MType>();//方法的参数列表
	public Vector<MType> vars = new Vector<MType>();//存储方法内定义的变量
	
	public Vector<MType> current_param = new Vector<MType>();//在调用方法匹配参数列表的时候的临时参数列表，用类辅助判断参数匹配
	public Hashtable<String, Integer> variable_table = new Hashtable<String, Integer>();
	public Hashtable<String, Integer> param_table = new Hashtable<String, Integer>();
	
	public MMethod() {
	}
	
	public MMethod(String method_name, String method_returntype, MClass field_name) {
		name = method_name;
		returntype = method_returntype;
		field = field_name;
	}
	
	/**
	 * 向方法中加入一个变量
	 */
	public boolean insertVar(MType mvar) {
		String var_name = mvar.getName();
		if (isRepeatedVar(var_name))
			return false;
		vars.addElement(mvar);
		return true;
	}
	
	/**
	 * 判断方法中是否已经存在名字为var_name的变量
	 * @param var_name 要判断的变量的名字
	 * @return 如果存在则返回真
	 */
	private boolean isRepeatedVar(String var_name) {
		int size = vars.size();
		for (int i = 0; i < size; i++)
			if (vars.elementAt(i).getName().equals(var_name))
				return true;
		size = paramlist.size();
		for (int i = 0; i < size; i++)
			if (paramlist.elementAt(i).getName().equals(var_name))
				return true;
		return false;
	}
	
	/**
	 * 在方法的声明时，向方法中加入一个参数变量
	 * @param mparam 加入的变量
	 * @return 如果成功加入则返回真
	 */
	public boolean insertParam(MType mparam) {
		String param_name = mparam.getName();
		if (isRepeatedParam(param_name))
			return false;
		paramlist.addElement(mparam);
		return true;
	}
	
	/**
	 * 判断方法中是否已经存在了名字为param_name的参数
	 * @param param_name 要判断的变量的名字
	 * @return 如果存在则返回真
	 */
	private boolean isRepeatedParam(String param_name) {
		int size = paramlist.size();
		for (int i = 0; i < size; i++)
			if (paramlist.elementAt(i).getName().equals(param_name))
				return true;
		return false;
	}
	
	/**
	 * 从方法的声明段中获得一个名字为var_name的变量
	 * @param var_name 变量的名字
	 * @return 如果有则返回该变量，没有则返回null
	 */
	public MType getVarByName(String var_name) {
		int size = vars.size();
		for (int i = 0; i < size; i++) {
			if (vars.elementAt(i).getName().equals(var_name))
				return vars.elementAt(i);
		}
		size = paramlist.size();
		for (int i = 0; i < size; i++) {
			if (paramlist.elementAt(i).getName().equals(var_name))
				return paramlist.elementAt(i);
		}
		return null;
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
	public void setName(String _name) {
		name = _name;		
	}

	@Override
	public String getType() {
		return returntype;
	}
}
