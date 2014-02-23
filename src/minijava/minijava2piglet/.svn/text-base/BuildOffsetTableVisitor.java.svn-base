package minijava.minijava2piglet;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Stack;

import minijava.symboltable.*;
import minijava.syntaxtree.ClassDeclaration;
import minijava.syntaxtree.ClassExtendsDeclaration;
import minijava.syntaxtree.Goal;
import minijava.syntaxtree.MainClass;
import minijava.syntaxtree.MethodDeclaration;

import minijava.syntaxtree.TypeDeclaration;
import minijava.visitor.GJDepthFirst;
import Mainclass.Mainclass;

public class BuildOffsetTableVisitor extends GJDepthFirst<MType,MType>  {
	
	   //
	   // User-generated visitor methods below
	   //

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
	      
	      String class_name = n.f1.f0.toString();
	      MClass mclass = ((MClasses)Mainclass.my_classes).getClassByName(class_name);
	      
	      //生成method_table，存储所有成员方法地址
	      int method_num = mclass.methods.size();
	      for (int i = 0; i < method_num; i++) {
	    	  mclass.method_table.put(mclass.getName()+"_"+mclass.methods.elementAt(i).getName(), i);
	      }
	      //生成variable_table，存储所有成员变量地址，表的第一位存储method_table的起始地址
	      int variable_num = mclass.vars.size();
	      for (int i = 1; i < variable_num; i++) {
	    	  mclass.variable_table.put(mclass.getName()+"_"+mclass.vars.elementAt(i).getName(), i);
	      }
	      
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
	      
	      String class_name = n.f1.f0.toString();
	      MClass mclass = ((MClasses)Mainclass.my_classes).getClassByName(class_name);
	      MClass father = mclass;
	      Stack<String> variable_stack = new Stack<String>();
	      
	      /**
	       * 用来辅助建立method_table的类，表示一个方法的方法名和其声明所在类
	       * @author GeniusLJR
	       *
	       */
	      class Mymethod {
	    	  public String method_name;
	    	  public String class_name;
	    	  Mymethod(String name1, String name2){
	    		  method_name = name1;
	    		  class_name = name2;
	    	  }
	    	  /**
	    	   * 重写equals函数，判断两个方法同名只需要比较method_name部分
	    	   */
	    	  public boolean equals(Object other) {
	    		  return (method_name == ((Mymethod)other).method_name)?true:false;
	    	  }
	    	  /**
	    	   * 重写hashCode函数，判断两个方法同名只需要比较method_name部分
	    	   */
	    	  public int hashCode() {
	    		  return method_name.hashCode();
	    	  }
	      }
	      Stack<Mymethod> method_stack = new Stack<Mymethod>();
	      
	      //遍历所继承的类
	      while (father != null) {
	    	  int temp_var_size = father.vars.size();
	    	  //将自己至祖先所声明的成员变量压栈，为了使variable_table从前到后依次是祖先到自己的变量
	    	  for (int i = temp_var_size-1; i>0; i--){
	    		  variable_stack.push(father.getName()+"_"+father.vars.elementAt(i).getName());  
	    	  }
	    	  //将自己至祖先所声明的成员方法压栈，为了使method_table从前到后依次是祖先到自己的方法
	    	  int temp_method_size = father.methods.size();
	    	  for (int i = temp_method_size-1; i>=0; i--) {
	    		  method_stack.push(new Mymethod(father.methods.elementAt(i).getName(), father.getName()));
	    	  }
	    	  father = father.getFather();
	      }
	      //将祖先到自己的变量依次存入variable_table中
	      int offset = 1;
	      while (!variable_stack.empty()) {
	    	  mclass.variable_table.put(variable_stack.pop(), offset++);
	      }
	      offset = 0;
	      //将祖先中有和自己同名的函数覆盖掉
	      Hashtable<Mymethod, Integer> temp_method_table = new Hashtable<Mymethod, Integer>();
	      while (!method_stack.empty()) {
	    	  Mymethod temp = method_stack.pop();
	    	  if (temp_method_table.containsKey(temp)) {
	    		  //有与自己同名(method_name相同，不考虑class_name)的函数
	    		  int value = temp_method_table.get(temp);
	    		  temp_method_table.remove(temp);
	    		  temp_method_table.put(temp, value);
	    	  }
	    	  else {
	    		  temp_method_table.put(temp, offset++);
	    	  }
	      }
	      
	      //将从祖先至自己的所有方法名加入method_table中
	      Iterator<Mymethod> it = temp_method_table.keySet().iterator();
	      while (it.hasNext()){
	    	  Mymethod temp = (Mymethod)it.next();
	    	  mclass.method_table.put(temp.class_name+"_"+temp.method_name, temp_method_table.get(temp));
	      }	      
	      
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
	      MType _ret=null;
	      
	      MMethod mmethod = (MMethod) ((MClass)argu).getMethodByName(n.f2.f0.toString());
	      
	      //为每个方法建立参数列表，存储每个参数相对起始位置的偏移
	      int param_size = mmethod.paramlist.size();
	      mmethod.param_table.put("this", 0);
	      for (int i = 0; i < param_size; i++) {
	    	  int offset = i+1;
	    	  //如果多余20个参数，则分配寄存器需要多偏移一位
	    	  if (param_size > 19) {
	    		  if (offset >= 19) offset++;
	    	  }
	    	  mmethod.param_table.put(mmethod.paramlist.elementAt(i).getName(), offset);
	      }
	      return _ret;	      
	   }

}
