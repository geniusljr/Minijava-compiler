package minijava.typecheck;

import minijava.symboltable.MArray;
import minijava.symboltable.MBoolean;
import minijava.symboltable.MClass;
import minijava.symboltable.MClasses;
import minijava.symboltable.MIdentifier;
import minijava.symboltable.MInteger;
import minijava.symboltable.MType;
import minijava.syntaxtree.AllocationExpression;
import minijava.syntaxtree.AndExpression;
import minijava.syntaxtree.ArrayAllocationExpression;
import minijava.syntaxtree.ArrayAssignmentStatement;
import minijava.syntaxtree.ArrayLength;
import minijava.syntaxtree.ArrayLookup;
import minijava.syntaxtree.AssignmentStatement;
import minijava.syntaxtree.Block;
import minijava.syntaxtree.BracketExpression;
import minijava.syntaxtree.ClassDeclaration;
import minijava.syntaxtree.ClassExtendsDeclaration;
import minijava.syntaxtree.CompareExpression;
import minijava.syntaxtree.Expression;
import minijava.syntaxtree.ExpressionList;
import minijava.syntaxtree.ExpressionRest;
import minijava.syntaxtree.FalseLiteral;
import minijava.syntaxtree.FormalParameter;
import minijava.syntaxtree.FormalParameterList;
import minijava.syntaxtree.FormalParameterRest;
import minijava.syntaxtree.Goal;
import minijava.syntaxtree.Identifier;
import minijava.syntaxtree.IfStatement;
import minijava.syntaxtree.IntegerLiteral;
import minijava.syntaxtree.MainClass;
import minijava.syntaxtree.MessageSend;
import minijava.syntaxtree.MethodDeclaration;
import minijava.syntaxtree.MinusExpression;
import minijava.syntaxtree.NotExpression;
import minijava.syntaxtree.PlusExpression;
import minijava.syntaxtree.PrimaryExpression;
import minijava.syntaxtree.PrintStatement;
import minijava.syntaxtree.Statement;
import minijava.syntaxtree.ThisExpression;
import minijava.syntaxtree.TimesExpression;
import minijava.syntaxtree.TrueLiteral;
import minijava.syntaxtree.Type;
import minijava.syntaxtree.TypeDeclaration;
import minijava.syntaxtree.VarDeclaration;
import minijava.syntaxtree.WhileStatement;
import minijava.visitor.GJDepthFirst;
import Mainclass.Mainclass;

public class CheckUndefineVisitor extends GJDepthFirst<MType, MType>{
	
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
	    
	    n.f14.accept(this, argu);
	    
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
        
        mclass = ((MClasses) Mainclass.my_classes).getClassByName(class_name);
        
        n.f3.accept(this, mclass);
       
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
        MClass mclass;
        String class_name;
      
        //处理类定义的标识符Identifier()
        //获得名字
        class_name = ((MIdentifier)n.f1.accept(this, argu)).getName();    
        
        mclass = ((MClasses) Mainclass.my_classes).getClassByName(class_name);
        
        n.f5.accept(this, mclass);
       
        n.f6.accept(this, mclass);
        
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
    	
    	//如果f1对应的变量类型未被声明过，则报未定义错
    	MType returntype = n.f1.accept(this, argu);
    	if (returntype != null) {
    		if (((MClasses) Mainclass.my_classes).getClassByName(returntype.getName()) == null)
    			Console.UndefinedClass(n.f0.beginLine, returntype.getName());
    	}
    	
    	
    	String method_name = ((MIdentifier)n.f2.accept(this, argu)).getName();
    	
    	MType _ret = ((MClass)argu).getMethodByName(method_name);
    	
    	n.f4.accept(this, _ret);
    	
    	n.f7.accept(this, _ret);
    	
        n.f8.accept(this, _ret);
        
        n.f10.accept(this, _ret);
        
