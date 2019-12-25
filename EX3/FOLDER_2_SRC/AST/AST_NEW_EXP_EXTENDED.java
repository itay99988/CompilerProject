package AST;

public class AST_NEW_EXP_EXTENDED extends AST_NEW_EXP
{
	AST_EXP size;
	
	public AST_NEW_EXP_EXTENDED (String type, AST_EXP size, int lineNumber){
		super(type, lineNumber);
		this.size = size;

		this.setLineNumber(lineNumber);
		System.out.format("====================== newExpExtended -> NEW ID( %s ) LBRACK exp RBRACK\n", type);
	}
	

	public void PrintMe() {
		/******************************************/
		/* AST NODE TYPE = VAR DEC EXP (AST NODE) */
		/******************************************/
		System.out.print("AST NODE: NEW EXP EXTENDED\n");

		/*****************************/
		/* RECURSIVELY PRINT exp ... */
		/*****************************/
		if (size != null) size.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"NEW\nEXP\nEXTENDED");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, size.SerialNumber);
	}

}
	
