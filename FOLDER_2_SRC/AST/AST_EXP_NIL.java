package AST;

import TYPES.*;
import TEMP.*;
import MIPS.*;

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
	
    public TEMP MIPSme()
	{
    	TEMP zero = TEMP_FACTORY.getInstance().getFreshTEMP();
    	sir_MIPS_a_lot.getInstance().li(zero, 0);
    	return zero;
    }
    
}