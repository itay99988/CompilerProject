package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;

public class AST_EXP_VAR extends AST_EXP
{
	public AST_VAR var;


	public AST_EXP_VAR(AST_VAR var, int lineNumber) {
		System.out.print("====================== exp -> var\n");
		this.var = var;

		this.setLineNumber(lineNumber);
	}
	

	public void PrintMe() {
		/************************************/
		/* AST NODE TYPE = EXP VAR AST NODE */
		/************************************/
		System.out.print("AST NODE: EXP VAR\n");

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (var != null) var.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"EXP\nVAR");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
			
	}
	
	public TYPE SemantMe() throws SemantException
	{
		return this.var.SemantMe();
	}

	public TEMP MIPSme() 
	{
		return this.var.getMipsValue();
	}

}
