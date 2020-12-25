package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import MIPS.*;

public class AST_VAR_DEC_NEW_EXP extends AST_VAR_DEC {

	public AST_STMT_NEW_ASSIGN newAssign;


	public AST_VAR_DEC_NEW_EXP(String type, String name, AST_NEW_EXP newExp, int lineNumber) {
		super(type, name, newExp);
		this.newAssign = new AST_STMT_NEW_ASSIGN(new AST_VAR_SIMPLE(name, lineNumber), newExp, lineNumber);

		this.setLineNumber(lineNumber);
		System.out.format("====================== varDec -> ID( %s ) ID( %s ) ASSIGN newExp SEMICOLON\n", type, name);
	}

	
	public void PrintMe() {
		/**********************************************/
		/* AST NODE TYPE = VAR DEC NEW EXP (AST NODE) */
		/**********************************************/
		System.out.print("AST NODE: VAR DEC NEW EXP\n");

		/*****************************/
		/* RECURSIVELY PRINT exp ... */
		/*****************************/
		if (this.exp != null) this.exp.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"VAR DEC\nNEW EXP");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, this.exp.SerialNumber);
	}


	public TYPE SemantMe(AST_CLASSDEC inClass) throws SemantException 
	{
		TYPE t = super.SemantMe(inClass);
		SYMBOL_TABLE symbol_table = SYMBOL_TABLE.getInstance();
		
		if(newAssign != null) {
			if(inClass != null) {
				// allow only constant values
				String err = "var_dec_new_exp: cannot use new assignments inside class.\n";
				throw new SemantException(this.getLineNumber(), err);
			}
			newAssign.SemantMe(null);
		}

		return t;
	}

	public void MIPSme() 
	{
		sir_MIPS_a_lot mips = sir_MIPS_a_lot.getInstance();
		if (this.isGlobal) 
		{
			mips.allocateWord(this.name);
			mips.writeInit();

			if(newAssign != null)
				newAssign.MIPSme();

			mips.writeText();
		} 
		else 
		{
			if(newAssign != null)
				newAssign.MIPSme();
		}
	}
	
}
	
