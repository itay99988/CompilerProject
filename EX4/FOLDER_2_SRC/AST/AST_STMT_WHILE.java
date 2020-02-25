package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import MIPS.*;

public class AST_STMT_WHILE extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT_LIST body;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_WHILE(AST_EXP cond,AST_STMT_LIST body, int lineNumber) {
		this.cond = cond;
		this.body = body;

		this.setLineNumber(lineNumber);
		System.out.print("====================== stmt -> WHILE LPAREN exp RPAREN LBRACE stmtList RBRACE\n");
	}


	public void PrintMe() {
		/*****************************************/
		/* AST NODE TYPE = STMT WHILE (AST NODE) */
		/*****************************************/
		System.out.print("AST NODE: STMT WHILE\n");

		/***************************************/
		/* RECURSIVELY PRINT cond and body ... */
		/***************************************/
		if (cond != null) cond.PrintMe();
		if (body != null) body.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"STMT\nWHILE");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, cond.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, body.SerialNumber);
	}

	public TYPE SemantMe(TYPE returnedTypeExpected) throws SemantException
	{
		/****************************/
		/* [0] Semant the Condition */
		/****************************/
		if (cond.SemantMe() != TYPE_INT.getInstance())
		{
            String err = String.format("stmt_while: condition inside WHILE does not return integer value.\n");
			throw new SemantException(this.getLineNumber(), err);
		}

		/*************************/
		/* [1] Begin Class Scope */
		/*************************/
		SYMBOL_TABLE.getInstance().beginScope(false);

		/***************************/
		/* [2] Semant Data Members */
		/***************************/
		body.SemantMe(returnedTypeExpected);

		/*****************/
		/* [3] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope(false);

		/*********************************************************/
		/* [4] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;
	}
	
	public void MIPSme()
	{
		sir_MIPS_a_lot mips = sir_MIPS_a_lot.getInstance();
		String while_start = sir_MIPS_a_lot.getFreshLabel("while_start");
		String while_end = sir_MIPS_a_lot.getFreshLabel("while_end");
		
		mips.label(while_start); //loop entrance
    	TEMP condition_eval = cond.MIPSme(); //evaluate condition
    	mips.beqz(condition_eval, while_end);
    	body.MIPSme(); //loop body
    	mips.jump(while_start);
    	mips.label(while_end);
		
	}
	
}