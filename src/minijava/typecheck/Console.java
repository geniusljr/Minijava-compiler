package minijava.typecheck;


public class Console {
	//�ض���
	public static void Redefination(int linenum, String identifier_name) {
		System.out.println("Line " + linenum + "\t: ReDefination of identifier: " + identifier_name);
	}
	
	//ѭ���̳�
	public static void LoopExtending(int linenum, String classname1, String classname2) {
		System.out.println("Line " + linenum + "\t: Cycle detected: a cycle exists in the type hierarchy between '"+classname1+"' and '"+classname2+"'");
	}
	
	//���Ͳ�ƥ��
	public static void InvalidAssignment(int linenum, String from, String to) {
		System.out.println("Line " + linenum + "\t: Type mismatch: Cannot convert from "+from+" to "+to);
	}
	
	//�������ʹ���
	public static void InvalidArray(int linenum, String type) {
		System.out.println("Line " + linenum + "\t: The type of the expression must be an array type but it resolved to "+type);
	}
	
	//&&,<,+,-,*,���ʹ���
	public static void InvalidExpression(int linenum, String operator, String left, String right) {
		System.out.println("Line " + linenum + "\t: The operator '"+operator+"' is undefined for the argument type(s) "+left+","+right);
	}
	
	//!���ʹ���
	public static void InvalidExpression(int linenum, String operator, String type) {
		System.out.println("Line " + linenum + "\t: The operator '"+operator+"' is undefined for the argument type "+type);
	}
	
	//�����ͱ������ú���
	public static void InvalidType(int linenum, String type) {
		System.out.println("Line " + linenum + "\t: No method is defined for the type '"+type+"'");
		System.exit(0);
	}
	
	//δ����ķ���
	public static void UndefinedMethod(int linenum, String classname, String methodname) {
		System.out.println("Line " + linenum + "\t: The method "+methodname+" is undefined for the type "+classname);
		System.exit(0);
	}
	//δ�������
	public static void UndefinedClass(int linenum, String classname) {
		System.out.println("Line " + linenum + "\t: '"+classname+"' cannot be resolved to a type");
		System.exit(0);
	}
	
	//δ����ı���
	public static void UndefinedVariable(int linenum, String varname) {
		System.out.println("Line " + linenum + "\t: '"+varname+"' cannot be resolved to a variable");
		System.exit(0);
	}
	
	//�����������Ͳ�ƥ��
	public static void InvalidParamlist(int linenum, String[] param1, String[] param2) {
		System.out.print("Line " + linenum + "\t: The method InvalidArray(");
		if (param1.length != 0)
			System.out.print(param1[0]);
		for (int i = 1; i < param1.length; i++)
			System.out.print(","+param1[i]);
		System.out.print(") in the type Console is not applicable for the arguments (");
		if (param2.length != 0)
			System.out.print(param2[0]);
		for (int i = 1; i < param2.length; i++)
			System.out.print(","+param2[i]);
		System.out.println(")");
		System.exit(0);
	}
	
	//���������ʹ���
	public static void InvalidPrint(int linenum, String type) {
		System.out.println("Line " + linenum + "\t: Type mismatch: The print method is defined for the type '"+type+"'");
		System.exit(0);
	}
	
	//���ش���
	public static void InvalidOverload(int linenum, String first, String second, String name) {
		System.out.println("Line " + linenum + "\t: Invalid overload: "+first+","+second+" @ "+name);
	}
}
