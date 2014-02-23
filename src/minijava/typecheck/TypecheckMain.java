package minijava.typecheck;

import Mainclass.Mainclass;
import minijava.MiniJavaParser;
import minijava.ParseException;
import minijava.TokenMgrError;
import minijava.syntaxtree.Node;
import minijava.typecheck.BuildSymbolTableVisitor;

public class TypecheckMain { 
	
    public static void Start() {
    	try {
    		Node root = new MiniJavaParser(System.in).Goal();
    		
    		//初步建立符号表
    		root.accept(new BuildSymbolTableVisitor(), Mainclass.my_classes);
    		
    		//完整化符号表，加入继承等信息
    		root.accept(new BuildSymbolTableVisitorExtra(), Mainclass.my_classes);
    		
    		//检查是否使用了未定义的类、变量、方法
    		root.accept(new CheckUndefineVisitor(), Mainclass.my_classes);
    		
    		//检查类型匹配、参数匹配
    		root.accept(new TypeCheckingVisitor(), Mainclass.my_classes);
    		
    		//检查是否有不合法的重载
    		root.accept(new CheckOverloadVisitor(), Mainclass.my_classes); 
    	}
    	catch(TokenMgrError e){
    		//Handle Lexical Errors
    		e.printStackTrace();
    	}
    	catch (ParseException e){
    		//Handle Grammar Errors
    		e.printStackTrace();
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }
}