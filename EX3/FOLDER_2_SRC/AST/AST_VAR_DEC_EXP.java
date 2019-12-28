package AST;

public class AST_VAR_DEC_EXP extends AST_VAR_DEC {
	
	public AST_VAR_DEC_EXP(String type, String name, AST_EXP exp, int lineNumber){
		super(type, name, exp);
		String expStr = exp == null ? "" : " exp";

		this.setLineNumber(lineNumber);
		System.out.format("====================== varDec -> ID( %s ) ID( %s ) ASSIGN%s SEMICOLON\n", type, name, expStr);
	}
	

	public void PrintMe() {
		/******************************************/
		/* AST NODE TYPE = VAR DEC EXP (AST NODE) */
		/******************************************/
		System.out.print("AST NODE: VAR DEC EXP\n");

		/*****************************/
		/* RECURSIVELY PRINT exp ... */
		/*****************************/
		if (exp != null) exp.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"VAR DEC\nEXP");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if(exp != null) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
		}
	}

}
	
