package AST;

public class AST_NEW_EXP extends AST_Node
{
	String type;
	
	public AST_NEW_EXP (String type){
		this.type = type;

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
	
