package AST;

import TYPES.*;

public class AST_EXP_NIL extends AST_EXP {

    public AST_EXP_NIL() {

        System.out.print("====================== exp -> NIL\n");
    }


	public void PrintMe() {
		/**********************************/
		/* AST NODE TYPE = AST EXP NIL */
		/**********************************/
		System.out.print("AST NODE: EXP_NIL\n");

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"NIL");
	}
	
	public TYPE SemantMe() throws SemantException{
		return TYPE_NIL.getInstance();
	}
    
}