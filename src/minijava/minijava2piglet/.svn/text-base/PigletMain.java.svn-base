package minijava.minijava2piglet;

import minijava.MiniJavaParser;
import minijava.ParseException;
import minijava.TokenMgrError;
import minijava.syntaxtree.Node;
import minijava.typecheck.BuildSymbolTableVisitor;
import minijava.typecheck.BuildSymbolTableVisitorExtra;
import minijava.typecheck.CheckOverloadVisitor;
import minijava.typecheck.CheckUndefineVisitor;
import minijava.typecheck.TypeCheckingVisitor;
import Mainclass.Mainclass;

public class PigletMain { 
 
	
    public static String Start() {
    	try {
    		
    		Node root = new MiniJavaParser(System.in).Goal();
    		
    		//初步建立符号表
    		root.accept(new BuildSymbolTableVisitor(),Mainclass.my_classes);
    		
    		//完整化符号表，加入继承等信息
    		root.accept(new BuildSymbolTableVisitorExtra(), Mainclass.my_classes);
    		
    		
    		//检查是否使用了未定义的类、变量、方法
    		root.accept(new CheckUndefineVisitor(), Mainclass.my_classes);
    		
    		//检查类型匹配、参数匹配
    		root.accept(new TypeCheckingVisitor(), Mainclass.my_classes);
    		
    		//检查是否有不合法的重载
    		root.accept(new CheckOverloadVisitor(), Mainclass.my_classes);
    		
    		//建立变量和方法的地址表
    		root.accept(new BuildOffsetTableVisitor(), Mainclass.my_classes);
    		
    		//翻译成SPIGLET
    		root.accept(new PigletVisitor(), Mainclass.my_classes);
    		
    		return Translate.ResultOutput();
    		
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
		return null;
    	
    }
}