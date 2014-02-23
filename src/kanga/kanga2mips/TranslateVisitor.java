package kanga.kanga2mips;

import java.util.Enumeration;

import kanga.syntaxtree.ALoadStmt;
import kanga.syntaxtree.AStoreStmt;
import kanga.syntaxtree.BinOp;
import kanga.syntaxtree.CJumpStmt;
import kanga.syntaxtree.CallStmt;
import kanga.syntaxtree.ErrorStmt;
import kanga.syntaxtree.Exp;
import kanga.syntaxtree.Goal;
import kanga.syntaxtree.HAllocate;
import kanga.syntaxtree.HLoadStmt;
import kanga.syntaxtree.HStoreStmt;
import kanga.syntaxtree.IntegerLiteral;
import kanga.syntaxtree.JumpStmt;
import kanga.syntaxtree.Label;
import kanga.syntaxtree.MoveStmt;
import kanga.syntaxtree.NoOpStmt;
import kanga.syntaxtree.Node;
import kanga.syntaxtree.NodeList;
import kanga.syntaxtree.NodeListOptional;
import kanga.syntaxtree.NodeOptional;
import kanga.syntaxtree.NodeSequence;
import kanga.syntaxtree.NodeToken;
import kanga.syntaxtree.Operator;
import kanga.syntaxtree.PassArgStmt;
import kanga.syntaxtree.PrintStmt;
import kanga.syntaxtree.Procedure;
import kanga.syntaxtree.Reg;
import kanga.syntaxtree.SimpleExp;
import kanga.syntaxtree.SpilledArg;
import kanga.syntaxtree.Stmt;
import kanga.syntaxtree.StmtList;
import kanga.visitor.GJDepthFirst;

