package minijava.typecheck;

import minijava.symboltable.MArray;
import minijava.symboltable.MBoolean;
import minijava.symboltable.MClass;
import minijava.symboltable.MClasses;
import minijava.symboltable.MIdentifier;
import minijava.symboltable.MInteger;
import minijava.symboltable.MMethod;
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
import minijava.syntaxtree.TypeDeclaration;
import minijava.syntaxtree.WhileStatement;
import minijava.visitor.GJDepthFirst;
import Mainclass.Mainclass;


public class TypeCheckingVisitor extends GJDepthFirst<MType, MType>{
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
        class_name = n.f1.f0.tokenImage;  
        
        mclass = ((MClasses) Mainclass.my_classes).getClassByName(class_name);
       
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
        class_name = n.f1.f0.tokenImage;
        
        mclass = ((MClasses) Mainclass.my_classes).getClassByName(class_name);
       
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
    	
    	String method_name = n.f2.f0.tokenImage;
    	
    	MType _ret = ((MClass)argu).getMethodByName(method_name);

        n.f8.accept(this, _ret);

        MType returntype = n.f10.accept(this, _ret);
       
        //如果return 的类型与函数声明的返回值类型不同，则报错
        if (!Judge.isTypeMatched(_ret, returntype))
        	Console.InvalidAssignment(n.f9.beginLine, returntype.getType(), _ret.getType());  	
        
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
    	
    	String leftname = n.f0.f0.tokenImage;
    	
    	MType left = Judge.isVarDeclared(leftname, argu);
    	MType right = n.f2.accept(this, argu);

    	//如果等号两边类型不相同那么则报错
    	if (!Judge.isTypeMatched(left, right))
    		Console.InvalidAssignment(n.f1.beginLine, right.getType(), left.getType());
  	
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
    	
    	String leftname = n.f0.f0.tokenImage;
    	
    	//f1如果不是数组型则报错
    	MType left = Judge.isVarDeclared(leftname, argu);
    	if (!left.getType().equals(MArray.type))
    		Console.InvalidArray(n.f0.f0.beginLine, left.getType());
    	
    	//f2如果不是整型则报错
    	MType middle = n.f2.accept(this, argu);
    	if (!middle.getType().equals(MInteger.type))
    		Console.InvalidAssignment(n.f0.f0.beginLine, middle.getType(), MInteger.type);
    		
    	//f5如果不是整型则报错
    	MType right = n.f5.accept(this, argu);
      	if (!right.getType().equals(MInteger.type))
      		Console.InvalidAssignment(n.f0.f0.beginLine, right.getType(), MInteger.type);
      	
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
    	
    	MType ifstate = n.f2.accept(this, argu);
    	//f2对应的类型如果不是布尔型则报错
    	if (!ifstate.getType().equals(MBoolean.type)) {
    		Console.InvalidAssignment(n.f0.beginLine, ifstate.getType(), MBoolean.type);
    	}
    	
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

    	//f2对应的类型如果不是布尔型则报错
    	MType whilestate = n.f2.accept(this, argu);
    	if (!whilestate.getType().equals(MBoolean.type)) {
    		Console.InvalidAssignment(n.f0.beginLine, whilestate.getType(), MBoolean.type);
    	}
    	
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
    	MType _ret = n.f2.accept(this, argu);
    	
    	//输出语句的类型如果不是整形则报错
    	if (!_ret.getType().equals(MInteger.type))
    		Console.InvalidPrint(n.f1.beginLine, _ret.getType());

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
    	
    	MType left = n.f0.accept(this, argu);
    	MType right = n.f2.accept(this, argu);
    	//'&&'两边得类型如果有非布尔型，则报错
    	if (!left.getType().equals(MBoolean.type) || !right.getType().equals(MBoolean.type))
    		Console.InvalidExpression(n.f1.beginLine, "&&", left.getType(), right.getType());
    	
