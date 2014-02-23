package spiglet.spiglet2kanga;

import java.util.Enumeration;

import spiglet.syntaxtree.BinOp;
import spiglet.syntaxtree.CJumpStmt;
import spiglet.syntaxtree.Call;
import spiglet.syntaxtree.ErrorStmt;
import spiglet.syntaxtree.Exp;
import spiglet.syntaxtree.Goal;
import spiglet.syntaxtree.HAllocate;
import spiglet.syntaxtree.HLoadStmt;
import spiglet.syntaxtree.HStoreStmt;
import spiglet.syntaxtree.IntegerLiteral;
import spiglet.syntaxtree.JumpStmt;
import spiglet.syntaxtree.Label;
import spiglet.syntaxtree.MoveStmt;
import spiglet.syntaxtree.NoOpStmt;
import spiglet.syntaxtree.Node;
import spiglet.syntaxtree.NodeList;
import spiglet.syntaxtree.NodeListOptional;
import spiglet.syntaxtree.NodeOptional;
import spiglet.syntaxtree.NodeSequence;
import spiglet.syntaxtree.NodeToken;
import spiglet.syntaxtree.Operator;
import spiglet.syntaxtree.PrintStmt;
import spiglet.syntaxtree.Procedure;
import spiglet.syntaxtree.SimpleExp;
import spiglet.syntaxtree.Stmt;
import spiglet.syntaxtree.StmtExp;
import spiglet.syntaxtree.StmtList;
import spiglet.syntaxtree.Temp;
import spiglet.visitor.GJDepthFirst;

