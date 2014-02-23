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
    		
    		//�����������ű�
    		root.accept(new BuildSymbolTableVisitor(),Mainclass.my_classes);
    		
    		//���������ű�����̳е���Ϣ
    		root.accept(new BuildSymbolTableVisitorExtra(), Mainclass.my_classes);
    		
    		
    		//����Ƿ�ʹ����δ������ࡢ����������
    		root.accept(new CheckUndefineVisitor(), Mainclass.my_classes);
    		
    		//�������ƥ�䡢����ƥ��
    		root.accept(new TypeCheckingVisitor(), Mainclass.my_classes);
    		
    		//����Ƿ��в��Ϸ�������
    		root.accept(new CheckOverloadVisitor(), Mainclass.my_classes);
    		
    		//���������ͷ����ĵ�ַ��
    		root.accept(new BuildOffsetTableVisitor(), Mainclass.my_classes);
    		
    		//�����SPIGLET
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