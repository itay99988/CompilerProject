package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import java.util.LinkedHashMap;


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
			Object value = getVarPrimitiveValue(t);

			return new TYPE_CLASS_DATA_MEMBER(((AST_VAR_DEC)this.dec).name, t, ((AST_VAR_DEC)this.dec).offset, value);

		}
		else if (this.dec instanceof AST_FUNCDEC)
		{
			TYPE t = ((AST_FUNCDEC)this.dec).SemantMe(inClass);

			//update vtable: map current function to this class
			AST_FUNCDEC funcDec = ((AST_FUNCDEC)this.dec);
			LinkedHashMap<String, VtableEntry> vt = SYMBOL_TABLE.getInstance().currClass.functionTable;
		    VtableEntry entry = vt.get(funcDec.name);
		    if(entry==null)
		    	vt.put(funcDec.name, new VtableEntry(vt.size(),inClass.className, funcDec.name));
		    else
		    	entry.definedInClass = inClass.className;
			
		    return new TYPE_CLASS_DATA_MEMBER(funcDec.name, t, funcDec.offset, null);
		}

		return null;
	}

	private Object getVarPrimitiveValue(TYPE var_type)
	{
		//in class we only allow initialize with primitive types
		AST_VAR_DEC varDec = (AST_VAR_DEC)this.dec;

		if( ((AST_VAR_DEC_EXP)varDec).assign.exp == null)
			return null;
		else
		{
			AST_VAR_DEC_EXP varDecExp = (AST_VAR_DEC_EXP)varDec;
		
			if(var_type instanceof TYPE_INT)
			{
				AST_EXP_INT intExp = (AST_EXP_INT)varDecExp.assign.exp;
				if(intExp == null) {
					return null;
				}
				return intExp.fullInt.GetValue();
			}
			if(var_type instanceof TYPE_STRING)
			{
				return ((AST_EXP_STRING)varDecExp.assign.exp).str;
			}
			return null;
		}
	}

}
	
