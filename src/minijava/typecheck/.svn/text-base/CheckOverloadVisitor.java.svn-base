package minijava.typecheck;

import Mainclass.Mainclass;
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
import minijava.syntaxtree.MethodDeclaration;
import minijava.syntaxtree.Type;
import minijava.syntaxtree.TypeDeclaration;
import minijava.syntaxtree.VarDeclaration;
import minijava.visitor.GJDepthFirst;
public class CheckOverloadVisitor extends GJDepthFirst<MType, MType>{
   
	/**
	 * f0 -> MainClass()
	 * f1 -> ( TypeDeclaration() )*
	 * f2 -> <EOF>
	 */
	public MType visit(Goal n, MType argu) {
		
		MType _ret=null;
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
     * f2 -> "{"
     * f3 -> ( VarDeclaration() )*
     * f4 -> ( MethodDeclaration() )*
     * f5 -> "}"
     */
    public MType visit(ClassDeclaration n, MType argu) {
    	
    	MType _ret=null;

    	//在Main.my_classes中f1对应的名字的类
        String class_name = ((MIdentifier)n.f1.accept(this, argu)).getName();    
        MClass mclass = ((MClasses)Mainclass.my_classes).getClassByName(class_name);
        
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
    	
    	//在Main.my_classes中f1对应的名字的类
        String class_name =((MIdentifier)n.f1.accept(this, argu)).getName();
        MClass mclass = ((MClasses)Mainclass.my_classes).getClassByName(class_name);
	    
	    n.f5.accept(this, mclass);
	    n.f6.accept(this, mclass);

	    return _ret;
	}
    
    /**
     * f0 -> Type()
     * f1 -> Identifier()
     * f2 -> ";"
     */
    public MType visit(VarDeclaration n, MType argu) {
    	MType _ret = null;
    	
    	String vartype = n.f0.accept(this, argu).getType();
    	
    	String varname = ((MIdentifier)n.f1.accept(this, argu)).getName();
    	
    	//检查该类继承的所有类中是否有跟当前正在声明的变量同名且不同类型的变量
    	//如果有则报”不合法重载“错误
    	MClass father = this.isVarDeclaredInFathers(varname, argu);
    	if (father != null && !father.getVarByName(varname).getType().equals(vartype))
    		Console.InvalidOverload(n.f2.beginLine, argu.getName(), father.getName(), varname);
    	
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
    	
    	MType _ret = null;
    	
     	String method_returntype = (n.f1.accept(this, argu)).getType();
        String methodname = ((MIdentifier)n.f2.accept(this, argu)).getName();
        
        //检查该类继承的所有类中是否有跟当前正在声明的方法同名且不同类型或不同参数列表的方法
    	//如果有则报”不合法重载“错误
        
        //在该类继承的所有类中获得拥有与methodname相同名字的方法的类，没有则是null
        MClass father = this.isMethodDeclaredInFathers(methodname, argu);
        if (father != null) {
        	//得到的方法的返回值类型与当前正在声明的方法不同
        	if (!father.getMethodByName(methodname).getType().equals(method_returntype))
        		Console.InvalidOverload(n.f3.beginLine, argu.getName(), father.getName(), methodname);
        	else {
        		//得到一个参数列表与f4相同的方法
        		MMethod method = new MMethod();
        		n.f4.accept(this, method);
        		
        		//将方法的参数列表的类型名字存储到两个字符串数组中进行比较
        		int size = method.paramlist.size();
            	int currentsize = ((MMethod)father.getMethodByName(methodname)).paramlist.size();
        	   
            	String[] param1 = new String[size];
            	String[] param2 = new String[currentsize];
            	
            	for (int i = 0; i < size; i++)
            		param1[i] = method.paramlist.elementAt(i).getType();
            	for (int i = 0; i < currentsize; i++)
            		param2[i] = ((MMethod)father.getMethodByName(methodname)).paramlist.elementAt(i).getType();
            	
            	//如果参数个数不同直接报”不合法重载“错误
            	if (size != currentsize)
            		Console.InvalidOverload(n.f3.beginLine, argu.getName(), father.getName(), methodname);
            	//个数相同但对应的参数类型不同则报”不合法重载“错误
            	else {
            		boolean invalid = false;
            		for (int i = 0; i < size; i++) {
            			if (!param1[i].equals(param2[i])) {
            				invalid = true;
            				break;
            			}
            		}
            		if (invalid)
            			Console.InvalidOverload(n.f3.beginLine, argu.getName(), father.getName(), methodname);
            	}  
        	}
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
    	MType _ret = n.f0.accept(this, argu);
	    _ret.setName(((MIdentifier)n.f1.accept(this, argu)).getName());
		((MMethod)argu).insertParam(_ret);
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
    	MType _ret = new MArray(argu);
    	return _ret;
    }
    
    /**
     * f0 -> "boolean"
     */
    public MType visit(BooleanType n, MType argu) {
    	MType _ret = new MBoolean(argu);
    	return _ret;
    }
    
    /**
     * f0 -> "int"
     */
    public MType visit(IntegerType n, MType argu) {
    	MType _ret= new MInteger(argu);
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
     * 判断一个名字为varname的变量是否在argu所继承的所有类中声明过
     * @param varname 要判断的变量的名字
     * @param argu 
     * @return 如果存在则返回拥有该变量的类
     */
    private MClass isVarDeclaredInFathers(String varname, MType argu) {
    	if (argu.getType() != MClass.type)
    		return null;
    	
    	MClass father = ((MClass)argu).getFather();
    	MType var = null;
    	if (var == null) {
    		while (father != null) {
    			var = father.getVarByName(varname);
    			if (var != null)
    				return father;
    			father = father.getFather();
    		}
    	}
    	return null;
    }
    
    /**
     * 判断一个名字为methodname是否在argu所继承的所有类中声明过
     * @param varname 要判断的方法的名字
     * @param argu 
     * @return 如果存在则返回拥有该方法的类
     */
    private MClass isMethodDeclaredInFathers(String methodname, MType argu) {
    	if (argu.getType() != MClass.type)
    		return null;
    	MClass father = ((MClass)argu).getFather();
    	MType var = null;
    	if (var == null) {
    		while (father != null) {
    			var = father.getMethodByName(methodname);
    			if (var != null)
    				return father;
    			father = father.getFather();
    		}
    	}
    	return null;
    }
}
