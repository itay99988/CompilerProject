package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;

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

	public TYPE_CLASS_DATA_MEMBER SemantMe(AST_CLASSDEC inClass) throws SemantException
	{
		if (this.dec instanceof AST_VAR_DEC)
		{
			TYPE t = ((AST_VAR_DEC)this.dec).SemantMe(inClass);
			return new TYPE_CLASS_DATA_MEMBER(((AST_VAR_DEC)this.dec).name, t);
		}
		else if (this.dec instanceof AST_FUNCDEC)
		{
			TYPE t = ((AST_FUNCDEC)this.dec).SemantMe(inClass);
			return new TYPE_CLASS_DATA_MEMBER(((AST_FUNCDEC)this.dec).name, t);
		}
		return null;
	}

	public TEMP IRme()
	{
		return null;
	}

}
	
