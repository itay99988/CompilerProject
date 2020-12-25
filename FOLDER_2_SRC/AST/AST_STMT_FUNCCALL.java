package AST;

import TYPES.*;
import TEMP.*;
import MIPS.sir_MIPS_a_lot;


public class AST_STMT_FUNCCALL  extends AST_STMT {
	
    public AST_FUNC_CALL funcCall;

	
	public AST_STMT_FUNCCALL(AST_FUNC_CALL funcCall, int lineNumber) {
        this.funcCall = funcCall;
        
		this.setLineNumber(lineNumber);
		System.out.print("====================== stmt -> funcCall SEMICOLON\n");
	}
	
	
	public void PrintMe() {
		/********************************************/
		/* AST NODE TYPE = STMT FUNCCALL (AST NODE) */
		/********************************************/
		System.out.print("AST NODE: STMT FUNCCALL\n");

		/**********************************/
		/* RECURSIVELY PRINT funcCall ... */
		/**********************************/
        if (funcCall != null) funcCall.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"STMT\nFUNC\nCALL");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, funcCall.SerialNumber);
	}
	
	//todo go to symbol table and return type of function from symbol table
	public TYPE SemantMe(TYPE expectedReturnType) throws SemantException {
		return funcCall.SemantMe();
	}


	public void MIPSme() {
		sir_MIPS_a_lot mips = sir_MIPS_a_lot.getInstance();
		if (funcCall.funcNameStr.equals("PrintInt")) {
			if(funcCall.funcClassName == null) {
				TEMP intTemp = funcCall.args.exp.MIPSme();
				mips.printInt(intTemp);
				return;
			}
		}
		if (funcCall.funcNameStr.equals("PrintString")) {
			if(funcCall.funcClassName == null) {
				TEMP strTemp = funcCall.args.exp.MIPSme();
				mips.printString(strTemp);
				return;
			}
		}

		funcCall.MIPSme();
	}

}
