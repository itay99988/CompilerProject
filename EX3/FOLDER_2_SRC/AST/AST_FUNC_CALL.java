package AST;
import TYPES.*;
import SYMBOL_TABLE.*;


public class AST_FUNC_CALL extends AST_DEC {

	public String name;
	public AST_BRACESEXP braceExps;
	public AST_VAR var;


	public AST_FUNC_CALL(String name, AST_BRACESEXP braceExps, AST_VAR var, int lineNumber){
		this.name = name;
		this.braceExps = braceExps;
		this.var = var;

		String varString = var == null ? "" : "var DOT";

		this.setLineNumber(lineNumber);
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
	
	//todo go to symbol table and return type of function from symbol table
		public TYPE SemantMe()throws SemantException
	{
		TYPE t = SYMBOL_TABLE.getInstance().find(name,EntryCategory.Obj);
		if (t == null)
		{
			throw new SemantException(this.getLineNumber(), "function is not in symbol table");			
		}
		
		braceExps.SemantMe();

		return null;
	}

}
	
