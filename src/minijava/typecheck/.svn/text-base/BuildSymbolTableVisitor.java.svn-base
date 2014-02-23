package minijava.typecheck;

import minijava.symboltable.MArray;
import minijava.symboltable.MBoolean;
import minijava.symboltable.MClass;
import minijava.symboltable.MClasses;
import minijava.symboltable.MIdentifier;
import minijava.symboltable.MInteger;
import minijava.symboltable.MMethod;
import minijava.symboltable.MType;
import minijava.syntaxtree.ArrayType;
import minijava.syntaxtree.BooleanType;
import minijava.syntaxtree.ClassDeclaration;
import minijava.syntaxtree.ClassExtendsDeclaration;
import minijava.syntaxtree.FormalParameter;
import minijava.syntaxtree.FormalParameterList;
import minijava.syntaxtree.FormalParameterRest;
import minijava.syntaxtree.Goal;
import minijava.syntaxtree.Identifier;
import minijava.syntaxtree.IntegerType;
import minijava.syntaxtree.MainClass;
import minijava.syntaxtree.MethodDeclaration;
import minijava.syntaxtree.Type;
import minijava.syntaxtree.TypeDeclaration;
import minijava.syntaxtree.VarDeclaration;
import minijava.visitor.GJDepthFirst;

/**
 * 初步建立符号表的visitor
 * 第一遍遍历语法树因为可能会继承后面声明的类，而该类无法通过Main.my_classes中通过名字获得，所以为方便第一次遍历直接忽略继承
 * @author GeniusLJR
 *
 */
public class BuildSymbolTableVisitor extends GJDepthFirst<MType, MType>{
   
	/**
	 * f0 -> MainClass()
	 * f1 -> ( TypeDeclaration() )*
	 * f2 -> <EOF>
	 */
	public MType visit(Goal n, MType argu) {
		MType _ret=null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		return _ret;
   	}
	
