package AST;

public class AST_VAR_DEC_NEW_EXP extends AST_VAR_DEC {
	
	public AST_VAR_DEC_NEW_EXP(String type, String name, AST_NEW_EXP newExp, int lineNumber)
	{
		super(type, name, newExp);

		this.setLineNumber(lineNumber);
		System.out.format("====================== varDec -> ID( %s ) ID( %s ) ASSIGN newExp SEMICOLON\n", type, name);
	}

	
	public void PrintMe() {
		/**********************************************/
		/* AST NODE TYPE = VAR DEC NEW EXP (AST NODE) */
		/**********************************************/
		System.out.print("AST NODE: VAR DEC NEW EXP\n");

		/*****************************/
		/* RECURSIVELY PRINT exp ... */
		/*****************************/
		if (this.exp != null) this.exp.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"VAR DEC\nNEW EXP");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, this.exp.SerialNumber);

	}
}
	
