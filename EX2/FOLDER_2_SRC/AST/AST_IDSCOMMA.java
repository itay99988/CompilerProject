package AST;

public class AST_IDSCOMMA extends AST_DEC {

	public String type;
	public String name;
	public AST_IDSCOMMA commaIdsLst;
	
	public AST_IDSCOMMA(String type, String name, AST_IDSCOMMA commaIdsLst) {
		this.type = type;
		this.name = name;
		this.commaIdsLst = commaIdsLst;

		System.out.format("====================== commaIdsLst -> COMMA ID( %s ) ID( %s ) commaIdsLst\n", type, name);
	}


	public void PrintMe() {
		/********************************************/
		/* AST NODE TYPE = STMT IDSCOMMA (AST NODE) */
		/********************************************/
		System.out.print("AST NODE: IDSCOMMA\n");

		/*************************************/
		/* RECURSIVELY PRINT commaIdsLst ... */
		/**************************************/
		if (commaIdsLst != null) commaIdsLst.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"COMMA\nIDS\nLST");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, commaIdsLst.SerialNumber);
	}
	
}
	
