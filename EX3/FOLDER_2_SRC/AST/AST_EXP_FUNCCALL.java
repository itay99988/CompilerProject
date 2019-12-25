package AST;

import TYPES.*;

public class AST_EXP_FUNCCALL extends AST_EXP {
	
	public AST_FUNC_CALL funcCall;
	
	public AST_EXP_FUNCCALL(AST_FUNC_CALL funcCall, int lineNumber){
		this.funcCall = funcCall;

		this.setLineNumber(lineNumber);
		System.out.print("====================== exp -> funcCall\n");
	}
	

	public void PrintMe() {
		/*******************************************/
		/* AST NODE TYPE = EXP FUNCCALL (AST NODE) */
		/*******************************************/
		System.out.print("AST NODE: EXP FUNCCALL\n");

		/**********************************/
		/* RECURSIVELY PRINT funcCall ... */
		/**********************************/
		if (funcCall != null) funcCall.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"EXP\nFUNC\nCALL");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, funcCall.SerialNumber);

	}

	public TYPE SemantMe() throws SemantException
	{
		return funcCall.SemantMe();
	}

}
	