    	//返回一个布尔型
    	return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "<"
     * f2 -> PrimaryExpression()
     */
    public MType visit(CompareExpression n, MType argu) {
    	MType _ret = new MBoolean();
    	
    	MType left = n.f0.accept(this, argu);
    	MType right = n.f2.accept(this, argu);
    	//'<'两边的类型如果有非整数型，则报错
    	if (!left.getType().equals(MInteger.type) || !right.getType().equals(MInteger.type))
    		Console.InvalidExpression(n.f1.beginLine, "<", left.getType(), right.getType());
	    
    	//返回一个布尔型
  		return _ret;
	}

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "+"
     * f2 -> PrimaryExpression()
     */
    public MType visit(PlusExpression n, MType argu) {
    	MType _ret = new MInteger();
    	
    	MType left = n.f0.accept(this, argu);
    	MType right = n.f2.accept(this, argu);
    	//'+'两边的类型如果有非整数型，则报错
    	if (!left.getType().equals(MInteger.type) || !right.getType().equals(MInteger.type))
    		Console.InvalidExpression(n.f1.beginLine, "+", left.getType(), right.getType());
	    
    	//返回一个整形
    	return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "-"
     * f2 -> PrimaryExpression()
     */
    public MType visit(MinusExpression n, MType argu) {
    	MType _ret = new MInteger();
    	
    	MType left = n.f0.accept(this, argu);
    	MType right = n.f2.accept(this, argu);
    	//'-'两边的类型如果有非整数型，则报错
    	if (!left.getType().equals(MInteger.type) || !right.getType().equals(MInteger.type))
    		Console.InvalidExpression(n.f1.beginLine, "-", left.getType(), right.getType());
	    
    	//返回一个整形
      	return _ret;
   	}

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "*"
     * f2 -> PrimaryExpression()
     */
    public MType visit(TimesExpression n, MType argu) {
    	MType _ret = new MInteger();
    	
    	MType left = n.f0.accept(this, argu);
    	MType right = n.f2.accept(this, argu);
    	//'*'两边的类型如果有非整数型，则报错
    	if (!left.getType().equals(MInteger.type) || !right.getType().equals(MInteger.type))
    		Console.InvalidExpression(n.f1.beginLine, "*", left.getType(), right.getType());
	    
    	//返回一个整形
      	return _ret;
	}

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "["
     * f2 -> PrimaryExpression()
     * f3 -> "]"
     */
    public MType visit(ArrayLookup n, MType argu) {
    	MType _ret = new MInteger();
    	
    	//f0如果不是一个数组类型则报错
    	MType left = n.f0.accept(this, argu);
    	if (!left.getType().equals(MArray.type))
    		Console.InvalidArray(n.f1.beginLine, left.getType());
  		
    	//f2如果不是一个整数类型则报错
    	MType right = n.f2.accept(this, argu);
  		if (!right.getType().equals(MInteger.type))
  			Console.InvalidAssignment(n.f1.beginLine, right.getType(), MInteger.type);
  				
  		//返回一个整形
      	return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> "length"
     */
    public MType visit(ArrayLength n, MType argu) {
    	MType _ret = new MInteger();
    	
    	//如果f0对应的类型不是数组则报错
    	MType left = n.f0.accept(this, argu);
    	if (!left.getType().equals(MArray.type))
    		Console.InvalidArray(n.f1.beginLine, left.getType());

    	//返回一个整形
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
    	MType _ret;
    	
    	MType left = n.f0.accept(this, argu);
    	
    	//如果f0所表示的类型是整形、布尔型、数组型则直接报错
    	if (left.getType().equals(MInteger.type) || left.getType().equals(MBoolean.type) || left.getType().equals(MArray.type))
    		Console.InvalidType(n.f1.beginLine, left.getName());
    	
    	//如果f0所标示的变量的类型以及其继承的类中没有声明一个名字为f2对应字符串的方法，则报错
    	String method_name = n.f2.f0.tokenImage;
    	MClass temp = ((MClasses)Mainclass.my_classes).getClassByName(left.getType());
    	MType method = Judge.isMethodDeclared(method_name, temp);
    	if (method == null)
   			Console.UndefinedMethod(n.f1.beginLine, left.getType(), method_name);  		
    	
    	String type = method.getType();

    	//确定返回值类型
    	if (type == MInteger.type) _ret = new MInteger();
    	else if (type == MBoolean.type) _ret = new MBoolean();
    	else if (type == MArray.type) _ret = new MArray(argu);
    	else _ret = new MIdentifier(type, argu);
    	
    	//判断参数是否匹配
    	MType newargu;
    	if (argu.getName()=="mclasses") 
    		newargu = new MMethod();
    	else newargu = argu;
    	n.f4.accept(this, newargu);
    	
    	int size = ((MMethod)method).paramlist.size();
    	int currentsize = ((MMethod)newargu).current_param.size();
	   
    	String[] param1 = new String[size];
    	String[] param2 = new String[currentsize];
    	for (int i = 0; i < size; i++)
    		param1[i] = ((MMethod)method).paramlist.elementAt(i).getType();
    	for (int i = 0; i < currentsize; i++)
    		param2[i] = ((MMethod)newargu).current_param.elementAt(i).getType();
    	
    	if (size != currentsize)
    		Console.InvalidParamlist(n.f1.beginLine, param1, param2);
    	else {
    		boolean invalid = false;
OUT:   		for (int i = 0; i < size; i++) {
    			if (!param1[i].equals(param2[i])) {
    				MClass father = ((MClasses)Mainclass.my_classes).getClassByName(param2[i]);
    				while (father != null) {
    					if (father.getName().equals(param1[i]))
    						continue OUT;
    					else father = father.getFather();
    				}
    				invalid = true;
    				break;
    			}
    		}
    		if (invalid)
    			Console.InvalidParamlist(n.f1.beginLine, param1, param2);
    	}
    	((MMethod)newargu).current_param.removeAllElements();
    	return _ret;
    }

    /**
     * f0 -> Expression()
     * f1 -> ( ExpressionRest() )*
     */
    public MType visit(ExpressionList n, MType argu) {
    	MType _ret=null;
    	
    	MType param = n.f0.accept(this, argu);
    	//向argu所表示的方法的current_param中添加参数变量，为之后检查参数匹配
    	((MMethod)argu).current_param.addElement(param);
    	n.f1.accept(this, argu);
    	return _ret;
    }

    /**
     * f0 -> ","
     * f1 -> Expression()
     */
    public MType visit(ExpressionRest n, MType argu) {
    	MType _ret=null;
    	MType param = n.f1.accept(this, argu);
    	//向argu所表示的方法的current_param中添加参数变量，为之后检查参数匹配
    	((MMethod)argu).current_param.addElement(param);
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
		if (_ret.getType() != MInteger.type && _ret.getType() != MBoolean.type && _ret.getType() != MArray.type) {
			//这个primaryexpression不是整形、布尔型或数组型
			if (_ret.getType() != MClass.type) {
				//如果不是一个类型的名字，那么就应该是一个变量的名字
				if (Judge.isVarDeclared(_ret.getName(), argu) != null) {
					//在可能的作用域查找该名字的变量并返回，如果没有则返回null
					_ret = Judge.isVarDeclared(_ret.getName(), argu);
					return _ret;
				}
				//是一个类型的名字，返回表示这种类型的类
				int size = ((MClasses)Mainclass.my_classes).mclasses.size();
				for (int i = 0; i < size; i++) {
					if (((MClasses)Mainclass.my_classes).mclasses.elementAt(i).getName().equals(_ret.getType()))
						return _ret;
				}
			}
			else {
				((MIdentifier)_ret).setType(_ret.getName());
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
    	return _ret;
    }
    
    
    /**
     * f0 -> "this"
     */
    public MType visit(ThisExpression n, MType argu) {
    	//返回所在类类型的变量
    	MType _ret = Judge.isVarDeclared("this", argu);
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
    
    	//如果f3对应的表达式的类型不是整形则报类型不匹配错误
    	MType middle = n.f3.accept(this, argu);
    	if (!middle.getType().equals(MInteger.type))
    		Console.InvalidAssignment(n.f0.beginLine, middle.getType(), MInteger.type);
    	
    	return _ret;
   	}

    /**
     * f0 -> "new"
     * f1 -> Identifier()
     * f2 -> "("
     * f3 -> ")"
     */
    public MType visit(AllocationExpression n, MType argu) {
    	//返回一个名字为f1对应字符串的MIdentifier，并将其类型设置为class，便于类型检查
    	MType _ret = (MIdentifier) n.f1.accept(this, argu);
    	((MIdentifier)_ret).setType(MClass.type);
      	return _ret;
    }

    /**
     * f0 -> "!"
     * f1 -> Expression()
     */
    public MType visit(NotExpression n, MType argu) {
    	MType _ret = n.f1.accept(this, argu);
    	
    	//如果!后面的表达式的类型不是布尔型则报类型不匹配错误
    	if (!_ret.getType().equals(MBoolean.type))
    		Console.InvalidExpression(n.f0.beginLine, "!", _ret.getType());
    	
    	//返回一个布尔类型
    	return new MBoolean();
    }

    /**
     * f0 -> "("
     * f1 -> Expression()
     * f2 -> ")"
     */
    public MType visit(BracketExpression n, MType argu) {
    	//类型与括号内表达式相同，所以直接返回括号内表达式
    	MType _ret = n.f1.accept(this, argu);
    	return _ret;
    }
}
