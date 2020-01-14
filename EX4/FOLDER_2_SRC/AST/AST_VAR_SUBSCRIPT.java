package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;

public class AST_VAR_SUBSCRIPT extends AST_VAR
{
	public AST_VAR var;
	public AST_EXP subscript;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SUBSCRIPT(AST_VAR var,AST_EXP subscript, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== var -> var LBRACK exp RBRACK\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.subscript = subscript;

		this.setLineNumber(lineNumber);
	}

	/*****************************************************/
	/* The printing message for a subscript var AST node */
	/*****************************************************/
	public void PrintMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE: SUBSCRIPT VAR\n");

		/****************************************/
		/* RECURSIVELY PRINT VAR + SUBSRIPT ... */
		/****************************************/
		if (var != null) var.PrintMe();
		if (subscript != null) subscript.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"SUBSCRIPT\nVAR\nleft[right]");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var       != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (subscript != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,subscript.SerialNumber);
	}

		
	public TYPE SemantMe() throws SemantException
	{
		TYPE t = var.SemantMe(); //should be TYPE_ARRAY

		if(t == null || !t.isArray())
		{
			String err = "var_subscript: the var is not an array.\n";
			throw new SemantException(this.getLineNumber(),err);
		}

		TYPE_ARRAY typeArray = (TYPE_ARRAY)t;
		if(subscript.SemantMe() != TYPE_INT.getInstance())
		{
			String err = "var_subscript: expression inside '[]' is not an integer.\n";
			throw new SemantException(this.getLineNumber(),err);
		}

		TYPE arrayType = SYMBOL_TABLE.getInstance().find(typeArray.name, EntryCategory.Type);
		TYPE elementTypeArray = SYMBOL_TABLE.getInstance().find(arrayType.name, EntryCategory.Type);


		if(elementTypeArray instanceof TYPE_INT || elementTypeArray instanceof TYPE_STRING || 
				elementTypeArray instanceof TYPE_CLASS)
			return elementTypeArray;

		return arrayType;
	}
	
	public TEMP IRme()
	{
		return null;
	}
	
}
