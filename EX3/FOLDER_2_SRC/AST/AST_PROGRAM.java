package AST;

import TYPES.*;

public class AST_PROGRAM extends AST_Node {
	
	AST_DEC_LIST l;
	
	public AST_PROGRAM (AST_DEC_LIST decList) {
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


}
