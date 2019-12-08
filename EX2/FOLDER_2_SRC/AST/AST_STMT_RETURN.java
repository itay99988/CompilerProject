package AST;

public class AST_STMT_RETURN  extends AST_STMT {
	
    public AST_EXP exp;

	
	public AST_STMT_RETURN(AST_EXP exp) {
        this.exp = exp;
        
        String expStr = exp == null ? "" : " exp";
		System.out.format("====================== stmt -> return%s SEMICOLON\n", expStr);
	}
	
	
	public void PrintMe() {
		/******************************************/
		/* AST NODE TYPE = STMT RETURN (AST NODE) */
		/******************************************/
		System.out.print("AST NODE: STMT RETURN\n");

		/*****************************/
		/* RECURSIVELY PRINT exp ... */
		/*****************************/
		if (exp != null) exp.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"STMT\nRETURN");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if(exp != null) {
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
        }
	}


}
