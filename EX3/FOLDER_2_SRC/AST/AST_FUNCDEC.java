package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_FUNCDEC extends AST_DEC {

	public String type;
	public String name;
	public String firstArgType;
	public String firstargName;
	public AST_IDSCOMMA otherArgs;
	public AST_STMT_LIST body;

	public AST_FUNCDEC(String type, String name, int lineNumber) {
		this.type = type;
		this.name = name;
		this.setLineNumber(lineNumber);
	}

	public AST_FUNCDEC(String type, String name, String firstArgType, String firstargName, AST_IDSCOMMA otherArgs,
			AST_STMT_LIST body, int lineNumber) {
		this(type, name, lineNumber);
		this.firstArgType = firstArgType;
		this.firstargName = firstargName;
		this.otherArgs = otherArgs;
		this.body = body;

		System.out.format(
				"====================== funcDec -> ID( %s ) ID( %s ) LPAREN ID( %s ) ID( %s ) commaIdsLst RPAREN LBRACE body RBRACE\n",
				type, name, firstArgType, firstargName);
	}

	public AST_FUNCDEC(String type, String name, AST_STMT_LIST body, int lineNumber) {
		this(type, name, lineNumber);
		this.body = body;

		System.out.format("====================== funcDec -> ID( %s ) ID( %s ) LPAREN RPAREN LBRACE body RBRACE\n",
				type, name);
	}

	public void PrintMe() {
		/***************************************/
		/* AST NODE TYPE = FUNC DEC (AST NODE) */
		/***************************************/
		System.out.print("AST NODE: FUNC DEC\n");

		/***************************************************/
		/* RECURSIVELY PRINT otherArgs and body ... */
		/***************************************************/
		if (otherArgs != null)
			otherArgs.PrintMe();
		if (body != null)
			body.PrintMe();

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "FUNC\nDEC");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (otherArgs != null) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, otherArgs.SerialNumber);
		}
		if (body != null) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, body.SerialNumber);
		}
	}

	public String toString() {
		return "funcDec";
	}

	public String GetType() {
		return type;
	}

	public String GetName() {
		return name;
	}

	public TYPE SemantMe(AST_CLASSDEC inClass) throws SemantException {
		TYPE t;
		TYPE returnType = null;
		TYPE_LIST type_list = null;

		System.out.println("semantme ast_funcdec");

		/*******************/
		/* [0] return type */
		/*******************/
		/* Check function type */
		returnType = SYMBOL_TABLE.getInstance().find(type, EntryCategory.Type);
		if (returnType == null) {
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
		body.SemantMe(returnType);

		if (firstArgType != null) {
			TYPE t2 = SYMBOL_TABLE.getInstance().find(firstArgType, EntryCategory.Type);
			if (t2 == null) {
				throw new SemantException(this.getLineNumber(), "function member TYPE is not in symbol table");
			}
			/*
			 * else // TODO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! { type_list =
			 * new TYPE_LIST(t,type_list); SYMBOL_TABLE.getInstance().enter(it.head.name,t);
			 * }
			 */
		}

		if (otherArgs != null)
			otherArgs.SemantMe();

		/*****************/
		/* [4] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();

		// TODO: cound otherArgs number
		int paramsLen = 0;

		/***************************************************/
		/* [5] Enter the Function Type to the Symbol Table */
		/***************************************************/
		//TODO: change null to class name
		SYMBOL_TABLE.getInstance().enter(name,new TYPE_FUNCTION(returnType,name,type_list,paramsLen,null), EntryCategory.Obj);

		/*********************************************************/
		/* [6] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;
	}

}
