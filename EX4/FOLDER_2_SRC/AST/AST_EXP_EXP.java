package AST;

import TYPES.*;
import TEMP.*;

public class AST_EXP_EXP extends AST_EXP {

    public AST_EXP exp;

    public AST_EXP_EXP(AST_EXP exp, int lineNumber) {
        this.exp = exp;

		this.setLineNumber(lineNumber);
        System.out.print("====================== exp -> LPAREN exp RPAREN\n");
    }
	
	public void PrintMe() 
	{
		/**********************************/
		/* AST NODE TYPE = AST EXP NIL */
		/**********************************/
		System.out.print("AST NODE: EXP_EXP\n");

		/*****************************/
		/* RECURSIVELY PRINT exp ... */
		/*****************************/
		if (exp != null) exp.PrintMe();

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
            "(exp)");
            
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);

	}
	
	public TYPE SemantMe() throws SemantException
	{
		return exp.SemantMe();
	}

	public TEMP MIPSme() 
	{
		return exp.MIPSme();
	}
	
}