        return _ret;
    }
    
    
    /**
     * f0 -> Type()
     * f1 -> Identifier()
     * f2 -> ";"
     */
    public MType visit(VarDeclaration n, MType argu) {
    	//如果f0对应的变量类型未被声明过，则报未定义错
    	MType _ret = n.f0.accept(this, argu);
    	if (_ret != null) {
    		if (((MClasses) Mainclass.my_classes).getClassByName(_ret.getName()) == null)
    			Console.UndefinedClass(n.f1.f0.beginLine, _ret.getName());
    	}
    	
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
    	//如果f0对应的参数类型未被声明过，则报未定义错
    	MType _ret = n.f0.accept(this, argu);
    	if (_ret != null) {
    		if (((MClasses) Mainclass.my_classes).getClassByName(_ret.getName()) == null)
    			Console.UndefinedClass(n.f1.f0.beginLine, _ret.getName());
    	}
    	
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
    	//如果f0是ArrayType,BooleanType,IntegerType则_ret是null
    	MType _ret = n.f0.accept(this, argu);
        return _ret;
    }
    
    /**
     * f0 -> Block()
     *       | AssignmentStatement()
     *       | ArrayAssignmentStatement()
     *       | IfStatement()
     *       | WhileStatement()
     *       | PrintStatement()
     */
    public MType visit(Statement n, MType argu) {
    	MType _ret=null;
    	n.f0.accept(this, argu);
    	return _ret;
	}
   	   
   	/**
   	 * f0 -> "{"
   	 * f1 -> ( Statement() )*
   	 * f2 -> "}"
   	 */
    public MType visit(Block n, MType argu) {
    	MType _ret=null;
    	n.f1.accept(this, argu);
    	return _ret;
    }

   	/**
   	 * f0 -> Identifier()
   	 * f1 -> "="
   	 * f2 -> Expression()
   	 * f3 -> ";"
   	 */
    public MType visit(AssignmentStatement n, MType argu) {
    	
    	MType _ret=null;
    	
    	//左边f0这个变量如果没有在该方法或该方法所在类或其继承的所有的类中声明则报未定义错
    	String leftname = n.f0.accept(this, argu).getName();
    	MType left = Judge.isVarDeclared(leftname, argu);
    	if (left == null)
    		Console.UndefinedVariable(n.f0.f0.beginLine, leftname);
    	
  		n.f2.accept(this, argu);
  		
  		return _ret;
    }	

    /**
     * f0 -> Identifier()
     * f1 -> "["
     * f2 -> Expression()
     * f3 -> "]"
     * f4 -> "="
     * f5 -> Expression()
     * f6 -> ";"
     */
    public MType visit(ArrayAssignmentStatement n, MType argu) {
    	MType _ret=null;
    	
    	//左边f0这个变量如果没有在该方法或该方法所在类或其继承的所有的类中声明则报未定义错
    	String leftname = n.f0.accept(this, argu).getName();
    	MType left = Judge.isVarDeclared(leftname, argu);
    	if (left == null)
    		Console.UndefinedVariable(n.f0.f0.beginLine, leftname);
	      	
      	n.f2.accept(this, argu);

      	n.f5.accept(this, argu);	

      	return _ret;
    }

    /**
     * f0 -> "if"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> Statement()
     * f5 -> "else"
     * f6 -> Statement()
     */
    public MType visit(IfStatement n, MType argu) {
    	MType _ret=null;
    	
    	n.f2.accept(this, argu);
    	
    	n.f4.accept(this, argu);
    	
    	n.f6.accept(this, argu);
    	
    	return _ret;
   	}

    /**
     * f0 -> "while"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> Statement()
     */
    public MType visit(WhileStatement n, MType argu) {
    	MType _ret=null;

    	n.f2.accept(this, argu);

    	n.f4.accept(this, argu);
    	
    	return _ret;
    }

   	/**
   	 * f0 -> "System.out.println"
   	 * f1 -> "("
   	 * f2 -> Expression()
   	 * f3 -> ")"
   	 * f4 -> ";"
   	 */
    public MType visit(PrintStatement n, MType argu) {
    	MType _ret=null;

    	n.f2.accept(this, argu);

    	return _ret;
	}

   	/**
   	 * f0 -> AndExpression()
   	 *       | CompareExpression()
   	 *       | PlusExpression()
   	 *       | MinusExpression()
   	 *       | TimesExpression()
   	 *       | ArrayLookup()
   	 *       | ArrayLength()
   	 *       | MessageSend()
   	 *       | PrimaryExpression()
   	 */
    public MType visit(Expression n, MType argu) {
    	MType _ret = n.f0.accept(this, argu);
    	return _ret;
   	}

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "&&"
     * f2 -> PrimaryExpression()
     */
    public MType visit(AndExpression n, MType argu) {
    	MType _ret = new MBoolean();
    	n.f0.accept(this, argu);
    	n.f1.accept(this, argu);
    	n.f2.accept(this, argu);
    	return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "<"
     * f2 -> PrimaryExpression()
     */
    public MType visit(CompareExpression n, MType argu) {
    	MType _ret = new MBoolean();
    	n.f0.accept(this, argu);
    	n.f2.accept(this, argu);
    	return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "+"
     * f2 -> PrimaryExpression()
     */
    public MType visit(PlusExpression n, MType argu) {
    	MType _ret = new MInteger();
    	n.f0.accept(this, argu);
    	n.f2.accept(this, argu);
    	return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "-"
     * f2 -> PrimaryExpression()
     */
    public MType visit(MinusExpression n, MType argu) {
    	MType _ret = new MInteger();
    	n.f0.accept(this, argu);
    	n.f2.accept(this, argu);
    	return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "*"
     * f2 -> PrimaryExpression()
     */
    public MType visit(TimesExpression n, MType argu) {
    	MType _ret =  new MInteger();
    	n.f0.accept(this, argu);
    	n.f2.accept(this, argu);
    	return _ret;
    }

   	/**
   	 * f0 -> PrimaryExpression()
   	 * f1 -> "["
   	 * f2 -> PrimaryExpression()
   	 * f3 -> "]"
   	 */
    public MType visit(ArrayLookup n, MType argu) {
    	MType _ret=null;
    	n.f0.accept(this, argu);
    	n.f2.accept(this, argu);
    	return _ret;
    }

   	/**
   	 * f0 -> PrimaryExpression()
   	 * f1 -> "."
   	 * f2 -> "length"
   	 */
    public MType visit(ArrayLength n, MType argu) {
    	MType _ret=null;
    	n.f0.accept(this, argu);
    	return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( ExpressionList() )?
     * f5 -> ")"
     */
    public MType visit(MessageSend n, MType argu) {
    	MType _ret = new MInteger();
    	n.f0.accept(this, argu);
    	n.f2.accept(this, argu);
    	n.f4.accept(this, argu);
    	return _ret;
    }

    /**
     * f0 -> Expression()
     * f1 -> ( ExpressionRest() )*
     */
    public MType visit(ExpressionList n, MType argu) {
    	MType _ret=null;
    	n.f0.accept(this, argu);
    	n.f1.accept(this, argu);
    	return _ret;
    }

    /**
     * f0 -> ","
     * f1 -> Expression()
     */
    public MType visit(ExpressionRest n, MType argu) {
    	MType _ret=null;
    	n.f1.accept(this, argu);
    	return _ret;
    }
	   
    /**
     * f0 -> IntegerLiteral()
     *       | TrueLiteral()
     *       | FalseLiteral()
     *       | Identifier()
     *       | ThisExpression()
     *       | ArrayAllocationExpression()
     *       | AllocationExpression()
     *       | NotExpression()
     *       | BracketExpression()
     */
	public MType visit(PrimaryExpression n, MType argu) {
		MType _ret = n.f0.accept(this, argu);
		if (_ret.getType() != MClass.type && _ret.getType() != MInteger.type && _ret.getType() != MBoolean.type && _ret.getType() != MArray.type) {
			//如果这个primaryexpression对应的不是一个类的名字，也不是int,boolean,array
			//那么就是一个变量名字，如果这个变量未被声明过，则报未定义错
			if (Judge.isVarDeclared(_ret.getName(), argu) == null){
				Console.UndefinedVariable(((MIdentifier)_ret).getLineNum(), _ret.getName());
			}
		}
		return _ret;
	}	

	/**
	 * f0 -> <INTEGER_LITERAL>
	 */
	public MType visit(IntegerLiteral n, MType argu) {
		MType _ret = new MInteger();
		return _ret;
   	}

	/**
	 * f0 -> "true"
	 */
	public MType visit(TrueLiteral n, MType argu) {
		MType _ret = new MBoolean();
		return _ret;
	}

	/**
	 * f0 -> "false"
	 */
	public MType visit(FalseLiteral n, MType argu) {
		MType _ret = new MBoolean();
		return _ret;
	}
    
	/**
     * f0 -> <IDENTIFIER>
     */
    public MType visit(Identifier n, MType argu) {
    	MType _ret = new MIdentifier(n.f0.toString(), argu);
    	//设置行号
    	((MIdentifier) _ret).setLineNum(n.f0.beginLine);
    	return _ret;
    }
    
    /**
     * f0 -> "this"
     */
    public MType visit(ThisExpression n, MType argu) {
    	MType _ret = argu.getField();
    	return _ret;
   	}

    /**
     * f0 -> "new"
     * f1 -> "int"
     * f2 -> "["
     * f3 -> Expression()
     * f4 -> "]"
     */
    public MType visit(ArrayAllocationExpression n, MType argu) {
    	MType _ret = new MArray(argu);
    	
    	n.f3.accept(this, argu);
    	
    	return _ret;
   	}

    /**
     * f0 -> "new"
     * f1 -> Identifier()
     * f2 -> "("
     * f3 -> ")"
     */
    public MType visit(AllocationExpression n, MType argu) {
    	
    	MType _ret = (MIdentifier) n.f1.accept(this, argu);
    	
    	//如果没有声明过名字为f1对应字符串的名字则报未定义错
    	if (((MClasses) Mainclass.my_classes).getClassByName(_ret.getName()) == null)
    		Console.UndefinedClass(n.f1.f0.beginLine, _ret.getName());
    	((MIdentifier)_ret).setType("class");
    	
      	return _ret;
    }

    /**
     * f0 -> "!"
     * f1 -> Expression()
     */
    public MType visit(NotExpression n, MType argu) {
    	MType _ret = n.f1.accept(this, argu);
    	return _ret;
    }

    /**
     * f0 -> "("
     * f1 -> Expression()
     * f2 -> ")"
     */
    public MType visit(BracketExpression n, MType argu) {
    	MType _ret = n.f1.accept(this, argu);
    	return _ret;
    }
    

}
