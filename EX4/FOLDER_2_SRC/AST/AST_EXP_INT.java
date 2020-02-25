package AST;

import TYPES.*;
import TEMP.*;
import MIPS.*;

public class AST_EXP_INT extends AST_EXP
{
	public AST_FULLINT fullInt;
	

	public AST_EXP_INT(AST_FULLINT fullInt) {
		System.out.print("====================== exp -> fullInt\n");

		this.fullInt = fullInt;
	}


	public void PrintMe() {
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		System.out.format("AST NODE: EXP INT\n", fullInt);

		/*********************************/
		/* RECURSIVELY PRINT fullInt ... */
		/*********************************/
		if (fullInt != null) fullInt.PrintMe();

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("EXP INT", fullInt));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, fullInt.SerialNumber);
	}

	public TYPE SemantMe() throws SemantException
	{
		return this.fullInt.SemantMe();
	}
	
	public TEMP MIPSme() 
	{
		return this.fullInt.MIPSme();
	}

}
