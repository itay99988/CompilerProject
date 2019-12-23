package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_BRACESEXP extends AST_DEC {
	
	public AST_EXP exp;
	public AST_COMMAEXP commaExpsList;
	
	public AST_BRACESEXP(AST_EXP exp, AST_COMMAEXP commaExpsList) {
		this.exp = exp;
		this.commaExpsList = commaExpsList;

		System.out.print("====================== bracesExp -> LPAREN exp commaExpsLst RPAREN\n");
	}
	

	public void PrintMe() {
		/******************************************/
		/* AST NODE TYPE = BRACES EXP (AST NODE) */
		/******************************************/
		System.out.print("AST NODE: BRACES EXP\n");

		/***********************************************/
		/* RECURSIVELY PRINT exp and commaExpsList ... */
		/***********************************************/
		if (exp != null) exp.PrintMe();
		if (commaExpsList != null) commaExpsList.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"BRACES\nEXP");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
		if(commaExpsList != null) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, commaExpsList.SerialNumber);
		}
	}
	
	public TYPE SemantMe() throws SemantException
	{
		exp.SemantMe();
		
		if (commaExpsList != null)
			commaExpsList.SemantMe();
		
		return null;
	}
}
	