public class TranslateVisitor extends GJDepthFirst<NewMType, NewMType>{
	//
	// Auto class visitors--probably don't need to be overridden.
	//
	public NewMType visit(NodeList n, NewMType argu) {
		NewMType _ret=null;
		int _count=0;
		for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
			e.nextElement().accept(this,argu);
			_count++;
		}
		return _ret;
	}

	public NewMType visit(NodeListOptional n, NewMType argu) {
		if ( n.present() ) {
			NewMType _ret=null;
			int _count=0;
			for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
	        	 if (argu.getType() != null)
	        		 e.nextElement().accept(this, ((FlowGraph)argu).nodes.elementAt(_count));
	        	 else e.nextElement().accept(this,argu);
	        	 _count++;
			}
			return _ret;
		}
		else
			return null;
	}

	public NewMType visit(NodeOptional n, NewMType argu) {
		if ( n.present() ) {
			//如果当前待翻译语句有标号，则将标号输出
			//且因为kanga的跳转标号是全局的，所以将标号名称前加该语句所在方法的名称从而进行区分
			Console.Print(((FlowGraphNode)argu).getField().getName()+"_"+((Label)n.node).f0.tokenImage+" ");
			return null;
		}
		else
			return null;
	}

	public NewMType visit(NodeSequence n, NewMType argu) {
		NewMType _ret=null;
		int _count=0;
	        
		for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
			e.nextElement().accept(this, argu);
			_count++;
		}
		return _ret;
	}
	   	
	public NewMType visit(NodeToken n, NewMType argu) { return null; }
	
	//
	// User-generated visitor methods below
	//
	
	/**
	 * f0 -> "MAIN"
	 * f1 -> StmtList()
	 * f2 -> "END"
	 * f3 -> ( Procedure() )*
	 * f4 -> <EOF>
	 */
	public NewMType visit(Goal n, NewMType argu) {
		
		NewMType _ret=null;
		
		Console.Print("MAIN ");
		FlowGraph main = ((AllFlowGraphs)argu).getGraphByName("MAIN");
		Console.Print("["+main.getParamNum()+"] ["+main.getSpillNum()+"] ["+main.getMaxCalledParam()+"]\n");
		
		//先把主方法中用到的寄存器保存
		int count = 0;
		for (int i = 0; i < FlowGraph.MAX_COLOR; i++) {
			if (main.used_colors[i]) {
				Console.Print("ASTORE  SPILLEDARG "+count+" "+Console.regName[i]+"\n");
				count++;
			}
		}
	      
		n.f1.accept(this, main);
		
		//还原主方法中用到的寄存器
		count--;
		for (int i = FlowGraph.MAX_COLOR-1; i >= 0; i--) {
			if (main.used_colors[i]) {
				Console.Print("ALOAD "+Console.regName[i]+" SPILLEDARG "+count+"\n");
				count--;
			}			
		}
			
		Console.Print("END\n");
	      	
		n.f3.accept(this, argu);
		
		return _ret;
   	}

	/**
	 * f0 -> ( ( Label() )? Stmt() )*
	 * 
	 */
	public NewMType visit(StmtList n, NewMType argu) {
		NewMType _ret=null;	
		n.f0.accept(this, argu);
		return _ret;
   	}

	/**
	 * f0 -> Label()
	 * f1 -> "["
	 * f2 -> IntegerLiteral()
	 * f3 -> "]"
	 * f4 -> StmtExp()
	 */
	public NewMType visit(Procedure n, NewMType argu) {
		
		NewMType _ret=null;
		
		Console.Print(n.f0.f0.tokenImage+" ");
		FlowGraph procedure = ((AllFlowGraphs)argu).getGraphByName(n.f0.f0.tokenImage);
		Console.Print("["+procedure.getParamNum()+"] ["+procedure.getSpillNum()+"] ["+procedure.getMaxCalledParam()+"]\n");	      
		
		int paramNum = procedure.getParamNum();
		
		//先把方法中用到的寄存器保存
		int count = paramNum > 4 ? paramNum - 4 : 0;
		for (int i = 0; i < FlowGraph.MAX_COLOR; i++) {
			if (procedure.used_colors[i]) {
				Console.Print("ASTORE  SPILLEDARG "+count+" "+Console.regName[i]+"\n");
				count++;
			}
		}
		
		//把所有参数存储到t/s寄存器或栈中
		paramNum = paramNum > 4 ? 4 : paramNum;
		for (int i = 0; i < paramNum; i++) {
			MyVariable variable = procedure.all_variables.get(i);
			if (variable != null) {
				//如果第一条语句的IN集合包含这个参数，才需要保存，否则不需要
				if (procedure.nodes.firstElement().IN.contains(variable.getValue())) {
					if (!variable.isSpilled())
						Console.Print("MOVE "+Console.regName[variable.getColor()]+" a"+i+"\n");
					else {
						Console.Print("ASTORE SPILLEDARG "+variable.getSpillNum()+" a"+i+"\n");
					}
				}
			}
		}	
		paramNum = procedure.getParamNum();
		//处理多余4个的参数
		if (procedure.getParamNum() > 4) {
			for (int i = 4; i < paramNum; i++) {
				MyVariable variable = procedure.all_variables.get(i);
				if (variable != null) {
					if (procedure.nodes.firstElement().IN.contains(variable.getValue())) {
						if (!variable.isSpilled())
							Console.Print("ALOAD "+Console.regName[procedure.all_variables.get(i).getColor()]+" SPILLEDARG "+(i-4)+"\n");
						else {
							Console.Print("ALOAD v0 SPILLEDARG "+(i-4)+"\n");
							Console.Print("ASTORE SPILLEDARG "+variable.getSpillNum()+" v0\n");
						}
					}
				}
			}	
      	}	
		
		n.f4.accept(this, procedure);
		
		//还原主方法中用到的寄存器
		count--;
		for (int i = FlowGraph.MAX_COLOR-1; i >= 0; i--) {
			if (procedure.used_colors[i]) {
				Console.Print("ALOAD "+Console.regName[i]+" SPILLEDARG "+count+"\n");
				count--;
			}
		}
	      
		Console.Print("END\n");
		return _ret;
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
	 */
	public NewMType visit(Stmt n, NewMType argu) {
		
		n.f0.accept(this, argu);
		
		return null;
	}
	
	/**
	 * f0 -> "NOOP"
	 */
	public NewMType visit(NoOpStmt n, NewMType argu) {
		
		Console.Print("NOOP\n");
	      
		return null;
   	}				

	/**
	 * f0 -> "ERROR"
	 */
	public NewMType visit(ErrorStmt n, NewMType argu) {
		
		Console.Print("ERROR\n");
		
		return null;
	}

	/**
	 * f0 -> "CJUMP"
	 * f1 -> Temp()
	 * f2 -> Label()
	 */
	public NewMType visit(CJumpStmt n, NewMType argu) {
	
		NewMType f1 = n.f1.accept(this, argu);
		NewMType f2 = n.f2.accept(this, argu);
		Console.Print("CJUMP "+f1.getName()+" "+((FlowGraphNode)argu).getField().getName()+"_"+f2.getName()+"\n");
		
		return null;
	}

	/**
	 * f0 -> "JUMP"
	 * f1 -> Label()
	 */
	public NewMType visit(JumpStmt n, NewMType argu) {
		
		NewMType f1 = n.f1.accept(this, argu);
		Console.Print("JUMP "+((FlowGraphNode)argu).getField().getName()+"_"+f1.getName()+"\n");
		
		return null;
   	}				
	
	/**
	 * f0 -> "HSTORE"
	 * f1 -> Temp()
	 * f2 -> IntegerLiteral()
	 * f3 -> Temp()
	 */
	public NewMType visit(HStoreStmt n, NewMType argu) {	

		NewMType f1 = n.f1.accept(this, argu);
		NewMType f2 = n.f2.accept(this, argu);
		NewMType f3 = n.f3.accept(this, argu);		
		Console.Print("HSTORE "+f1.getName()+" "+f2.getName()+" "+f3.getName()+"\n");
		
		return null;
	}
	
	/**
	 * f0 -> "HLOAD"
	 * f1 -> Temp()
	 * f2 -> Temp()
	 * f3 -> IntegerLiteral()
	 */
	public NewMType visit(HLoadStmt n, NewMType argu) {
		
		//只有当该条语句的OUT集合中包含f1这个变量时才需要翻译这条语句
		if (((FlowGraphNode)argu).OUT.contains(Integer.parseInt(n.f1.f1.f0.tokenImage))) {
			NewMType f2 = n.f2.accept(this, argu);
			NewMType f3 = n.f3.accept(this, argu);
			NewMType f1 = n.f1.accept(this, argu);
			Console.Print("HLOAD "+f1.getName()+" "+f2.getName()+" "+f3.getName()+"\n");
			MyVariable variable = ((FlowGraphNode)argu).getField().all_variables.get(Integer.parseInt(n.f1.f1.f0.tokenImage));
			if (variable.isSpilled()) {
				Console.Print("ASTORE SPILLEDARG "+variable.getSpillNum()+" "+f1.getName()+"\n");
			}
		}
		//否则
		else {
			//如果这条语句有标号，即可能有语句跳转到该处，则把该语句翻译为NOOP
			if (((FlowGraphNode)argu).getNodeLabel() != null)
				Console.Print("NOOP\n");
		}
		
		return null;
	}

	/**
	 * f0 -> "MOVE"
	 * f1 -> Temp()
	 * f2 -> Exp()
	 */
	public NewMType visit(MoveStmt n, NewMType argu) {
	
		MyVariable variable = ((FlowGraphNode)argu).getField().all_variables.get(Integer.parseInt(n.f1.f1.f0.tokenImage));
		
		//只有当该条语句的OUT集合中包含f1这个变量时才需要翻译这条语句
		if (((FlowGraphNode)argu).OUT.contains(Integer.parseInt(n.f1.f1.f0.tokenImage))) {
			NewMType f2 = n.f2.accept(this, argu);
			if (variable.isSpilled()) {
				Console.Print("MOVE a0 "+f2.getName()+"\n");
				Console.Print("ASTORE SPILLEDARG "+variable.getSpillNum()+" a0\n");
			}
			else Console.Print("MOVE "+Console.regName[variable.getColor()]+" "+f2.getName()+"\n");
	    }
		else {
			//如果f2代表的是一个方法的调用，则仍需要把这条调用语句翻译出来
			if (n.f2.f0.which == 0) n.f2.accept(this, argu);
			else {
				//如果这条语句有标号，即可能有语句跳转到该处，则把该语句翻译为NOOP
				if (((FlowGraphNode)argu).getNodeLabel() != null)
					Console.Print("NOOP\n");
			}
		}
		
		return null;
   	}

	/**	
	 * f0 -> "PRINT"
	 * f1 -> SimpleExp()
	 */
	public NewMType visit(PrintStmt n, NewMType argu) {
		
		NewMType f1 = n.f1.accept(this, argu);
		Console.Print("PRINT "+f1.getName()+"\n");
	
      	return null;
	}	

	/**
	 * f0 -> Call()
	 *       | HAllocate()
	 *       | BinOp()
	 *       | SimpleExp()
	 */
	public NewMType visit(Exp n, NewMType argu) {
		NewMType _ret = n.f0.accept(this, argu);
		return _ret;
	}
	
	/**
	 * f0 -> "BEGIN"
	 * f1 -> StmtList()
	 * f2 -> "RETURN"
	 * f3 -> SimpleExp()
	 * f4 -> "END"
	 */
	public NewMType visit(StmtExp n, NewMType argu) {
		NewMType _ret=null;
		
		n.f1.accept(this, argu);
	      
		NewMType f3 = n.f3.accept(this, ((FlowGraph)argu).nodes.lastElement());
		Console.Print("MOVE v0 "+f3.getName()+"\n");			
		return _ret;	
	}	

	/**
	 * f0 -> "CALL"
	 * f1 -> SimpleExp()
	 * f2 -> "("
	 * f3 -> ( Temp() )*
	 * f4 -> ")"
	 */
	public NewMType visit(Call n, NewMType argu) {

		int paramNum = n.f3.size() > 4 ? 4 : n.f3.size();
		
		//少于四个参数，则存储到寄存器a中
		for (int i = 0; i < paramNum; i++) {
			NewMType f3 = ((Temp)n.f3.nodes.elementAt(i)).accept(this, argu);
			Console.Print("MOVE a"+i+" "+f3.getName()+"\n");
		}
		
		//多余的参数要通过PASSARG传到栈中
		paramNum = n.f3.size();	      
		if (paramNum > 4) {
			for (int i = 4; i < paramNum; i++) {
				NewMType f3 = ((Temp)n.f3.nodes.elementAt(i)).accept(this, argu);
				Console.Print("PASSARG "+(i-3)+" "+f3.getName()+"\n");
			}
		}
		
		NewMType f1 = n.f1.accept(this, argu);
		Console.Print("CALL "+f1.getName()+"\n");
		
		NewMType _ret = new MString("v0");
		return _ret;
	}
	
	/**
	 * f0 -> "HALLOCATE"
	 * f1 -> SimpleExp()
	 */
	public NewMType visit(HAllocate n, NewMType argu) {
		
		NewMType f1 = n.f1.accept(this, argu);
		
		NewMType _ret = new MString("HALLOCATE "+f1.getName());
		return _ret;
	}	
	
	/**
	 * f0 -> Operator()
	 * f1 -> Temp()
	 * f2 -> SimpleExp()
	 */
	public NewMType visit(BinOp n, NewMType argu) {
		
		NewMType f0 = n.f0.accept(this, argu);
		NewMType f1 = n.f1.accept(this, argu);
		NewMType f2 = n.f2.accept(this, argu);

		NewMType _ret = new MString(f0.getName()+" "+f1.getName()+" "+f2.getName());
		return _ret;
   	}	

	/**
	 * f0 -> "LT"
	 *       | "PLUS"
	 *       | "MINUS"
	 *       | "TIMES"
	 */
	public NewMType visit(Operator n, NewMType argu) {
		NewMType _ret = new MString();
					
		switch(n.f0.which) {
		case 0:_ret.setName("LT");break;
		case 1:_ret.setName("PLUS");break;
		case 2:_ret.setName("MINUS");break;
		case 3:_ret.setName("TIMES");break;
		}
		
		return _ret;
	}

	/**
	 * f0 -> Temp()
	 *       | IntegerLiteral()
	 *       | Label()
	 */
	public NewMType visit(SimpleExp n, NewMType argu) {
		NewMType _ret = n.f0.accept(this, argu);
		return _ret;
	}
	
	/**
	 * f0 -> "TEMP"
	 * f1 -> IntegerLiteral()
	 */	
	public NewMType visit(Temp n, NewMType argu) {
			
		NewMType _ret = new MString();
		MyVariable variable = ((FlowGraphNode)argu).getField().all_variables.get(Integer.parseInt(n.f1.f0.tokenImage));		
		
		//如果该TEMP溢出，则先存储到临时寄存器v中
		if (variable.isSpilled()) {
			//如果是第一个溢出，存储到v0中
			if (this.isFirst) {
				Console.Print("ALOAD v0 SPILLEDARG "+variable.getSpillNum()+"\n");
				this.setFirst();
				_ret.setName("v0");
			}
			//否则存储到v1中
			else {
				Console.Print("ALOAD v1 SPILLEDARG "+variable.getSpillNum()+"\n");
				this.clearFirst();
				_ret.setName("v1");
			}
		}
		else _ret.setName(Console.regName[variable.getColor()]);
		
		return _ret;
	}
	
	/**
	 * f0 -> <INTEGER_LITERAL>
	 */
	public NewMType visit(IntegerLiteral n, NewMType argu) {
		NewMType _ret = new MString(n.f0.tokenImage);
		return _ret;
	}	
		
	/**
	 * f0 -> <IDENTIFIER>
	 */
	public NewMType visit(Label n, NewMType argu) {
		NewMType _ret = new MString(n.f0.tokenImage);
		return _ret;
	}
	
	private boolean isFirst = true;
	
	private void setFirst() {
		isFirst = false;
	}
	
	private void clearFirst() {
		isFirst = true;
	}
	
	
}
