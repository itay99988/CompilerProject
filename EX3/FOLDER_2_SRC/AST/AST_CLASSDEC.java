package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_CLASSDEC extends AST_DEC {
	
	public String className;
	public String superClassName;
	public AST_CFIELD_LIST cfieldList;



	public AST_CLASSDEC(String className, String extendingClassName, AST_CFIELD_LIST cfieldList, int lineNumber) {
		this.className = className;
		this.superClassName = extendingClassName;
		this.cfieldList = cfieldList;
		
		String extStr = extendingClassName == null ? "" : String.format(" EXTENDS ( %s )", extendingClassName);

		this.setLineNumber(lineNumber);
		System.out.format("====================== classDec -> CLASS ID( %s )%s LBRACE cFieldList RBRACE\n", className, extStr);
	}
	

	public void PrintMe(){
		/***************************************/
		/* AST NODE TYPE = CLASS DEC (AST NODE) */
		/***************************************/
		System.out.print("AST NODE: CLASS DEC\n");

		/***************************************/
		/* RECURSIVELY PRINT head and tail ... */
		/***************************************/
		if (cfieldList != null) cfieldList.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"CLASS\nDEC");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, cfieldList.SerialNumber);
	}


	public String toString() {
		return "classDec";
	}
	
	public TYPE SemantMe() throws SemantException
	{	
	
		System.out.println("semant me ast_classDEC");
		/*************************/
		/* [1] Begin Class Scope */
		/*************************/
		SYMBOL_TABLE.getInstance().beginScope();

		/***************************/
		/* [2] Semant Data Members */
		/***************************/
		TYPE_CLASS t = new TYPE_CLASS(null,className,cfieldList.SemantMe(this));

		/*****************/
		/* [3] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();

		/************************************************/
		/* [4] Enter the Class Type to the Symbol Table */
		/************************************************/
		SYMBOL_TABLE.getInstance().enter(className,t, EntryCategory.Type);

		/*********************************************************/
		/* [5] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;		
	}	
}
	
