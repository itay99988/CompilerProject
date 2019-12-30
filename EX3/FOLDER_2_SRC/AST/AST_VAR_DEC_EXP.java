package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VAR_DEC_EXP extends AST_VAR_DEC {

	public AST_STMT_ASSIGN assign;
	
	public AST_VAR_DEC_EXP(String type, String name, AST_EXP exp, int lineNumber){
		super(type, name, exp);
		this.assign = new AST_STMT_ASSIGN(new AST_VAR_SIMPLE(name, lineNumber), exp, lineNumber);

		String expStr = exp == null ? "" : " exp";

		this.setLineNumber(lineNumber);
		System.out.format("====================== varDec -> ID( %s ) ID( %s ) ASSIGN%s SEMICOLON\n", type, name, expStr);
	}
	

	public void PrintMe() {
		/******************************************/
		/* AST NODE TYPE = VAR DEC EXP (AST NODE) */
		/******************************************/
		System.out.print("AST NODE: VAR DEC EXP\n");

		/*****************************/
		/* RECURSIVELY PRINT exp ... */
		/*****************************/
		if (exp != null) exp.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"VAR DEC\nEXP");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if(exp != null) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
		}
	}

	
	public TYPE SemantMe(AST_CLASSDEC inClass) throws SemantException {
		TYPE t = super.SemantMe(inClass);

		if(exp != null) {
			if(inClass != null) {
				//allow only constant values
				if(!(assign.exp instanceof AST_EXP_INT ||
					 assign.exp instanceof AST_EXP_STRING ||
				 	 assign.exp instanceof AST_EXP_NIL)) {
					String err = "var_dec_exp: cannot use non-constant assignments inside class.\n";
					throw new SemantException(this.getLineNumber(), err);
				}
			}
			assign.SemantMe(null);
		}
		
		return t;
	}

}
	
