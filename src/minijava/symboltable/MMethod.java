package minijava.symboltable;

import java.util.Hashtable;
import java.util.Vector;

/**
 * �洢һ������
 * @author GeniusLJR
 *
 */
public class MMethod extends MType{
	
	private String name;//����
	private String returntype;//����ֵ����
	private MClass field;//������
	
	public Vector<MType> paramlist = new Vector<MType>();//�����Ĳ����б�
	public Vector<MType> vars = new Vector<MType>();//�洢�����ڶ���ı���
	
	public Vector<MType> current_param = new Vector<MType>();//�ڵ��÷���ƥ������б��ʱ�����ʱ�����б����ศ���жϲ���ƥ��
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
	 * �򷽷��м���һ������
	 */
	public boolean insertVar(MType mvar) {
		String var_name = mvar.getName();
		if (isRepeatedVar(var_name))
			return false;
		vars.addElement(mvar);
		return true;
	}
	
	/**
	 * �жϷ������Ƿ��Ѿ���������Ϊvar_name�ı���
	 * @param var_name Ҫ�жϵı���������
	 * @return ��������򷵻���
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
	 * �ڷ���������ʱ���򷽷��м���һ����������
	 * @param mparam ����ı���
	 * @return ����ɹ������򷵻���
	 */
	public boolean insertParam(MType mparam) {
		String param_name = mparam.getName();
		if (isRepeatedParam(param_name))
			return false;
		paramlist.addElement(mparam);
		return true;
	}
	
	/**
	 * �жϷ������Ƿ��Ѿ�����������Ϊparam_name�Ĳ���
	 * @param param_name Ҫ�жϵı���������
	 * @return ��������򷵻���
	 */
	private boolean isRepeatedParam(String param_name) {
		int size = paramlist.size();
		for (int i = 0; i < size; i++)
			if (paramlist.elementAt(i).getName().equals(param_name))
				return true;
		return false;
	}
	
	/**
	 * �ӷ������������л��һ������Ϊvar_name�ı���
	 * @param var_name ����������
	 * @return ������򷵻ظñ�����û���򷵻�null
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
