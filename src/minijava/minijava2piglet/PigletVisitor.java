package minijava.minijava2piglet;

import java.util.Iterator;

import minijava.symboltable.*;
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
import minijava.syntaxtree.VarDeclaration;
import minijava.syntaxtree.WhileStatement;
import minijava.typecheck.Judge;
import minijava.visitor.GJDepthFirst;
import Mainclass.Mainclass;


public class PigletVisitor extends GJDepthFirst<MType,MType>  {
	
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
		   Translate.Print("MAIN");
		   n.f14.accept(this, argu);
		   Translate.Print("\nEND");
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
		   MClass mclass = ((MClasses)Mainclass.my_classes).getClassByName(n.f1.f0.toString());
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
		   MClass mclass = ((MClasses)Mainclass.my_classes).getClassByName(n.f1.f0.toString());
		   n.f6.accept(this, mclass);
		   return _ret;
	   }

	   /**
	    * f0 -> Type()
	    * f1 -> Identifier()
	    * f2 -> ";"
	    */
	   public MType visit(VarDeclaration n, MType argu) {
		  MType _ret=null;
		  //为方法局部变量分配寄存器
		  ((MMethod)argu).variable_table.put(n.f1.f0.tokenImage, Translate.temp_count++);
		  //初始化局部变量
	      Translate.Print("\nMOVE "+"TEMP "+((MMethod)argu).variable_table.get(n.f1.f0.toString())+ " 0");
	      n.f2.accept(this, argu);
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
	      String method_name = n.f2.f0.toString();
	      MType mmethod = ((MClass)argu).getMethodByName(method_name);
	      
	      //如果方法多于20个参数则输出参数个数为20
	      int realparamsize = ((MMethod)mmethod).param_table.size();
	      int param_size = realparamsize > 20 ? 20 : realparamsize;
	      Translate.Print("\n\n"+argu.getName()+"_"+method_name+" "+"["+param_size+"]");
	      
	      Translate.Print("\nBEGIN");
	      
	      //多余20个参数，要先把每个参数从内存中读取出来
	      //20以后的参数都存放在一片连续的地址空间，这片空间的起始地址存放在TEMP 19中
	      if (realparamsize > 20) {
		      Translate.temp_count += realparamsize - 19;
		      for (int i = 0; i <= realparamsize-20; i++) {
		    	  Translate.Print("\nHLOAD TEMP "+(20+i)+" TEMP 19 "+(i*4));
		      }
	      }
	      
	      n.f7.accept(this, mmethod);
	      n.f8.accept(this, mmethod);
	      
	      int return_temp = n.f10.accept(this, mmethod).returntemp;
	      Translate.Print("\nRETURN TEMP "+return_temp);
	      Translate.Print("\nEND");
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
	      String variable_name = n.f0.f0.toString();
	      
	      int f2_temp = n.f2.accept(this, argu).returntemp;
	      
	      //给左边变量赋值
	      if (((MMethod)argu).param_table.containsKey(variable_name))
	    	  //左边是方法参数
	    	  Translate.Print("\nMOVE TEMP "+((MMethod)argu).param_table.get(variable_name)+" TEMP "+f2_temp);
	      else if (((MMethod)argu).variable_table.containsKey(variable_name))
	    	  //左边是局部变量
	    	  Translate.Print("\nMOVE TEMP "+((MMethod)argu).variable_table.get(variable_name)+" TEMP "+f2_temp);
	      else {
	    	  //左边是类的成员变量
	    	  MClass mclass = (MClass) argu.getField();
	    	  while (mclass != null) {
	    		  if (mclass.getVarByName(variable_name) != null)
	    			  break;
	    		  else mclass = mclass.getFather();
	    	  }
	    	  variable_name = mclass.getName()+"_"+variable_name;
	    	  Translate.Print("\nHSTORE TEMP 0 "+mclass.variable_table.get(variable_name)*4+" TEMP "+f2_temp);
	      }  
	      	      
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
	      	      
	      //先获得数组的起始位置
	      int f0_temp = n.f0.accept(this, argu).returntemp;
	      int array_temp = Translate.temp_count++;
	      Translate.Print("\nMOVE TEMP "+array_temp+" TEMP "+f0_temp);
	      
	      //检查空指针
	      this.NullCheck(array_temp);
	      
	      int f2_temp = n.f2.accept(this, argu).returntemp;
	      //得到数组指针相对数组起始地址的偏移
	      int array_index_temp = Translate.temp_count++;
	      Translate.Print("\nMOVE TEMP "+array_index_temp+" TEMP "+f2_temp);
	      
	      
	      //检查数组下标越界
	      int array_length_temp = Translate.temp_count++;
	      Translate.Print("\nHLOAD TEMP "+array_length_temp+" TEMP "+array_temp+" 0");
	      int error_label = Translate.label_count++;
	      int judge_temp = Translate.temp_count++;
	      Translate.Print("\nMOVE TEMP "+judge_temp+" LT TEMP "+array_index_temp+" TEMP "+array_length_temp);
	      Translate.Print("\nCJUMP TEMP "+judge_temp+" L"+error_label);
	      int noerror_label = Translate.label_count++;
	      Translate.Print("\nJUMP L"+noerror_label);
	      Translate.Print("\nL"+error_label+" ERROR");
	      Translate.Print("\nL"+noerror_label+" NOOP");
	      
	      //得到所需要的数组元素
	      int one_temp = Translate.temp_count++;
	      Translate.Print("\nMOVE TEMP "+one_temp+" 1");
	      Translate.Print("\nMOVE TEMP "+array_index_temp+" PLUS TEMP "+one_temp+" TEMP "+array_index_temp);
	      int four_temp = Translate.temp_count++;
	      Translate.Print("\nMOVE TEMP "+four_temp+" 4");
	      Translate.Print("\nMOVE TEMP "+array_index_temp+" TIMES TEMP "+four_temp+" TEMP "+array_index_temp);
	      Translate.Print("\nMOVE TEMP "+array_temp+" PLUS TEMP "+array_temp+" TEMP "+array_index_temp);
	      
	      //给该元素赋值
	      int f5_temp = n.f5.accept(this, argu).returntemp;
	      Translate.Print("\nHSTORE "+"TEMP "+array_temp+" "+"0 TEMP "+f5_temp);
	      
	      
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
	      
	      //将判断条件的值存入寄存器
	      int f2_temp = n.f2.accept(this, argu).returntemp;
	      int if_temp = Translate.temp_count++; 
	      Translate.Print("\nMOVE TEMP "+if_temp+" TEMP "+f2_temp);
	      
	      int falselabel = Translate.label_count++;
	      int returnlabel = Translate.label_count++;
	      //如果判断条件不成立则跳转到falselabel处
	      Translate.Print("\nCJUMP TEMP "+if_temp+" L"+falselabel);
	      n.f4.accept(this, argu);
	      //执行statement后跳转到结束语句
	      Translate.Print("\nJUMP L"+returnlabel);
	      Translate.Print("\nL"+falselabel+" NOOP");
	      n.f6.accept(this, argu);
	      Translate.Print("\nL"+returnlabel+" NOOP");
	      
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
	      int whilelabel = Translate.label_count++;
	      int returnlabel = Translate.label_count++;
	      Translate.Print("\nL"+whilelabel+" NOOP");
	      
	      
	      //将判断条件的值存入寄存器
	      int f2_temp = n.f2.accept(this, argu).returntemp;
	      int while_temp = Translate.temp_count++;
	      Translate.Print("\nMOVE TEMP "+while_temp+" TEMP "+f2_temp);
	      
	      //如果判断条件不成立则跳转到returnlabel处
	      Translate.Print("\nCJUMP TEMP "+while_temp+" L"+returnlabel);
	      n.f4.accept(this, argu);
	      //继续进行判断
	      Translate.Print("\nJUMP L"+whilelabel);
	      Translate.Print("\nL"+returnlabel+" NOOP");
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
	      int f2_temp = n.f2.accept(this, argu).returntemp;
	      Translate.Print("\nPRINT TEMP "+f2_temp);
	      
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
	      int end_label = Translate.label_count++;
	      int f0_temp = n.f0.accept(this, argu).returntemp;
	      int return_temp = Translate.temp_count++;
	      Translate.Print("\nMOVE TEMP "+return_temp+" TEMP "+f0_temp);
	      Translate.Print("\nCJUMP TEMP "+return_temp+" L"+end_label);
	      int f2_temp = n.f2.accept(this, argu).returntemp;
	      Translate.Print("\nMOVE TEMP "+return_temp+" TEMP "+f2_temp);	      
	      Translate.Print("\nL"+end_label+" NOOP");
	      _ret.returntemp = return_temp;
	      return _ret;
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "<"
	    * f2 -> PrimaryExpression()
	    */
	   public MType visit(CompareExpression n, MType argu) {
	      MType _ret = new MBoolean();
	      int f0_temp = n.f0.accept(this, argu).returntemp;
	      int f2_temp = n.f2.accept(this, argu).returntemp;
	      int return_temp = Translate.temp_count++;
	      Translate.Print("\nMOVE TEMP "+return_temp+" LT TEMP "+f0_temp+" TEMP "+f2_temp);      
	      _ret.returntemp = return_temp;
	      return _ret;
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "+"
	    * f2 -> PrimaryExpression()
	    */
	   public MType visit(PlusExpression n, MType argu) {
	      MType _ret = new MInteger();
	      int f0_temp = n.f0.accept(this, argu).returntemp;
	      int f2_temp = n.f2.accept(this, argu).returntemp;
	      int return_temp = Translate.temp_count++;
	      Translate.Print("\nMOVE TEMP "+return_temp+" PLUS TEMP "+f0_temp+" TEMP "+f2_temp);      
	      _ret.returntemp = return_temp;
	      return _ret;
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "-"
	    * f2 -> PrimaryExpression()
	    */
	   public MType visit(MinusExpression n, MType argu) {
		   MType _ret = new MInteger();
		   int f0_temp = n.f0.accept(this, argu).returntemp;
		   int f2_temp = n.f2.accept(this, argu).returntemp;
		   int return_temp = Translate.temp_count++;
		   Translate.Print("\nMOVE TEMP "+return_temp+" MINUS TEMP "+f0_temp+" TEMP "+f2_temp);      
		   _ret.returntemp = return_temp;
		   return _ret;
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "*"
	    * f2 -> PrimaryExpression()
	    */
	   public MType visit(TimesExpression n, MType argu) {
	      MType _ret = new MInteger();
	      int f0_temp = n.f0.accept(this, argu).returntemp;
	      int f2_temp = n.f2.accept(this, argu).returntemp;
	      int return_temp = Translate.temp_count++;
	      Translate.Print("\nMOVE TEMP "+return_temp+" TIMES TEMP "+f0_temp+" TEMP "+f2_temp);      
	      _ret.returntemp = return_temp;
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
		   //得到数组变量起始位置
		   int f0_temp = n.f0.accept(this, argu).returntemp;
		   int array_temp = Translate.temp_count++;
		   Translate.Print("\nMOVE TEMP "+array_temp+" TEMP "+f0_temp);		   
		   
		   //检查空指针
		   this.NullCheck(array_temp);
		   //得到数组下标
		   int f2_temp = n.f2.accept(this, argu).returntemp;
		   int array_index = Translate.temp_count++;
		   Translate.Print("\nMOVE TEMP "+array_index+" TEMP "+f2_temp);
		  
		   //检查数组下标越界
		   int array_length = Translate.temp_count++;
		   Translate.Print("\nHLOAD TEMP "+array_length+" TEMP "+array_temp+" 0");
		   int errorlabel = Translate.label_count++;
		   int noerrorlabel = Translate.label_count++;
		   int judge_temp = Translate.temp_count++;
		   Translate.Print("\nMOVE TEMP "+judge_temp+" LT TEMP "+array_index+" TEMP "+array_length);
		   Translate.Print("\nCJUMP TEMP "+judge_temp+" L"+errorlabel);
		   Translate.Print("\nJUMP L"+noerrorlabel);
		   Translate.Print("\nL"+errorlabel+" ERROR");
		   Translate.Print("\nL"+noerrorlabel+" NOOP");
		   
		   //得到所需元素的起始位置
		   int one_temp = Translate.temp_count++;
		   Translate.Print("\nMOVE TEMP "+one_temp+" 1");
		   Translate.Print("\nMOVE TEMP "+array_index+" PLUS TEMP "+one_temp+" TEMP "+array_index);
		   int four_temp = Translate.temp_count++;
		   Translate.Print("\nMOVE TEMP "+four_temp+" 4");
		   Translate.Print("\nMOVE TEMP "+array_index+" TIMES TEMP "+four_temp+" TEMP "+array_index);
		   Translate.Print("\nMOVE TEMP "+array_temp+" PLUS TEMP "+array_temp+" TEMP "+array_index);
		   
		   //将所需元素起始位置对应的值存入寄存器并返回
		   Translate.Print("\nHLOAD TEMP "+array_temp+" TEMP "+array_temp+" 0");
		   
		   _ret.returntemp = array_temp;
		   return _ret;
	   }

	   /**
	    * f0 -> PrimaryExpression()
	    * f1 -> "."
	    * f2 -> "length"
	    */
	   public MType visit(ArrayLength n, MType argu) {
	      MType _ret = new MInteger();
	      
	      //得到数组变量起始位置
	      int f0_temp = n.f0.accept(this, argu).returntemp;
	      int array_temp = Translate.temp_count++;
	      Translate.Print("\nMOVE TEMP "+array_temp+" TEMP "+f0_temp);
	      
	      //检查空指针
	      this.NullCheck(array_temp);
	      
	      int array_length_temp = Translate.temp_count++;
	      //数组变量对应地址空间的第一个字节即为数组长度
	      Translate.Print("\nHLOAD TEMP "+array_length_temp+" TEMP "+array_temp+" 0");
	      _ret.returntemp = array_length_temp;
	      
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
		   
		   //得到自定义类型变量的起始位置
		   MType left = n.f0.accept(this, argu);
		   int f0_temp = left.returntemp;
		   int class_temp = Translate.temp_count++;
		   Translate.Print("\nMOVE "+"TEMP "+class_temp+" TEMP "+f0_temp);
		   MClass mclass = ((MClasses)Mainclass.my_classes).getClassByName(left.getType());
		   
		   //检查空指针
		   this.NullCheck(class_temp);
		   
		   MClass father = mclass;
		   while (father != null) {
			   if (father.getMethodByName(n.f2.f0.toString()) != null)
				   break;
			   father = father.getFather();
		   }
		   //处理参数
		   //如果参数个数大于20则需要特殊处理
		   MMethod method = (MMethod)(MMethod)father.getMethodByName(n.f2.f0.tokenImage);
		   realparamsize = method.param_table.size();
		   if (realparamsize > 20) {
			   cur_param_num++;
			   extra_param= Translate.temp_count++;
		   }
		   n.f4.accept(this, argu);
		   realparamsize = method.param_table.size();
		   
		   //找到对应方法的地址并返回
		   int method_offset = mclass.method_table.get(father.getName()+"_"+n.f2.f0.toString());
		   //得到method_table的起始位置
		   int method_target = Translate.temp_count++;
		   Translate.Print("\nHLOAD "+"TEMP "+method_target+" "+"TEMP "+class_temp+" 0");
		   Translate.Print("\nHLOAD "+"TEMP "+method_target+" "+"TEMP "+method_target+" "+method_offset*4);
		   int return_temp = Translate.temp_count++;
		   Translate.Print("\nMOVE TEMP "+return_temp+" CALL TEMP "+method_target);
		   
		   
		   //得到参数列表
		   Translate.Print(" (TEMP "+class_temp);
		   for (int i = 0; i < realparamsize-1; i++) {
			   if (realparamsize > 20 && i == 0) continue;
			   if (i >= 20) break;
			   Translate.Print(" TEMP "+param_temp[i]);
		   }
		   Translate.Print(")");
		   
		   MType _ret = new MIdentifier(father.getMethodByName(n.f2.f0.toString()).getType(), argu);
		   _ret.returntemp = return_temp;
		   return _ret;
	   }
	   
	   private int realparamsize = 0;
	   private int cur_param_num = 0;
	   private int extra_param = 0;
	   private int[] param_temp = new int[20];
	   
	   /**
	    * f0 -> Expression()
	    * f1 -> ( ExpressionRest() )*
	    */
	   public MType visit(ExpressionList n, MType argu) {
	      MType _ret=null;
	      int f0_temp = n.f0.accept(this, argu).returntemp;
	      param_temp[cur_param_num] = Translate.temp_count++;
	      Translate.Print("\nMOVE TEMP "+param_temp[cur_param_num]+" TEMP "+f0_temp);
	      //cur_param_num记录当前读到的参数个数
	      cur_param_num++;
	      n.f1.accept(this, argu);
	      //参数个书大于20
	      if (realparamsize > 20) {
	    	  param_temp[19] = extra_param;
	      }
	      //遍历参数列表节点结束后将当前读到参数个数清零
	      cur_param_num = 0;
	      return _ret;
	   }

	   /**
	    * f0 -> ","
	    * f1 -> Expression()
	    */
	   public MType visit(ExpressionRest n, MType argu) {
	      MType _ret=null;
	      //cur_param_num记录当前读到的参数个数
	      //如果参数个数大于20
	      if (cur_param_num == 19) {
	    	  //先为之后的所有参数申请内存
	    	  Translate.Print("\nMOVE TEMP "+extra_param+" HALLOCATE "+((realparamsize-19)*4));
	      }
	      int f1_temp = n.f1.accept(this, argu).returntemp;
	      if (cur_param_num >= 19) {
	    	  //将参数存储到对应的寄存器中
	    	  Translate.Print("\nHSTORE TEMP "+extra_param+" "+((cur_param_num-19)*4)+" TEMP "+f1_temp);
	      }
	      if (cur_param_num < 19) {
	    	  param_temp[cur_param_num] = Translate.temp_count++;
	    	  Translate.Print("\nMOVE TEMP "+param_temp[cur_param_num]+" TEMP "+f1_temp);
	      }
	      cur_param_num++;
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
	      return _ret;
	   }

	   /**
	    * f0 -> <INTEGER_LITERAL>
	    */
	   public MType visit(IntegerLiteral n, MType argu) {
		   MType _ret = new MInteger();
		   int return_temp = Translate.temp_count++;
		   Translate.Print("\nMOVE TEMP "+return_temp+" "+n.f0.tokenImage);
		   _ret.returntemp = return_temp;
		   return _ret;
	   }

	   /**
	    * f0 -> "true"
	    */
	   public MType visit(TrueLiteral n, MType argu) {
		   MType _ret = new MBoolean();
		   int return_temp = Translate.temp_count++;
		   Translate.Print("\nMOVE TEMP "+return_temp+" "+1);
		   _ret.returntemp = return_temp;
		   return _ret;
	   }

	   /**
	    * f0 -> "false"
	    */
	   public MType visit(FalseLiteral n, MType argu) {
	      MType _ret = new MBoolean();
	      int return_temp = Translate.temp_count++;
		   Translate.Print("\nMOVE TEMP "+return_temp+" "+0);
		   _ret.returntemp = return_temp;
	      return _ret;
	   }

	   /**
	    * f0 -> <IDENTIFIER>
	    */
	   public MType visit(Identifier n, MType argu) {

		   String identifier_name = n.f0.toString();
		   MType _ret = ((MClasses)Mainclass.my_classes).getClassByName(identifier_name);
		   if (_ret == null) {
			   _ret = Judge.isMethodDeclared(identifier_name, argu.getField());
			   if (_ret == null) {
				   _ret = Judge.isVarDeclared(identifier_name, argu);
				   
				   if (!identifier_name.equals("this")) {
					   if (((MMethod)argu).variable_table.containsKey(identifier_name)) {
						   _ret.returntemp = ((MMethod)argu).variable_table.get(identifier_name);
					   }
					   else if (((MMethod)argu).param_table.containsKey(identifier_name)) {
						   //方法参数
						   _ret.returntemp =((MMethod)argu).param_table.get(identifier_name);
					   }
					   else {
						   //类的成员变量
						   MClass mclass = (MClass) argu.getField();
						   while (mclass != null) {
							   if (mclass.getVarByName(identifier_name) != null)
								   break;
							   else mclass = mclass.getFather();
						   }
						   identifier_name = mclass.getName()+"_"+identifier_name;
						   int temp_return_count = Translate.temp_count++;
						   Translate.Print("\nHLOAD "+"TEMP "+temp_return_count+" "+"TEMP 0 "+
								   ((MClass)argu.getField()).variable_table.get(identifier_name)*4);
						   _ret.returntemp = temp_return_count;
					   }
				   }
			   }
		   }
		   return _ret;
	   }

	   /**
	    * f0 -> "this"
	    */
	   public MType visit(ThisExpression n, MType argu) {
		   MType _ret = Judge.isVarDeclared("this", argu);
		   _ret.returntemp = 0;
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
		   int f3_temp = n.f3.accept(this, argu).returntemp;
		   //得到数组长度
		   int array_length_temp = Translate.temp_count++;
		   Translate.Print("\nMOVE "+"TEMP "+array_length_temp+" TEMP "+f3_temp);
		   int address_length_temp = Translate.temp_count++;
		   int one_temp = Translate.temp_count++;
		   Translate.Print("\nMOVE TEMP "+one_temp+" 1");
		   Translate.Print("\nMOVE TEMP "+address_length_temp+" PLUS TEMP "+one_temp+" TEMP "+array_length_temp);
		   //为数组申请地址空间
		   int address_size_temp = Translate.temp_count++;
		   int four_temp = Translate.temp_count++;
		   Translate.Print("\nMOVE TEMP "+four_temp+" 4");
		   Translate.Print("\nMOVE TEMP "+address_size_temp+" TIMES TEMP "+four_temp+" TEMP "+address_length_temp);
		   int array_temp = Translate.temp_count++;
		   Translate.Print("\nMOVE TEMP "+array_temp+" HALLOCATE TEMP "+address_size_temp);
		   
		   //数组地址空间的首个字节记录数组长度
		   Translate.Print("\nHSTORE TEMP "+array_temp+" 0 TEMP "+f3_temp);
		   
		   int initlabel1 = Translate.label_count++;
		   int initlabel2 = Translate.label_count++;
		   
		   //数组初始化
		   int array_index_temp = Translate.temp_count++;
		   int init_value_temp = Translate.temp_count++;
		   Translate.Print("\nMOVE TEMP "+init_value_temp+" 0");
		   Translate.Print("\nMOVE TEMP "+array_index_temp+" 0");
		   Translate.Print("\nL"+initlabel1+" NOOP");
		   int judge_bound_temp = Translate.temp_count++;
		   Translate.Print("\nMOVE TEMP "+judge_bound_temp+" LT TEMP "+array_index_temp+" TEMP "+f3_temp);
		   Translate.Print("\nCJUMP TEMP "+judge_bound_temp+" L"+initlabel2);
		   int index_temp = Translate.temp_count++;
		   Translate.Print("\nMOVE TEMP "+index_temp+" PLUS TEMP "+one_temp+" TEMP "+array_index_temp);
		   Translate.Print("\nMOVE TEMP "+index_temp+" TIMES TEMP "+four_temp+" TEMP "+index_temp);
		   int real_address_temp = Translate.temp_count++;
		   Translate.Print("\nMOVE TEMP "+real_address_temp+" PLUS TEMP "+array_temp+" TEMP "+index_temp);
		   
		   Translate.Print("\nHSTORE TEMP "+real_address_temp+" 0 TEMP "+init_value_temp);
		   Translate.Print("\nMOVE TEMP "+array_index_temp+" PLUS TEMP "+one_temp+" TEMP "+array_index_temp);
		   Translate.Print("\nJUMP L"+initlabel1);
		   Translate.Print("\nL"+initlabel2+" NOOP");
		   
		   //返回数组起始地址
		   MType _ret = new MArray(argu);
		   _ret.returntemp = array_temp;
		   return _ret;
	   }

	   /**
	    * f0 -> "new"
	    * f1 -> Identifier()
	    * f2 -> "("
	    * f3 -> ")"
	    */
	   public MType visit(AllocationExpression n, MType argu) {
		   	MClass mclass = ((MClasses)Mainclass.my_classes).getClassByName(n.f1.f0.toString());
		   	int variable_num=mclass.variable_table.size();
			int method_num=mclass.method_table.size();
		    
		    int method_temp = Translate.temp_count++;
		    int variable_temp = Translate.temp_count++;	    
		    //为方法分配地址空间
		    Translate.Print("\nMOVE "+"TEMP "+method_temp+" "+"HALLOCATE "+method_num*4);
		   
		    //为每个方法分配地址 
		    Iterator<String> it = mclass.method_table.keySet().iterator();
		    int method_name_temp = Translate.temp_count++;
		    while (it.hasNext()) {
		    	String temp = (String)it.next();
		    	Translate.Print("\nMOVE TEMP "+method_name_temp+" "+temp);
		    	Translate.Print("\nHSTORE "+"TEMP "+method_temp+" "+mclass.method_table.get(temp)*4+" TEMP "+method_name_temp);
		    }
		    
		    //为变量分配地址
		    Translate.Print("\nMOVE "+"TEMP "+variable_temp+" "+"HALLOCATE "+(variable_num+1)*4);
		    
		    //如果有变量则对变量进行初始化
		    it = mclass.variable_table.keySet().iterator();
		    int variable_value_temp = Translate.temp_count++;
		    Translate.Print("\nMOVE TEMP "+variable_value_temp+" "+0);
		    while (it.hasNext()) {
		    	String temp = (String)it.next();
		    	Translate.Print("\nHSTORE "+"TEMP "+variable_temp+" "+mclass.variable_table.get(temp)*4+" TEMP "+variable_value_temp);
		    }
		    
		    //将方法地址空间首地址存入变量地址空间的最开始
		    Translate.Print("\nHSTORE "+"TEMP "+variable_temp+" "+"0 "+"TEMP "+method_temp);
		    //将变量地址空间首地址传回

		    MType _ret = new MIdentifier(n.f1.f0.toString(), argu);
		    _ret.returntemp = variable_temp;
		    return _ret;
	   }

	   /**
	    * f0 -> "!"
	    * f1 -> Expression()
	    */
	   public MType visit(NotExpression n, MType argu) {
		   int right_temp = n.f1.accept(this, argu).returntemp;
		   int return_temp = Translate.temp_count++;
		   int one_temp = Translate.temp_count++;
		   Translate.Print("\nMOVE TEMP "+one_temp+" 1");
		   Translate.Print("\nMOVE TEMP "+return_temp);
		   Translate.Print("MINUS TEMP "+one_temp+" TEMP "+right_temp);
		   MType _ret = new MBoolean();
		   _ret.returntemp = return_temp;
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
	   
	   /**
	    * 检查空指针
	    * @param temp_num
	    */
	   private void NullCheck(int temp_num) {
		   int noerrorlabel = Translate.label_count++;
		   int judge_temp = Translate.temp_count++;
		   Translate.Print("\nMOVE TEMP "+judge_temp+" LT TEMP "+temp_num+" 1");
		   Translate.Print("\nCJUMP TEMP "+judge_temp+" L"+noerrorlabel);
		   Translate.Print("\nERROR");
		   Translate.Print("\nL"+noerrorlabel+" NOOP");
	   }

}
