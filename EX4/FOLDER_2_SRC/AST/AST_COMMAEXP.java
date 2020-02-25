package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;

public class AST_COMMAEXP extends AST_DEC {
	
	public AST_EXP exp;
	public AST_COMMAEXP commaExpsList;

	public AST_COMMAEXP(AST_EXP exp, AST_COMMAEXP commaExpsList, int lineNumber){
		this.exp = exp;
		this.commaExpsList = commaExpsList;

		this.setLineNumber(lineNumber);
		System.out.print("====================== commaExpsList -> COMMA exp commaExpsLst\n");
	}
	
	public void PrintMe() {
		/***************************************/
		/* AST NODE TYPE = COMMAEXP (AST NODE) */
		/***************************************/
		System.out.print("AST NODE: COMMAEXP\n");

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
			"COMMA\nEXP\nLIST");

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
		return null;
	}


	public int length() {
		if(this.commaExpsList == null) {
			return 1;
		}
		return 1 + this.commaExpsList.length();
	}

}
	
