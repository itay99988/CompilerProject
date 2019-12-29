package AST;

import TYPES.*;

public class AST_CFIELD_LIST extends AST_Node {

	public AST_CFIELD clsField;
	public AST_CFIELD_LIST clsFieldList;
	

	public AST_CFIELD_LIST(AST_CFIELD clsField, int lineNumber) {
		this.clsField = clsField;

		this.setLineNumber(lineNumber);
		System.out.print("====================== cFieldList -> cField\n");
	}


	public AST_CFIELD_LIST(AST_CFIELD clsField, AST_CFIELD_LIST clsFieldList, int lineNumber) {
		this.clsField = clsField;
		this.clsFieldList = clsFieldList;

		this.setLineNumber(lineNumber);
		System.out.print("====================== cFieldList -> cField cFieldList\n");
	}
	

	public void PrintMe() {
		/******************************************/
		/* AST NODE TYPE = CFIELD LIST (AST NODE) */
		/******************************************/
		System.out.print("AST NODE: CFIELD LIST\n");

		/*************************************/
		/* RECURSIVELY PRINT commaIdsLst ... */
		/**************************************/
		if (clsField != null) clsField.PrintMe();
		if (clsFieldList != null) clsFieldList.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"CFIELD\nLIST");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, clsField.SerialNumber);
		if(clsFieldList != null) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, clsFieldList.SerialNumber);
		}
	}

	public TYPE_CLASS_DATA_MEMBER_LIST SemantMe(AST_CLASSDEC inClass) throws SemantException
	{
		if (clsFieldList == null)
		{
			return new TYPE_CLASS_DATA_MEMBER_LIST(
				clsField.SemantMe(inClass),
				null);
		}
		else
		{
			return new TYPE_CLASS_DATA_MEMBER_LIST(
				clsField.SemantMe(inClass),
				clsFieldList.SemantMe(inClass));
		}
	}

}
	