	/**
	 * f0 -> "class"
	 * f1 -> Identifier()
	 * f2 -> "{"
	 * f3 -> "public"
	 * f4 -> "static"
	 * f5 -> "void"
	 * f6 -> "main"
	 * f7 -> "("
	 * f8 -> "String"
	 * f9 -> "["
	 * f10 -> "]"
	 * f11 -> Identifier()
	 * f12 -> ")"
	 * f13 -> "{"
	 * f14 -> PrintStatement()
	 * f15 -> "}"
	 * f16 -> "}"
	 */
	public MType visit(MainClass n, MType argu) {
	   
	    MType _ret=null;
	    MClass mclass;
	    String class_name;
	    
	    //处理类定义的标识符Identifier()
        //获得名字
	    class_name = ((MIdentifier)n.f1.accept(this, argu)).getName();
	    
	    //将类插入MClasses
	    mclass = new MClass(class_name,(MClasses)argu);
	    ((MClasses)argu).insertClass(mclass);
	    
	    n.f11.accept(this, argu);
	    
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
     * f2 -> "{"
     * f3 -> ( VarDeclaration() )*
     * f4 -> ( MethodDeclaration() )*
     * f5 -> "}"
     */
    public MType visit(ClassDeclaration n, MType argu) {
    	
    	MType _ret=null;
        MClass mclass;
        String class_name;
      
        //处理类定义的标识符Identifier()
        //获得名字
        class_name = ((MIdentifier)n.f1.accept(this, argu)).getName();    
        
        //在符号表中插入该类，如果出错，打印出错信息
        mclass = new MClass(class_name,(MClasses)argu);
        if (!((MClasses)argu).insertClass(mclass))
        	Console.Redefination(n.f1.f0.beginLine, class_name);
        
        //向每个新声明的类直接加入一个名字为this的变量，其类型即为该类
        MType myself = new MIdentifier("this", mclass);
        ((MIdentifier)myself).setType(class_name);
        mclass.insertVar(myself);
        
        //变量声明
        n.f3.accept(this, mclass);
       
        //方法声明
        n.f4.accept(this, mclass);
        
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
        String class_name ;
        
        //处理类定义的标识符Identifier()
        //获得名字
        class_name = ((MIdentifier)n.f1.accept(this, argu)).getName();
        
        //在符号表中插入该类，如果出错，打印出错信息 
        mclass = new MClass(class_name,(MClasses)argu);
        if (!((MClasses)argu).insertClass(mclass))
        	Console.Redefination(n.f1.f0.beginLine, class_name);
        
        //向每个新声明的类直接加入一个名字为this的变量，其类型即为该类
        MType myself = new MIdentifier("this", mclass);
        ((MIdentifier)myself).setType(class_name);
        mclass.insertVar(myself);
	    
        //变量声明
	    n.f5.accept(this, mclass);
	    
	    //方法声明
	    n.f6.accept(this, mclass);
	    
	    return _ret;
	}
    
    /**
     * f0 -> Type()
     * f1 -> Identifier()
     * f2 -> ";"
     */
    public MType visit(VarDeclaration n, MType argu) {
    	//获得当前声明变量的类型，可能有四种MInteger,MBoolean,MArray,MIdentifer(表示自己定义的类)
    	MType _ret = n.f0.accept(this, argu);
    	
    	//将该变量的名字设置为f1获得的字符串
	    _ret.setName(((MIdentifier)n.f1.accept(this, argu)).getName());
	    
	    //将该变量加入argu的变量集合中，不成功则重定义错
	    if(!argu.insertVar(_ret))
	    	Console.Redefination(n.f1.f0.beginLine, _ret.getName());
	    return _ret;
	}
    
    /**
     * f0 -> "public"
     * f1 -> Type()
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( FormalParameterList() )?
     * f5 -> ")"
     * f6 -> "{"
     * f7 -> ( VarDeclaration() )*
     * f8 -> ( Statement() )*
     * f9 -> "return"
     * f10 -> Expression()
     * f11 -> ";"
     * f12 -> "}"
     */
    public MType visit(MethodDeclaration n, MType argu) {
    	//获得一个变量类型的字符串，可能有四种"int","boolean","int[]", "自己定义的类的名字"
     	String method_returntype = (n.f1.accept(this, argu)).getType();
     	
     	//获得方法的名字
        String method_name = ((MIdentifier)n.f2.accept(this, argu)).getName();
        
        //声明一个名字为method_name，返回值类型为method_returntype，作用域是argu的方法
        MType _ret = new MMethod(method_name, method_returntype, (MClass)argu);
        
        //将该方法加入到argu的方法集合中，不成功则报重定义错
        if (!((MClass)argu).insertMethod((MMethod)_ret))
        	Console.Redefination(n.f2.f0.beginLine, _ret.getName());
        
        //参数列表声明
        n.f4.accept(this, _ret);
        
        //方法内局部变量声明
        n.f7.accept(this, _ret);
        return _ret;
    }
    
    /**
     * f0 -> FormalParameter()
     * f1 -> ( FormalParameterRest() )*
     */
    public MType visit(FormalParameterList n, MType argu) {
    	MType _ret=null;
	    n.f0.accept(this, argu);
	    n.f1.accept(this, argu);
	    return _ret;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     */
    public MType visit(FormalParameter n, MType argu) {
    	//获得当前声明参数的类型，可能有四种MInteger,MBoolean,MArray,MIdentifer(表示自己定义的类)
    	MType _ret = n.f0.accept(this, argu);
	    
    	//将该参数的名字设置为f1获得的字符串
    	_ret.setName(((MIdentifier)n.f1.accept(this, argu)).getName());
		
    	 //将该变量加入argu的参数列表中，不成功则重定义错
    	if(!((MMethod)argu).insertParam(_ret))
			Console.Redefination(n.f1.f0.beginLine, _ret.getName());
	    return _ret;
	}
    
    /**
     * f0 -> ","
     * f1 -> FormalParameter()
     */
    public MType visit(FormalParameterRest n, MType argu) {
    	MType _ret=null;
    	n.f1.accept(this, argu);
    	return _ret;
	}
    
    
    /**
     * f0 -> ArrayType()
     *       | BooleanType()
     *       | IntegerType()
     *       | Identifier()
     */
    public MType visit(Type n, MType argu) {
    	MType _ret = n.f0.accept(this, argu);
        return _ret;
    }
    
    /**
     * f0 -> "int"
     * f1 -> "["
     * f2 -> "]"
     */
    public MType visit(ArrayType n, MType argu) {
    	//返回一个数组型变量
    	MType _ret = new MArray(argu);
    	return _ret;
    }
    
    /**
     * f0 -> "boolean"
     */
    public MType visit(BooleanType n, MType argu) {
    	//返回一个布尔型变量
    	MType _ret = new MBoolean(argu);
    	return _ret;
    }
    
    /**
     * f0 -> "int"
     */
    public MType visit(IntegerType n, MType argu) {
    	//返回一个整形变量
    	MType _ret= new MInteger(argu);
        return _ret;
    }
  
    /**
     * f0 -> <IDENTIFIER>
     */
    public MType visit(Identifier n, MType argu) {
    	//返回一个名字为f0的字符串的MIdentifier
    	MType _ret = new MIdentifier(n.f0.toString(), argu);
		return _ret;
    }
}
