package minijava.typecheck;


public class Console {
	//重定义
	public static void Redefination(int linenum, String identifier_name) {
		System.out.println("Line " + linenum + "\t: ReDefination of identifier: " + identifier_name);
	}
	
	//循环继承
	public static void LoopExtending(int linenum, String classname1, String classname2) {
		System.out.println("Line " + linenum + "\t: Cycle detected: a cycle exists in the type hierarchy between '"+classname1+"' and '"+classname2+"'");
	}
	
	//类型不匹配
	public static void InvalidAssignment(int linenum, String from, String to) {
		System.out.println("Line " + linenum + "\t: Type mismatch: Cannot convert from "+from+" to "+to);
	}
	
	//数组类型错误
	public static void InvalidArray(int linenum, String type) {
		System.out.println("Line " + linenum + "\t: The type of the expression must be an array type but it resolved to "+type);
	}
	
	//&&,<,+,-,*,类型错误
	public static void InvalidExpression(int linenum, String operator, String left, String right) {
		System.out.println("Line " + linenum + "\t: The operator '"+operator+"' is undefined for the argument type(s) "+left+","+right);
	}
	
	//!类型错误
	public static void InvalidExpression(int linenum, String operator, String type) {
		System.out.println("Line " + linenum + "\t: The operator '"+operator+"' is undefined for the argument type "+type);
	}
	
	//非类型变量调用函数
	public static void InvalidType(int linenum, String type) {
		System.out.println("Line " + linenum + "\t: No method is defined for the type '"+type+"'");
		System.exit(0);
	}
	
	//未定义的方法
	public static void UndefinedMethod(int linenum, String classname, String methodname) {
		System.out.println("Line " + linenum + "\t: The method "+methodname+" is undefined for the type "+classname);
		System.exit(0);
	}
	//未定义的类
	public static void UndefinedClass(int linenum, String classname) {
		System.out.println("Line " + linenum + "\t: '"+classname+"' cannot be resolved to a type");
		System.exit(0);
	}
	
	//未定义的变量
	public static void UndefinedVariable(int linenum, String varname) {
		System.out.println("Line " + linenum + "\t: '"+varname+"' cannot be resolved to a variable");
		System.exit(0);
	}
	
	//方法参数类型不匹配
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
	
	//输出语句类型错误
	public static void InvalidPrint(int linenum, String type) {
		System.out.println("Line " + linenum + "\t: Type mismatch: The print method is defined for the type '"+type+"'");
		System.exit(0);
	}
	
	//重载错误
	public static void InvalidOverload(int linenum, String first, String second, String name) {
		System.out.println("Line " + linenum + "\t: Invalid overload: "+first+","+second+" @ "+name);
	}
}
