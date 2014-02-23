package kanga.kanga2mips;

public class Console {
	private static String code="";
	
	public static void Print(String statement) {
		code += statement;
	}
	
	public static void Println() {
		code += "\n";
	}
	
	public static void Println(String statement) {
		code += statement + "\n";
	}
	
	public static void ResultOutput() {
		System.out.println(code);
	}
	
}
