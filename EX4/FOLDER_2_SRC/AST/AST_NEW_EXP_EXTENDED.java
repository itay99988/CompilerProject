package AST;

import TYPES.*;
import MIPS.*;
import SYMBOL_TABLE.*;
import TEMP.*;

public class AST_NEW_EXP_EXTENDED extends AST_NEW_EXP
{
	AST_EXP size;
	
	public AST_NEW_EXP_EXTENDED (String type, AST_EXP size, int lineNumber){
		super(type, lineNumber);
		this.size = size;

		this.setLineNumber(lineNumber);
		System.out.format("====================== newExpExtended -> NEW ID( %s ) LBRACK exp RBRACK\n", type);
	}
	

	public void PrintMe() {
		/******************************************/
		/* AST NODE TYPE = VAR DEC EXP (AST NODE) */
		/******************************************/
		System.out.print("AST NODE: NEW EXP EXTENDED\n");

		/*****************************/
		/* RECURSIVELY PRINT exp ... */
		/*****************************/
		if (size != null) size.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"NEW\nEXP\nEXTENDED");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, size.SerialNumber);
	}

	public TYPE SemantMe() throws SemantException
	{
		TYPE t = SYMBOL_TABLE.getInstance().find(this.type, EntryCategory.Type);
		if(t == null)
		{
		    String err = String.format("new_exp_extended: type '%s' for array init doesn't exist in SYMBOL_TABLE.\n", this.type);
		    throw new SemantException(this.getLineNumber(),err);
		}

		TYPE expType = size.SemantMe();
		if(expType == TYPE_INT.getInstance())
			return new TYPE_ARRAY(this.type, 0);

		//array wasn't defined properly
		String err = String.format("new_exp_extended: array allocation must be with integral size.\n");
		throw new SemantException(this.getLineNumber(),err);
	}

	public TEMP MIPSme() 
	{
		sir_MIPS_a_lot mips = sir_MIPS_a_lot.getInstance();
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP(); //arr address on heap
		TEMP sizeInBytes = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP arrayLen = this.size.MIPSme();

		mips.li(sizeInBytes, 4); //one element is sizeInBytes*4
		mips.mul(sizeInBytes, sizeInBytes, arrayLen);
		mips.addi(sizeInBytes, sizeInBytes, 4); //add 4 byts to store the array length
		mips.allocateOnHeap(dst, sizeInBytes);
		mips.store(dst, arrayLen, 0);

		return dst; 
	}

}
	
