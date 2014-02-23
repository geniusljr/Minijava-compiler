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

public class BuildFlowGraphVisitor extends GJDepthFirst<NewMType, NewMType> {
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
				e.nextElement().accept(this,argu);
            	_count++;
			}
			return _ret;
		}
		else
			return null;
	}

	public NewMType visit(NodeOptional n, NewMType argu) {
		
		//只有一种情况，即读取每条statement最前面的Label时会进入该节点
		if ( n.present() ) {
			//如果存在则将该Label名称返回
			return new MString(((Label)n.node).f0.tokenImage);
		}
      	else
      		return null;
   	}

	public NewMType visit(NodeSequence n, NewMType argu) {
		
		NewMType _ret=null;
		int _count=0;
		
		//读取每条statement时会进入该节点
		NewMType graphNode = new FlowGraphNode((FlowGraph)argu);
		
		for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
			NewMType mtype = e.nextElement().accept(this,graphNode);
			if (_count == 0 && mtype != null) 
				//如果最前面有Label，则把该条语句对应的CFG节点添加一个Label
				((FlowGraphNode)graphNode).setNodeLabel(((MString)mtype).getName());
			_count++;
		}
		
		//如果argu是FlowGraph类型，则把当前新生成的节点添加到argu中。
		if (argu.getType() != null)
			((FlowGraph)argu).addNode((FlowGraphNode)graphNode);
		
		return _ret;
	}

	public NewMType visit(NodeToken n, NewMType argu) { 
		return null;
	}
	
	/**
	 * f0 -> "MAIN"
	 * f1 -> StmtList()
	 * f2 -> "END"
	 * f3 -> ( Procedure() )*
	 * f4 -> <EOF>
	 */
	public NewMType visit(Goal n, NewMType argu) {
		NewMType _ret=null;
		
		FlowGraph maingraph = new FlowGraph("MAIN", 0);
		((AllFlowGraphs)argu).addGraph(maingraph);
		
		//遍历方法中每条语句时argu都是其所在CFG
		n.f1.accept(this, maingraph);
		
		n.f3.accept(this, argu);
		
		//活性变量分析，并进行LinearScan算法分配寄存器
		maingraph.make();		
		
		return _ret;
	}

	/**
	 * f0 -> ( ( Label() )? Stmt() )*
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
      
		FlowGraph procedureGraph = new FlowGraph(n.f0.f0.tokenImage, Integer.parseInt(n.f2.f0.tokenImage));

		//将该CFG添加到所有CFG所在的集合中
		((AllFlowGraphs)argu).addGraph(procedureGraph);
		
		//遍历方法中每条语句时argu都是其所在CFG
		n.f4.accept(this, procedureGraph);

		//活性变量分析，并进行LinearScan算法分配寄存器
		procedureGraph.make();	
		
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
		NewMType _ret=null;
		n.f0.accept(this, argu);
		return _ret;
	}

	/**
	 * f0 -> "NOOP"
	 */
	public NewMType visit(NoOpStmt n, NewMType argu) {
		NewMType _ret=null;
		return _ret;
	}

	/**
	 * f0 -> "ERROR"
	 */
	public NewMType visit(ErrorStmt n, NewMType argu) {
		NewMType _ret=null;
		return _ret;
	}

	/**
	 * f0 -> "CJUMP"
	 * f1 -> Temp()
	 * f2 -> Label()
	 */
	public NewMType visit(CJumpStmt n, NewMType argu) {
		NewMType _ret=null;
		
		//此TEMP属该条语句的USE集合
		n.f1.accept(this, argu);
		
		//设置该条语句的跳转标号
		((FlowGraphNode)argu).setCJump();
		((FlowGraphNode)argu).setJumpLabel(n.f2.f0.tokenImage);	
		
		return _ret;
	}

	/**
	 * f0 -> "JUMP"
	 * f1 -> Label()
	 */
	public NewMType visit(JumpStmt n, NewMType argu) {
		NewMType _ret=null;

		//设置该条语句的跳转标号
		((FlowGraphNode)argu).setJump();
		((FlowGraphNode)argu).setJumpLabel(n.f1.f0.tokenImage);
		
		return _ret;
	}

	/**
	 * f0 -> "HSTORE"
	 * f1 -> Temp()
	 * f2 -> IntegerLiteral()
	 * f3 -> Temp()
	 */
	public NewMType visit(HStoreStmt n, NewMType argu) {
		NewMType _ret=null;
		
		//此TEMP属该条语句的USE集合
		n.f1.accept(this, argu);
		//此TEMP属该条语句的USE集合
		n.f3.accept(this, argu);
		
		return _ret;
	}

	/**
	 * f0 -> "HLOAD"
	 * f1 -> Temp()
	 * f2 -> Temp()
	 * f3 -> IntegerLiteral()
	 */
	public NewMType visit(HLoadStmt n, NewMType argu) {
		NewMType _ret=null;
		
		//此TEMP属该条语句的DEF集合
		((FlowGraphNode)argu).addDEF(n.f1.f1.f0.tokenImage);
		
		//此TEMP属该条语句的USE集合
		n.f2.accept(this, argu);
		
		return _ret;
	}

	/**
	 * f0 -> "MOVE"
	 * f1 -> Temp()
	 * f2 -> Exp()
	 */
	public NewMType visit(MoveStmt n, NewMType argu) {
		NewMType _ret=null;
		
		//此TEMP属该条语句的DEF集合
		((FlowGraphNode)argu).addDEF(n.f1.f1.f0.tokenImage);
		n.f2.accept(this, argu);
		
		return _ret;
	}

	/**
	 * f0 -> "PRINT"
	 * f1 -> SimpleExp()
	 */
	public NewMType visit(PrintStmt n, NewMType argu) {
		NewMType _ret=null;
		
		n.f1.accept(this, argu);

		return _ret;
	}

	/**
	 * f0 -> Call()
	 *       | HAllocate()
	 *       | BinOp()
	 *       | SimpleExp()
	 */
	public NewMType visit(Exp n, NewMType argu) {
		NewMType _ret=null;
		n.f0.accept(this, argu);
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

		//将最后的RETURN语句添加为(FlowGraph)argu这个CFG的最后一个节点
		NewMType graphNode = new FlowGraphNode((FlowGraph)argu);
		n.f3.accept(this, graphNode);
		((FlowGraph)argu).addNode((FlowGraphNode) graphNode);

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
		NewMType _ret=null;
		
		n.f1.accept(this, argu);

		//此TEMP属该条语句的USE集合
		n.f3.accept(this, argu);
		
		//更新这个方法中所调用所有函数的最大参数个数
		((FlowGraphNode)argu).getField().updateMaxCalledParam(n.f3.size());
		
		return _ret;
	}

	/**
	 * f0 -> "HALLOCATE"
	 * f1 -> SimpleExp()
	 */
	public NewMType visit(HAllocate n, NewMType argu) {
		NewMType _ret=null;
		
		n.f1.accept(this, argu);
		
		return _ret;
	}

	/**
	 * f0 -> Operator()
	 * f1 -> Temp()
	 * f2 -> SimpleExp()
	 */
	public NewMType visit(BinOp n, NewMType argu) {
		NewMType _ret=null;

		//此TEMP属该条语句的USE集合
		n.f1.accept(this, argu);
		
		n.f2.accept(this, argu);
		
		return _ret;
	}

	/**
	 * f0 -> "LT"
	 *       | "PLUS"
	 *       | "MINUS"
	 *       | "TIMES"
	 */
	public NewMType visit(Operator n, NewMType argu) {
		NewMType _ret=null;
		n.f0.accept(this, argu);
		return _ret;
	}	

	/**
	 * f0 -> Temp()
	 *       | IntegerLiteral()
	 *       | Label()
	 */
	public NewMType visit(SimpleExp n, NewMType argu) {
		NewMType _ret=null;
		n.f0.accept(this, argu);
		return _ret;
	}

	/**
	 * f0 -> "TEMP"
	 * f1 -> IntegerLiteral()
	 */
	public NewMType visit(Temp n, NewMType argu) {
		NewMType _ret=null;
		
		//只有属于当前语句USE集合的TEMP才会进入到该节点
		if (argu.getType() == null)
			((FlowGraphNode)argu).addUSE(n.f1.f0.tokenImage);		
		return _ret;
	}	

	/**
	 * f0 -> <INTEGER_LITERAL>
	 */
	public NewMType visit(IntegerLiteral n, NewMType argu) {
		NewMType _ret=null;
		n.f0.accept(this, argu);
		return _ret;
	}

	/**
	 * f0 -> <IDENTIFIER>
	 */
	public NewMType visit(Label n, NewMType argu) {
		NewMType _ret=null;
		n.f0.accept(this, argu);
		return _ret;
	}

}
