package minijava.symboltable;

/**
 * ���ű�����Ҫ����ĳ�����
 * @author GeniusLJR
 *
 */
public abstract class MType {
	public int returntemp;
	public abstract String getName();
	public abstract MType getField();
	public abstract String getType();
	public abstract void setName(String _name);
	public abstract boolean insertVar(MType mvar);
}
