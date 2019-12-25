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
	
	public TYPE SemantMe() throws SemantException
	{
		TYPE t = SYMBOL_TABLE.getInstance().find(type);
		if (t == null)
		{
			throw new SemantException(this.getLineNumber(), "function member TYPE is not in symbol table");			
		}
		/*else // TODO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		{
			type_list = new TYPE_LIST(t,type_list);
			SYMBOL_TABLE.getInstance().enter(it.head.name,t);
		}*/		
		
		if (commaIdsLst != null)
			commaIdsLst.SemantMe();

		return null;
	}
	
}
	
