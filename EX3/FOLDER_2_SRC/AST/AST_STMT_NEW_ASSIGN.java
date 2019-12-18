package AST;

public class AST_STMT_NEW_ASSIGN  extends AST_STMT {
	
    public AST_VAR var;
    public AST_NEW_EXP newExp;

	
	public AST_STMT_NEW_ASSIGN(AST_VAR var, AST_NEW_EXP newExp) {
        this.var = var;
        this.newExp = newExp;
        
		System.out.print("====================== stmt -> var ASSIGN newExp SEMICOLON\n");
	}
	
	
	public void PrintMe() {
		/**********************************************/
		/* AST NODE TYPE = STMT NEW ASSIGN (AST NODE) */
		/**********************************************/
		System.out.print("AST NODE: STMT RETURN\n");

		/****************************************/
		/* RECURSIVELY PRINT var and newExp ... */
		/****************************************/
        if (var != null) var.PrintMe();
        if (newExp != null) newExp.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"STMT\nNEW EXP");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, newExp.SerialNumber);
	}


}