public class TranslateVisitor extends GJDepthFirst<String, KType>{
	   //
	   // Auto class visitors--probably don't need to be overridden.
	   //
	   public String visit(NodeList n, KType argu) {
	      String _ret=null;
	      int _count=0;
	      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
	         e.nextElement().accept(this,argu);
	         _count++;
	      }
	      return _ret;
	   }

	   public String visit(NodeListOptional n, KType argu) {
	      if ( n.present() ) {
	         String _ret=null;
	         int _count=0;
	         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
	            e.nextElement().accept(this,argu);
	            _count++;
	         }
	         return _ret;
	      }
	      else
	         return null;
	   }

	   public String visit(NodeOptional n, KType argu) {
	      if ( n.present() ) {
	    	  Console.Print(((Label)n.node).f0.tokenImage+":  ");
	         return null;
	      }
	      else
	         return null;
	   }

	   public String visit(NodeSequence n, KType argu) {
	      String _ret=null;
	      int _count=0;
	      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
	         e.nextElement().accept(this,argu);
	         _count++;
	      }
	      return _ret;
	   }

	   public String visit(NodeToken n, KType argu) { return null; }

	   //
	   // User-generated visitor methods below
	   //
	   
	/**
	 * f0 -> "MAIN"
	 * f1 -> "["
	 * f2 -> IntegerLiteral()
	 * f3 -> "]"
	 * f4 -> "["
	 * f5 -> IntegerLiteral()
	 * f6 -> "]"
	 * f7 -> "["
	 * f8 -> IntegerLiteral()
	 * f9 -> "]"
	 * f10 -> StmtList()
	 * f11 -> "END"
	 * f12 -> ( Procedure() )*
	 * f13 -> <EOF>
	 */
	public String visit(Goal n, KType argu) {
		
		Console.Println(".data");
		Console.Println(".align 0");
		Console.Println("endl: .asciiz \"\\n\"");
		Console.Println("error: .asciiz \"ERROR\\n\"");
		
		Console.Println();
		Console.Println(".text");
		Console.Println(".globl main");
		Console.Println("main:");
		
		int stackSize = Integer.parseInt(n.f5.f0.tokenImage);
		int maxOtherParam = Integer.parseInt(n.f8.f0.tokenImage);
		maxOtherParam = maxOtherParam > 4 ? maxOtherParam - 4 : 0;
		
		Console.Println("sw $fp, -8($sp)");
		Console.Println("sw $ra, -4($sp)");
		Console.Println("move $fp, $sp");
		Console.Println("subu $sp, $sp, "+(stackSize+2+maxOtherParam)*4);

		int maxParam = Integer.parseInt(n.f2.f0.tokenImage);
		maxParam = maxParam > 4 ? maxParam - 4 : 0;
		KType mainProcedure = new KType(maxParam);
		n.f10.accept(this, mainProcedure);
		
		Console.Println("addu $sp, $sp, "+(stackSize+2+maxOtherParam)*4);
		Console.Println("lw $fp, -8($sp)");
		Console.Println("lw $ra, -4($sp)");
		Console.Println("j $ra");
		
		n.f12.accept(this, argu);

		Console.Println();
		Console.Println(".text");
		Console.Println(".globl _halloc");
		Console.Println("_halloc:");
		Console.Println("li $v0, 9");
		Console.Println("syscall");
		Console.Println("j $ra");
		
		Console.Println();
		Console.Println(".text");
		Console.Println(".globl _printNum");
		Console.Println("_printNum:");
		Console.Println("li $v0, 1");
		Console.Println("syscall");
		Console.Println("la $a0, endl");
		Console.Println("li $v0, 4");
		Console.Println("syscall");
		Console.Println("j $ra");
		
		Console.Println();
		Console.Println(".text");
		Console.Println(".globl _printErr");
		Console.Println("_printErr:");
		Console.Println("la $a0, error");
		Console.Println("li $v0, 4");
		Console.Println("syscall");
		Console.Println("j $ra");
		
		return null;
	}

	/**
	 * f0 -> ( ( Label() )? Stmt() )*
	 */
	public String visit(StmtList n, KType argu) {
		n.f0.accept(this, argu);
		return null;
	}

	/**
	 * f0 -> Label()
	 * f1 -> "["
	 * f2 -> IntegerLiteral()
	 * f3 -> "]"
	 * f4 -> "["
	 * f5 -> IntegerLiteral()
	 * f6 -> "]"
	 * f7 -> "["
	 * f8 -> IntegerLiteral()
	 * f9 -> "]"
	 * f10 -> StmtList()
	 * f11 -> "END"
	 */
	public String visit(Procedure n, KType argu) {
		
		Console.Println();
		Console.Println(".text");
		Console.Println(".globl "+n.f0.f0.tokenImage);
		Console.Println(n.f0.f0.tokenImage+":");
		
		int stackSize = Integer.parseInt(n.f5.f0.tokenImage);
		int maxOtherParam = Integer.parseInt(n.f8.f0.tokenImage);
		maxOtherParam = maxOtherParam > 4 ? maxOtherParam - 4 : 0;
		Console.Println("sw $fp, -8($sp)");
		Console.Println("sw $ra, -4($sp)");
		Console.Println("move $fp, $sp");
		Console.Println("subu $sp, $sp, "+(stackSize+2+maxOtherParam)*4);
		
		int maxParam = Integer.parseInt(n.f2.f0.tokenImage);
		maxParam = maxParam > 4 ? maxParam - 4 : 0;
		KType procedure = new KType(maxParam);
		n.f10.accept(this, procedure);
		
		Console.Println("addu $sp, $sp, "+(stackSize+2+maxOtherParam)*4);
		Console.Println("lw $fp, -8($sp)");
		Console.Println("lw $ra, -4($sp)");
		Console.Println("j $ra");
	     
		return null;
	}

	/**
	 * f0 -> NoOpStmt()
	 *       | ErrorStmt()
	 *       | CJumpStmt()
	 *       | JumpStmt()
	 *       | HStoreStmt()
	 *       | HLoadStmt()
	 *       | MoveStmt()
	 *       | PrintStmt()
	 *       | ALoadStmt()
	 *       | AStoreStmt()
	 *       | PassArgStmt()
	 *       | CallStmt()
	 */
	public String visit(Stmt n, KType argu) {
		String _ret=null;
		n.f0.accept(this, argu);
		return _ret;
	}

	/**
	 * f0 -> "NOOP"
	 */
	public String visit(NoOpStmt n, KType argu) {
		Console.Println("nop");
		return null;
	}

	/**
	 * f0 -> "ERROR"
	 */
	public String visit(ErrorStmt n, KType argu) {
		Console.Println("jal _printErr");
		Console.Println("li $v0, 10");
		Console.Println("syscall");
		return null;
	}

	/**
	 * f0 -> "CJUMP"
	 * f1 -> Reg()
	 * f2 -> Label()
	 */
	public String visit(CJumpStmt n, KType argu) {
		String f1 = n.f1.accept(this, argu);
		Console.Println("li $v1, 1");
		Console.Println("bne $"+f1+", $v1, "+n.f2.f0.tokenImage);
		return null;
	}

	/**
	 * f0 -> "JUMP"
	 * f1 -> Label()
	 */
	public String visit(JumpStmt n, KType argu) {
      	Console.Println("j "+n.f1.f0.tokenImage);
		return null;
	}

	/**
	 * f0 -> "HSTORE"
	 * f1 -> Reg()
	 * f2 -> IntegerLiteral()
	 * f3 -> Reg()
	 */
	public String visit(HStoreStmt n, KType argu) {
		String f1 = n.f1.accept(this, argu);
		String f3 = n.f3.accept(this, argu);
		Console.Println("sw $"+f3+", "+n.f2.f0.tokenImage+"($"+f1+")");
		return null;
	}

	/**
	 * f0 -> "HLOAD"
	 * f1 -> Reg()
	 * f2 -> Reg()
	 * f3 -> IntegerLiteral()
	 */
	public String visit(HLoadStmt n, KType argu) {
		String f1 = n.f1.accept(this, argu);
		String f2 = n.f2.accept(this, argu);
		Console.Println("lw $"+f1+", "+n.f3.f0.tokenImage+"($"+f2+")");
		return null;
	}	

	/**
	 * f0 -> "MOVE"
	 * f1 -> Reg()
	 * f2 -> Exp()
	 */
	public String visit(MoveStmt n, KType argu) {
		String f1 = n.f1.accept(this, argu);
		String f2 = n.f2.accept(this, argu);
		Console.Println("move $"+f1+", $"+f2);
		return null;
	}

	/**
	 * f0 -> "PRINT"
	 * f1 -> SimpleExp()
	 */
	public String visit(PrintStmt n, KType argu) {
		String f1 = n.f1.accept(this, argu);
		Console.Println("move $a0, $"+f1);
		Console.Println("jal _printNum");
		return null;
	}

	/**
	 * f0 -> "ALOAD"
	 * f1 -> Reg()
	 * f2 -> SpilledArg()
	 */
	public String visit(ALoadStmt n, KType argu) {
		String f1 = n.f1.accept(this, argu);
		String f2 = n.f2.accept(this, argu);
		Console.Println("lw $"+f1+", "+f2);
		return null;
	}

	/**
	 * f0 -> "ASTORE"
	 * f1 -> SpilledArg()
	 * f2 -> Reg()
	 */
	public String visit(AStoreStmt n, KType argu) {
		String f1 = n.f1.accept(this, argu);
		String f2 = n.f2.accept(this, argu);
		Console.Println("sw $"+f2+", "+f1);
		return null;
   	}

	/**
	 * f0 -> "PASSARG"
	 * f1 -> IntegerLiteral()
	 * f2 -> Reg()
	 */
	public String visit(PassArgStmt n, KType argu) {
		String f2 = n.f2.accept(this, argu);
		int f1 = Integer.parseInt(n.f1.f0.tokenImage);
		Console.Println("sw $"+f2+", "+(f1-1)*4+"($sp)");
		return null;
	}

	/**
	 * f0 -> "CALL"
	 * f1 -> SimpleExp()
	 */
	public String visit(CallStmt n, KType argu) {
		
		String f1 = n.f1.accept(this, argu);
		if (n.f1.f0.which == 0)
			Console.Println("jalr $"+f1);
		else if (n.f1.f0.which == 2)
			Console.Println("jal "+f1);
		else {
			System.err.println("Error");
			System.exit(0);
		}
		return null;
	}

	/**
	 * f0 -> HAllocate()
	 *       | BinOp()
	 *       | SimpleExp()
	 */
	public String visit(Exp n, KType argu) {	
		String _ret = n.f0.accept(this, argu);
		return _ret;
	}

	/**
	 * f0 -> "HALLOCATE"
	 * f1 -> SimpleExp()
	 */
	public String visit(HAllocate n, KType argu) {
		String f1 = n.f1.accept(this, argu);
		Console.Println("move $a0, $"+f1);
		Console.Println("jal _halloc");
		
		String _ret = "v0";
		return _ret;
   	}

	/**
	 * f0 -> Operator()
	 * f1 -> Reg()
	 * f2 -> SimpleExp()
	 */
	public String visit(BinOp n, KType argu) {
		
		String f0 = n.f0.accept(this, argu);
		String f1 = n.f1.accept(this, argu);
		String f2 = n.f2.accept(this, argu);

		Console.Println(f0+" $v1, $"+f1+", $"+f2);
		
		String _ret = "v1"; 
		return _ret;
	}

	/**
	 * f0 -> "LT"
	 *       | "PLUS"
	 *       | "MINUS"
	 *       | "TIMES"
	 */
	public String visit(Operator n, KType argu) {
		String _ret = "";
		if (n.f0.which == 0)
			_ret = "slt";
		else if (n.f0.which == 1)
			_ret = "add";
		else if (n.f0.which == 2)
			_ret = "sub";
		else _ret = "mul";
		return _ret;
	}

	/**
	 * f0 -> "SPILLEDARG"
	 * f1 -> IntegerLiteral()
	 */
	public String visit(SpilledArg n, KType argu) {
		String _ret = "";
		int index = Integer.parseInt(n.f1.f0.tokenImage);
		if (index >= argu.getMaxParamNum())
			_ret = (index-argu.getMaxParamNum())*4+"($sp)";
		else
			_ret = (index*4)+"($fp)";
		return _ret;
	}

	/**
	 * f0 -> Reg()
	 *       | IntegerLiteral()
	 *       | Label()
	 */
	public String visit(SimpleExp n, KType argu) {
		String _ret = n.f0.accept(this, argu);
		return _ret;
	}

	/**
	 * f0 -> "a0"
	 *       | "a1"
	 *       | "a2"
	 *       | "a3"
	 *       | "t0"
	 *       | "t1"
	 *       | "t2"
	 *       | "t3"
	 *       | "t4"
	 *       | "t5"
	 *       | "t6"
	 *       | "t7"
	 *       | "s0"
	 *       | "s1"
	 *       | "s2"
	 *       | "s3"
	 *       | "s4"
	 *       | "s5"
	 *       | "s6"
	 *       | "s7"
	 *       | "t8"
	 *       | "t9"
	 *       | "v0"
	 *       | "v1"
	 */
    public String visit(Reg n, KType argu) {
    	return n.f0.choice.toString();
    }

    /**
     * f0 -> <INTEGER_LITERAL>
     */
    public String visit(IntegerLiteral n, KType argu) {
    	Console.Println("li $v1, "+n.f0.tokenImage);
    	return "v1";
    }	
    
    /**
     * f0 -> <IDENTIFIER>
     */
    public String visit(Label n, KType argu) {
    	Console.Println("la $v1, "+n.f0.tokenImage);
    	return "v1";
    }
}	
