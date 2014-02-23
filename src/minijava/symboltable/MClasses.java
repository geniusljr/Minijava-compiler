package minijava.symboltable;

import java.util.Vector;

/**
 * 存储所有类的类
 * @author GeniusLJR
 *
 */
public class MClasses extends MType{
	
	public Vector<MClass> mclasses = new Vector<MClass>();//存储所有类
	
	/**
	 * 加入一个自己声明的类mclass
	 * @param mclass 要加入的类
	 * @return 如果加入成功返回true
	 */
	public boolean insertClass(MClass mclass) {
		String class_name = mclass.getName();
		if (isRepeatedClass(class_name))
			return false;
		mclasses.addElement(mclass);
		return true;
	}
	
	/**
	 * 是否存在class_name的类
	 * @param class_name 要判断的类的名字
	 * @return 如果存在返回true
	 */
	private boolean isRepeatedClass(String class_name) {
		int size = mclasses.size();
		for (int i = 0; i < size; i++) {
			if (mclasses.elementAt(i).getName().equals(class_name))
				return true;
		}
		return false;
	}
	
	/**
	 * 获得一个名字为class_name的类
	 * @param class_name 要获得的类的名字
	 * @return 如果存在则返回该类，否则返回null
	 */
	public MClass getClassByName(String class_name) {
		int size = mclasses.size();
		for (int i = 0; i < size; i++) {
			if (mclasses.elementAt(i).getName().equals(class_name))
				return mclasses.elementAt(i);
		}
		return null;
	}
	
	@Override
	public String getName() {
		return "mclasses";
	}
	@Override
	public void setName(String _name) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public MType getField() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "classes";
	}
	@Override
	public boolean insertVar(MType mvar) {
		// TODO Auto-generated method stub
		return false;
	}
}
