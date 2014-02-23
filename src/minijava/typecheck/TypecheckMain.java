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
    		
    		//�����������ű�
    		root.accept(new BuildSymbolTableVisitor(), Mainclass.my_classes);
    		
    		//���������ű�����̳е���Ϣ
    		root.accept(new BuildSymbolTableVisitorExtra(), Mainclass.my_classes);
    		
    		//����Ƿ�ʹ����δ������ࡢ����������
    		root.accept(new CheckUndefineVisitor(), Mainclass.my_classes);
    		
    		//�������ƥ�䡢����ƥ��
    		root.accept(new TypeCheckingVisitor(), Mainclass.my_classes);
    		
    		//����Ƿ��в��Ϸ�������
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