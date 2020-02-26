package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;

public class AST_STMT_NEW_ASSIGN  extends AST_STMT {
	
    public AST_VAR var;
    public AST_NEW_EXP newExp;

	
	public AST_STMT_NEW_ASSIGN(AST_VAR var, AST_NEW_EXP newExp, int lineNumber) {
        this.var = var;
        this.newExp = newExp;

		this.setLineNumber(lineNumber);
		System.out.print("====================== stmt -> var ASSIGN newExp SEMICOLON\n");
	}
	
	
	public void PrintMe() {
		/**********************************************/
		/* AST NODE TYPE = STMT NEW ASSIGN (AST NODE) */
		/**********************************************/
		System.out.print("AST NODE: STMT RETURN\n");

		/****************************************/
		/* RECURSIVELY PRINT var and newExp ... */
		/****************************************/
        if (var != null) var.PrintMe();
        if (newExp != null) newExp.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"STMT\nNEW EXP");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, newExp.SerialNumber);
	}

	public TYPE SemantMe(TYPE returnedTypeExpected) throws SemantException
	{
		TYPE expressionType;
		TYPE varType = this.var.SemantMe();
		if (varType == null)
		{
			String err = "stmt_new_assign: var doesn't exist.\n";
			throw new SemantException(this.getLineNumber(), err);
		}

		expressionType = this.newExp.SemantMe();
		
		if(varType.isArray())
		{	//array gets special treatment here
			if(!expressionType.isArray())
			{
				String err = String.format("stmt_new_assign: '%s': new exp type is not an array.\n", newExp.type);
				throw new SemantException(this.getLineNumber(), err);
			}
		}

		if (expressionType == null)
		{
			String err = "stmt_new_assign: exp type doesn't exist\n";
			throw new SemantException(this.getLineNumber(), err);
		}
		if (AST_STMT_ASSIGN.isValidAssignment(varType, expressionType, this.getLineNumber()))
		{
			System.out.println(varType.name + "," + expressionType.name);
			return null;
		}

		else 
		{
			String err = "stmt_new_assign: assignment is illegal!\n";
			throw new SemantException(this.getLineNumber(), err);
		}
	}

	public void MIPSme() 
	{
		String dst = this.var.getMipsRepr();
		TEMP src = (this.newExp != null) ? this.newExp.MIPSme() : null;
		this.var.setMipsValue(dst, src);
	}

}
