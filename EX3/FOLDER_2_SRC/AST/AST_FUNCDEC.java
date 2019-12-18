package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

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
	
	public String GetType()
	{
		return type;
	}
	
	public String GetName()
	{
		return name;
	}
	
	public TYPE SemantMe()
	{
		/*
		TYPE t;
		TYPE returnType = null;
		TYPE_LIST type_list = null;
		*/
		/*******************/
		/* [0] return type */
		/*******************/
		/*
		returnType = SYMBOL_TABLE.getInstance().find(returnTypeName);
		if (returnType == null)
		{
			System.out.format(">> ERROR [%d:%d] non existing return type %s\n",6,6,returnType);				
		}*/
	
		/****************************/
		/* [1] Begin Function Scope */
		/****************************/
		/*SYMBOL_TABLE.getInstance().beginScope();*/

		/***************************/
		/* [2] Semant Input Params */
		/***************************/
		/*for (AST_TYPE_NAME_LIST it = params; it  != null; it = it.tail)
		{
			t = SYMBOL_TABLE.getInstance().find(it.head.type);
			if (t == null)
			{
				System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,it.head.type);				
			}
			else
			{
				type_list = new TYPE_LIST(t,type_list);
				SYMBOL_TABLE.getInstance().enter(it.head.name,t);
			}
		}*/

		/*******************/
		/* [3] Semant Body */
		/*******************/
		/*body.SemantMe();*/

		/*****************/
		/* [4] End Scope */
		/*****************/
		/*SYMBOL_TABLE.getInstance().endScope();*/

		/***************************************************/
		/* [5] Enter the Function Type to the Symbol Table */
		/***************************************************/
		/*SYMBOL_TABLE.getInstance().enter(name,new TYPE_FUNCTION(returnType,name,type_list));*/

		/*********************************************************/
		/* [6] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;		
	}
	
}
	
