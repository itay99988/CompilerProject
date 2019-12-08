package AST;

public class AST_CLASSDEC extends AST_DEC {
	
	public String className;
	public String extendingClassName;
	public AST_CFIELD_LIST cfieldList;



	public AST_CLASSDEC(String className, String extendingClassName, AST_CFIELD_LIST cfieldList) {
		this.className = className;
		this.extendingClassName = extendingClassName;
		this.cfieldList = cfieldList;
		
		String extStr = extendingClassName == null ? "" : String.format(" EXTENDS ( %s )", extendingClassName);
		System.out.format("====================== classDec -> CLASS ID( %s )%s LBRACE cFieldList RBRACE\n", className, extStr);
	}
	

	public void printMe(){
		/***************************************/
		/* AST NODE TYPE = CLASS DEC (AST NODE) */
		/***************************************/
		System.out.print("AST NODE: CLASS DEC\n");

		/***************************************/
		/* RECURSIVELY PRINT head and tail ... */
		/***************************************/
		if (cfieldList != null) cfieldList.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"CLASS\nDEC");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, cfieldList.SerialNumber);
	}


	public String toString() {
		return "classDec";
	}
}
	
