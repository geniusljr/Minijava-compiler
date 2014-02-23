package minijava.symboltable;

import java.util.Hashtable;
import java.util.Vector;

/**
 * 存储一个声明的类
 * @author GeniusLJR
 *
 */
public class MClass extends MType{
	private String name;//类的名字
	private MClasses field;//类的作用域
	private MClass father;//父类
	public static final String type = "class";//标记自己是一个类
	
	//存放所有变量Integer,Boolean,Array
	public Vector<MType> vars = new Vector<MType>();//类中声明的变量
	public Vector<MMethod> methods = new Vector<MMethod>();//类中声明的方法
	
	public Hashtable<String, Integer> variable_table = new Hashtable<String, Integer>();
	public Hashtable<String, Integer> method_table = new Hashtable<String, Integer>();
	
	public MClass(String class_name, MClasses class_field) {
		name = class_name;
		field = class_field;
		father = null;
	}
	
	/**
	 * 存储父类
	 * @param mclass 父类
	 */
	public void setFather(MClass mclass) {
		father = mclass;
	}
	
	/**
	 * 向类中加入一个变量mvar
	 * @return 如果加入成功则返回true
	 */
	public boolean insertVar(MType mvar) {
		String var_name = mvar.getName();
		if (isRepeatedVar(var_name))
			return false;
		vars.addElement(mvar);
		return true;
	}
	
	/**
	 * 判断一个类中是否已经声明了名字为var_name的变量
	 * @param var_name 要判断的变量名字
	 * @return 如果存在返回true
	 */
	private boolean isRepeatedVar(String var_name) {
		int size = vars.size();
		for (int i = 0; i < size; i++)
			if (vars.elementAt(i).getName().equals(var_name))
				return true;
		return false;
	}
	
	/**
	 * 向类中加入一个方法mmethod
	 * @param mmethod 要加入的方法
	 * @return 如果加入成功则返回true
	 */
	public boolean insertMethod(MMethod mmethod) {
		String method_name = mmethod.getName();
		if (isRepeatedMethod(method_name))
			return false;
		methods.addElement(mmethod);
		return true;
	}
	
	/**
	 * 判断类中是否声明了名字为method_name的方法
	 * @param method_name 要判断的方法的名字
	 * @return 如果存在返回true
	 */
	private boolean isRepeatedMethod(String method_name) {
		int size = methods.size();
		for (int i = 0; i < size; i++)
			if (methods.elementAt(i).getName().equals(method_name))
				return true;
		return false;
	}
	
	/**
	 * 获得父类
	 * @return 父类
	 */
	public MClass getFather() {
		return father;
	}
	
	/**
	 * 获得该类声明的名字为var_name的变量
	 * @param var_name 要获得的变量的名字
	 * @return 如果存在返回该变量，否则返回null
	 */
	public MType getVarByName(String var_name) {
		int size = vars.size();
		for (int i = 0; i < size; i++) {
			if (vars.elementAt(i).getName().equals(var_name))
				return vars.elementAt(i);
		}
		return null;
	}
	
	/**
	 * 获得该类声明的名字为method_name的方法
	 * @param method_name 要获得的方法的名字
	 * @return 如果存在返回该方法，否则返回null
	 */
	public MType getMethodByName(String method_name) {
		int size = methods.size();
		for (int i = 0; i < size; i++) {
			if (methods.elementAt(i).getName().equals(method_name))
				return methods.elementAt(i);
		}
		return null;
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
		return type;
	}
}
