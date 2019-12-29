package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_IDSCOMMA extends AST_DEC {

	public String type;
	public String name;
	public AST_IDSCOMMA commaIdsLst;
	
	public AST_IDSCOMMA(String type, String name, AST_IDSCOMMA commaIdsLst, int lineNumber) {
		this.type = type;
		this.name = name;
		this.commaIdsLst = commaIdsLst;

		this.setLineNumber(lineNumber);
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
		if (commaIdsLst != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, commaIdsLst.SerialNumber);
	}
	
	public TYPE_LIST SemantMe() throws SemantException
	{
		if (this.commaIdsLst == null)
		{
			return new TYPE_LIST(this.CheckSingleArg(), null);
		}
		else
		{
			return new TYPE_LIST( this.CheckSingleArg(), this.commaIdsLst.SemantMe() );
		}
	}

	public TYPE CheckSingleArg() throws SemantException
	{
		TYPE t = SYMBOL_TABLE.getInstance().find(type, EntryCategory.Type);
		if (t == null)
		{
			/**************************/
			/* ERROR: undeclared type */
			/**************************/
			String err = String.format(">> ERROR arg: %s %s, type doesn't exist\n", this.type, this.name);
            throw new SemantException(this.getLineNumber(), err);
		}
		else
		{
			/*******************************************************/
			/* Enter var with name=name and type=t to symbol table */
			/*******************************************************/
			SYMBOL_TABLE.getInstance().enter(name, t, EntryCategory.Obj);
			
		}

		/****************************/
		/* return (existing) type t */
		/****************************/
		return t;
	} 
	
}
	
