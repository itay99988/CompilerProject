package AST;

public class AST_ARR_DEC extends AST_DEC {
	
	String name;
	String type;
	
	public AST_ARR_DEC(String name, String type){
		this.name = name;
		this.type = type;

		System.out.format("====================== arrayDec -> ARRAY ID( %s ) EQ ID( %s ) LBRACK RBRACK", name, type);
	}
	
	public void PrintMe(){
		/****************************************/
		/* AST NODE TYPE = ARRAY DEC (AST NODE) */
		/****************************************/
		System.out.print("AST NODE: ARRAY DEC\n");
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"ARR\nDEC");
	}

}
	
