package AST;

public class AST_FUNCDEC extends AST_DEC {

	public String type;
	public String name;
	public String argType;
	public String argName;
	public AST_IDSCOMMA commaIdsList;
	public AST_STMT_LIST stmtList;


	public AST_FUNCDEC(String type, String name) {
		this.type = type;
		this.name = name;
	}

	
	public AST_FUNCDEC(String type, String name, String argType, String argName, AST_IDSCOMMA commaIdsList, AST_STMT_LIST stmtList){
		this(type, name);
		this.argType = argType;
		this.argName = argName;
		this.commaIdsList = commaIdsList;
		this.stmtList = stmtList;

		System.out.format("====================== funcDec -> ID( %s ) ID( %s ) LPAREN ID( %s ) ID( %s ) commaIdsLst RPAREN LBRACE stmtList RBRACE\n", type, name, argType, argName);
	}
	

	public AST_FUNCDEC(String type, String name, AST_STMT_LIST stmtList){
		this(type, name);
		this.stmtList = stmtList;
		
		System.out.format("====================== funcDec -> ID( %s ) ID( %s ) LPAREN RPAREN LBRACE stmtList RBRACE\n", type, name);
	}
	

	public void PrintMe(){
		/***************************************/
		/* AST NODE TYPE = FUNC DEC (AST NODE) */
		/***************************************/
		System.out.print("AST NODE: FUNC DEC\n");

		/***************************************************/
		/* RECURSIVELY PRINT commaIdsList and stmtList ... */
		/***************************************************/
		if (commaIdsList != null) commaIdsList.PrintMe();
		if (stmtList != null) stmtList.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"FUNC\nDEC");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if(commaIdsList != null) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, commaIdsList.SerialNumber);
		}
		if(stmtList != null) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, stmtList.SerialNumber);
		}
	}

	public String toString() {
		return "funcDec";
	}
}
	
