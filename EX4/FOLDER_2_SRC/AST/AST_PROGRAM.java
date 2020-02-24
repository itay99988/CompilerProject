package AST;

import TYPES.*;
import TEMP.*;
import IR.*;

public class AST_PROGRAM extends AST_Node {
	
	public AST_DEC_LIST l;

	public static boolean stringConcatUsed = false;
    public static boolean stringCompareUsed = false;
	
	public AST_PROGRAM (AST_DEC_LIST decList) 
	{
		System.out.print("====================== program -> decs\n");
		l = decList;
	}
	
	
	public void PrintMe() {
		/************************************/
		/* AST NODE TYPE = PROGRAM (AST NODE) */
		/************************************/
		System.out.print("AST NODE: PROGRAM\n");

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (l != null) l.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"PROGRAM");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, l.SerialNumber);

	}

	public TYPE SemantMe() throws SemantException {
		return l.SemantMe();
	}

	public TEMP IRme()
	{
		l.IRme();
		IR.getInstance().Add_IRcommand(new IRcommand_Label("end"));
		return null;
	}

}
