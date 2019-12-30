package AST;


import TYPES.*;
import SYMBOL_TABLE.*;

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

	public TYPE SemantMe() throws SemantException
	{
		TYPE t = SYMBOL_TABLE.getInstance().find(this.type, EntryCategory.Type);
		if(t == null){
		    String err = String.format("new_exp: class '%s' is not in SYMBOL_TABLE.\n", this.type);
		    throw new SemantException(this.getLineNumber(), err);
		}
		if(t.isClass())
		    return t;
		String err = String.format("new_exp: new expression can't be followed by '%s' (it's not a class or an array).\n", this.type);
		throw new SemantException(this.getLineNumber(), err);
	}

}
	
