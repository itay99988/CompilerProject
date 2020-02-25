package AST;

import TYPES.*;
import TEMP.*;

public class AST_STMT_RETURN  extends AST_STMT {
	
    public AST_EXP exp;

	
	public AST_STMT_RETURN(AST_EXP exp, int lineNumber) {
        this.exp = exp;
        
        String expStr = exp == null ? "" : " exp";

		this.setLineNumber(lineNumber);
		System.out.format("====================== stmt -> return%s SEMICOLON\n", expStr);
	}
	
	
	public void PrintMe()  {
		/******************************************/
		/* AST NODE TYPE = STMT RETURN (AST NODE) */
		/******************************************/
		System.out.print("AST NODE: STMT RETURN\n");

		/*****************************/
		/* RECURSIVELY PRINT exp ... */
		/*****************************/
		if (exp != null) exp.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"STMT\nRETURN");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if(exp != null) {
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
        }
	}
	
	
	public TYPE SemantMe(TYPE returnedTypeExpected) throws SemantException
	{
		if (exp != null)
		{
			TYPE expType = exp.SemantMe();
			try {
				AST_STMT_ASSIGN.isValidAssignment(returnedTypeExpected, expType, this.getLineNumber());
			}
			catch(SemantException e) {
				String err = "stmt_return: etrun expression type doesn't match the expected return type.";
				err += String.format("\nexpected: '%s'; got: '%s'.\n", returnedTypeExpected.name, expType.name);
				throw new SemantException(this.getLineNumber(), err);
			}
		}
		else
		{
			if(returnedTypeExpected !=  TYPE_VOID.getInstance()) 
			{
            		String err = "stmt_return: empty return expression with non-void expected return type.\n";
            		throw new SemantException(this.getLineNumber(), err);
        	}
		}
		return null;
	}

}