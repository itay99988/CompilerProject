package AST;

public class AST_FUNC_CALL extends AST_DEC {

	public String name;
	public AST_BRACESEXP braceExps;
	public AST_VAR var;


	public AST_FUNC_CALL(String name, AST_BRACESEXP braceExps, AST_VAR var){
		this.name = name;
		this.braceExps = braceExps;
		this.var = var;

		String varString = var == null ? "" : "var DOT";
		System.out.format("====================== funcCall ->%s ID( %s ) braceExps\n", varString, name);
	}
	

	public void PrintMe() {
		/****************************************/
		/* AST NODE TYPE = FUNC_CALL (AST NODE) */
		/****************************************/
		System.out.print("AST NODE: VAR DEC EXP\n");

		/******************************************/
		/* RECURSIVELY PRINT braceExps and var... */
		/******************************************/
		if (braceExps != null) braceExps.PrintMe();
		if (var != null) var.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"FUNC\nCALL");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if(braceExps != null) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, braceExps.SerialNumber);
		}
		if(var != null) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
		}

	}

}
	
