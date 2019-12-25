package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_CFIELD extends AST_DEC {

	AST_DEC dec;
	
	public AST_CFIELD(AST_DEC dec, int lineNumber) {
		this.dec = dec;

		this.setLineNumber(lineNumber);
		System.out.format("====================== cField -> %s\n", dec);
	}
	
	public void PrintMe() {
		/*************************************/
		/* AST NODE TYPE = CFIELD (AST NODE) */
		/*************************************/
		System.out.print("AST NODE: CFIELD\n");

		/*************************************/
		/* RECURSIVELY PRINT commaIdsLst ... */
		/**************************************/
		if (dec != null) dec.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"CFIELD");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, dec.SerialNumber);
	}

	public TYPE_CLASS_DATA_MEMBER SemantMe() throws SemantException
	{
		String type = dec.GetType();
		String name = dec.GetName();
		
		TYPE t;
		
///		System.out.println("TEST AST_CFIELD SemantMe");
		
		/****************************/
		/* [1] Check If Type exists */
		/****************************/
	//	t = SYMBOL_TABLE.getInstance().find(type);
		//if (t == null)
	//	{
		//	System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,type);
		//	throw new SemantException(this.getLineNumber(), "TYPE is not found in symbol table");
			
	//	}
		
		/**************************************/
		/* [2] Check That Name does NOT exist */
		/**************************************/
		//if (SYMBOL_TABLE.getInstance().find(name) != null)
		//{
		//	System.out.format(">> ERROR [%d:%d] variable %s already exists in scope\n",2,2,name);	
		//	throw new SemantException(this.getLineNumber(), "name is already exist in symbol table");
			
		//}

		/***************************************************/
		/* [3] Enter the Function Type to the Symbol Table */
		/***************************************************/
		//SYMBOL_TABLE.getInstance().enter(name,t);

		/*********************************************************/
		/* [4] Return value is irrelevant for class declarations */
		/*********************************************************/
		
		dec.SemantMe();
		
		return null;
	}

}
	
