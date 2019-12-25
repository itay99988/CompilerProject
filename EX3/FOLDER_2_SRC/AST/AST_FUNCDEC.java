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


	public AST_FUNCDEC(String type, String name, int lineNumber) {
		this.type = type;
		this.name = name;
		this.setLineNumber(lineNumber);
	}

	
	public AST_FUNCDEC(String type, String name, String argType, String argName, AST_IDSCOMMA commaIdsList, AST_STMT_LIST stmtList, int lineNumber){
		this(type, name, lineNumber);
		this.argType = argType;
		this.argName = argName;
		this.commaIdsList = commaIdsList;
		this.stmtList = stmtList;

		System.out.format("====================== funcDec -> ID( %s ) ID( %s ) LPAREN ID( %s ) ID( %s ) commaIdsLst RPAREN LBRACE stmtList RBRACE\n", type, name, argType, argName);
	}
	

	public AST_FUNCDEC(String type, String name, AST_STMT_LIST stmtList, int lineNumber){
		this(type, name, lineNumber);
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
	
	public TYPE SemantMe(AST_CLASSDEC inClass) throws SemantException
	{
		TYPE t;
		TYPE returnType = null;
		TYPE_LIST type_list = null;
		
		System.out.println("semantme ast_funcdec");
		
		/*******************/
		/* [0] return type */
		/*******************/
		/* Check function type */
		returnType = SYMBOL_TABLE.getInstance().find(type);
		if (returnType == null)
		{
			throw new SemantException(this.getLineNumber(), "TYPE is not found in symbol table");			
		}
	
		/****************************/
		/* [1] Begin Function Scope */
		/****************************/
		SYMBOL_TABLE.getInstance().beginScope();

		/***************************/
		/* [2] Semant Input Params */
		/***************************/
		
		/* Check if function type is equal to function return type */
		t = stmtList.SemantMe();
		
		if (t != returnType)
		{
			throw new SemantException(this.getLineNumber(), "Function return type is wrong");
		}
		
		if (argType != null)
		{
			TYPE t2 = SYMBOL_TABLE.getInstance().find(argType);
			if (t2 == null)
			{
				throw new SemantException(this.getLineNumber(), "function member TYPE is not in symbol table");			
			}
			/*else // TODO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			{
				type_list = new TYPE_LIST(t,type_list);
				SYMBOL_TABLE.getInstance().enter(it.head.name,t);
			}*/
		}
		
		if (commaIdsList != null)
			commaIdsList.SemantMe();

		/*****************/
		/* [4] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();

		//TODO: cound args number
		int paramsLen = 0;

		/***************************************************/
		/* [5] Enter the Function Type to the Symbol Table */
		/***************************************************/
		//TODO: change null to class name
		SYMBOL_TABLE.getInstance().enter(name,new TYPE_FUNCTION(returnType,name,type_list,paramsLen,null));

		/*********************************************************/
		/* [6] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;		
	}
	
}
	
