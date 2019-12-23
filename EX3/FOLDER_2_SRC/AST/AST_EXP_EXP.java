package AST;

import TYPES.*;

public class AST_EXP_EXP extends AST_EXP {

    public AST_EXP exp;

    public AST_EXP_EXP(AST_EXP exp) {
        this.exp = exp;

        System.out.print("====================== exp -> LPAREN exp RPAREN\n");
    }
	
	
	public TYPE SemantMe() throws SemantException{
		return exp.SemantMe();
	}


	public void PrintMe() {
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
    
}