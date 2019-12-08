package AST;

public class AST_CFIELD extends AST_DEC {

	AST_DEC dec;
	
	public AST_CFIELD(AST_DEC dec) {
		this.dec = dec;
		System.out.format("====================== cField -> %s\n", dec);
	}
	
	public void PrintMe() {
		/*************************************/
		/* AST NODE TYPE = CFIELD (AST NODE) */
		/*************************************/
		System.out.print("AST NODE: CFIELD\n");

		/*************************************/
		/* RECURSIVELY PRINT commaIdsLst ... */
		/**************************************/
		if (dec != null) dec.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"CFIELD");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, dec.SerialNumber);
	}

}
	
