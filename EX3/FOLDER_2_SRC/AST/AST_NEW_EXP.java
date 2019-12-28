package AST;

public class AST_NEW_EXP extends AST_EXP
{
	String type;
	
	public AST_NEW_EXP (String type, int lineNumber){
		this.type = type;

		this.setLineNumber(lineNumber);
		System.out.format("====================== newExp -> NEW ID( %s )\n", type);
	}
	
	public void PrintMe() {
		/**************************************/
		/* AST NODE TYPE = NEW EXP (AST NODE) */
		/**************************************/
		System.out.print("AST NODE: VAR DEC EXP\n");
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"NEW\nEXP");
	}

}
	
