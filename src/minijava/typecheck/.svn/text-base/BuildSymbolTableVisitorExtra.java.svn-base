package minijava.typecheck;

import minijava.symboltable.MClass;
import minijava.symboltable.MClasses;
import minijava.symboltable.MIdentifier;
import minijava.symboltable.MType;
import minijava.syntaxtree.ClassExtendsDeclaration;
import minijava.syntaxtree.Goal;
import minijava.syntaxtree.Identifier;
import minijava.syntaxtree.TypeDeclaration;
import minijava.visitor.GJDepthFirst;

public class BuildSymbolTableVisitorExtra extends GJDepthFirst<MType, MType>{
	
	/**
	 * f0 -> MainClass()
	 * f1 -> ( TypeDeclaration() )*
	 * f2 -> <EOF>
	 */
	public MType visit(Goal n, MType argu) {
		
		MType _ret = null;
		n.f1.accept(this, argu);		
		return _ret;
   	}
   
	/**
	 * f0 -> ClassDeclaration()
	 *       | ClassExtendsDeclaration()
	 */
    public MType visit(TypeDeclaration n, MType argu) {
    	MType _ret=null;
    	n.f0.accept(this, argu);
    	return _ret;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "extends"
     * f3 -> Identifier()
     * f4 -> "{"
     * f5 -> ( VarDeclaration() )*
     * f6 -> ( MethodDeclaration() )*
     * f7 -> "}"
     */
    public MType visit(ClassExtendsDeclaration n, MType argu) {
    	
    	MType _ret=null;
        MClass mclass ;
        MClass mfather;
        String class_name ;
        String father_name;
        
        //处理类定义的标识符Identifier()
        //获得子类名字
        class_name = ((MIdentifier)n.f1.accept(this, argu)).getName();
        
        //处理类定义的标识符Identifier()
        //获得父类名字
        father_name = ((MIdentifier)n.f3.accept(this, argu)).getName();
        
        //在MClasses中获得以class_name和father_name命名的两个类
        mclass = (MClass) ((MClasses)argu).getClassByName(class_name);
        mfather =(MClass) ((MClasses)argu).getClassByName(father_name);
        
        //如果未找到说明未定义则报错
        if (mfather == null) {
        	Console.UndefinedVariable(n.f3.f0.beginLine, father_name);
        }
        //否则设置继承关系
        else
        	mclass.setFather(mfather);
        
        //检查是否有循环继承的的关系
        MClass temp = mclass;
        int size = ((MClasses)argu).mclasses.size();
        for (int i = 0; i < size; i++) {
        	temp = temp.getFather();
        	if (temp == null) 
        		break;
        	//如果沿着父类向上查询到了自己，说明出现了循环继承
        	else if (temp.getName().equals(class_name)){
        		Console.LoopExtending(n.f1.f0.beginLine, class_name, mclass.getFather().getName());
        		break;
        	}
        }    
	    return _ret;
	}
    /**
     * f0 -> <IDENTIFIER>
     */
    public MType visit(Identifier n, MType argu) {
    	//得到对应的字符串
 	   	MType _ret = new MIdentifier(n.f0.toString(), argu);
 	   	return _ret;
    }
}
