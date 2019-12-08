package AST;

public class AST_CFIELD_LIST extends AST_DEC {

	public AST_CFIELD clsField;
	public AST_CFIELD_LIST clsFieldList;
	

	public AST_CFIELD_LIST(AST_CFIELD clsField) {
		this.clsField = clsField;

		System.out.print("====================== cFieldList -> cField\n");
	}


	public AST_CFIELD_LIST(AST_CFIELD clsField, AST_CFIELD_LIST clsFieldList) {
		this.clsField = clsField;
		this.clsFieldList = clsFieldList;

		System.out.print("====================== cFieldList -> cField cFieldList\n");
	}
	

	public void printMe() {
		/******************************************/
		/* AST NODE TYPE = CFIELD LIST (AST NODE) */
		/******************************************/
		System.out.print("AST NODE: CFIELD LIST\n");

		/*************************************/
		/* RECURSIVELY PRINT commaIdsLst ... */
		/**************************************/
		if (clsField != null) clsField.PrintMe();
		if (clsFieldList != null) clsFieldList.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"CFIELD\nLIST");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, clsField.SerialNumber);
		if(clsFieldList != null) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, clsFieldList.SerialNumber);
		}
	}


}
	
