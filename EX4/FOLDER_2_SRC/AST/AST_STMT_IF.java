package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public class AST_STMT_IF extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT_LIST body;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_IF(AST_EXP cond,AST_STMT_LIST body, int lineNumber)
	{
		this.cond = cond;
		this.body = body;

		this.setLineNumber(lineNumber);
		System.out.print("====================== stmt -> IF LPAREN exp RPAREN LBRACE stmtList RBRACE\n");
	}


	public void PrintMe() {
		/**************************************/
		/* AST NODE TYPE = STMT IF (AST NODE) */
		/**************************************/
		System.out.print("AST NODE: STMT IF\n");

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
			"STMT\nIF");

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
            String err = String.format("stmt_if: condition inside IF does not return an integer value.\n");
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
	
	public TEMP IRme()
	{
		String if_cond_label = IRcommand.getFreshLabel("ifCond");
		String if_true_label = IRcommand.getFreshLabel("ifTrue");
		String if_end_label = IRcommand.getFreshLabel("ifEnd");
		//Generate ifCond label and condition checking code.
		IR.getInstance().Add_IRcommand(new IRcommand_Label(if_cond_label));
		TEMP cond_temp = cond.IRme();
		//Generate beq code (if code == 0, jump to ifEnd).
		IR.getInstance().Add_IRcommand(new IRcommand_Jump_If_Eq_To_Zero(cond_temp,if_end_label));
		IR.getInstance().Add_IRcommand(new IRcommand_Label(if_true_label));
		//Generate body code.
		body.IRme();
		//Generate ifFalse label
		IR.getInstance().Add_IRcommand(new IRcommand_Label(if_end_label));
		return null;
	}	
	
}