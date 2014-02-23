package minijava.symboltable;

import java.util.Vector;

/**
 * �洢���������
 * @author GeniusLJR
 *
 */
public class MClasses extends MType{
	
	public Vector<MClass> mclasses = new Vector<MClass>();//�洢������
	
	/**
	 * ����һ���Լ���������mclass
	 * @param mclass Ҫ�������
	 * @return �������ɹ�����true
	 */
	public boolean insertClass(MClass mclass) {
		String class_name = mclass.getName();
		if (isRepeatedClass(class_name))
			return false;
		mclasses.addElement(mclass);
		return true;
	}
	
	/**
	 * �Ƿ����class_name����
	 * @param class_name Ҫ�жϵ��������
	 * @return ������ڷ���true
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
	 * ���һ������Ϊclass_name����
	 * @param class_name Ҫ��õ��������
	 * @return ��������򷵻ظ��࣬���򷵻�null
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
