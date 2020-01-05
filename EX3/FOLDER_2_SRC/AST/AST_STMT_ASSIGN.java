package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_ASSIGN extends AST_STMT
{
	/***************/
	/*  var := exp */
	/***************/
	public AST_VAR var;
	public AST_EXP exp;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_ASSIGN(AST_VAR var,AST_EXP exp, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> var ASSIGN exp SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.exp = exp;

		this.setLineNumber(lineNumber);
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST NODE: ASSIGN STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (var != null) var.PrintMe();
		if (exp != null) exp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"ASSIGN\nleft := right\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}
	
	public TYPE SemantMe(TYPE returnedTypeExpected) throws SemantException
	{
		TYPE expressionType = null;
		TYPE varType = this.var.SemantMe();
		if (varType == null)
		{
			String err = "stmt_assign: var doesn't exist.\n";
			throw new SemantException(this.getLineNumber(), err);
		}

		if (this.exp != null)
		{
			expressionType = this.exp.SemantMe();
		}
			

		if (expressionType == null)
		{
			String err = "stmt_assign: exp type doesn't exist.\n";
			throw new SemantException(this.getLineNumber(), err);
		}
		if (isValidAssignment(varType, expressionType, this.getLineNumber()))
			return null;
		else 
		{
			String err = "stmt_assign: assignment is illegal!\n";
			throw new SemantException(this.getLineNumber(), err);
		}
	}

	public static boolean isValidAssignment(TYPE t1, TYPE t2, int lineNumber) throws SemantException
	{
	    if(t1 == null || t2 == null)
	        return false;
        if(t1.name.equals(t2.name))
		{
			if( !( t2.isArray() && !t1.isArray() ) )
				return true;
			else
			{
				    String err = String.format("stmt_assign: cannot assign array to non array type.\n");
        			throw new SemantException(lineNumber, err);
			}
		} 
            
        if(t1.isClass())
		{
            TYPE_CLASS father = (TYPE_CLASS)t1;

            if(t2.isClass()) { //(Father) var := (Son) exp
                TYPE_CLASS son = (TYPE_CLASS) t2;
                if (son.isDerivedFrom(father))
                    return true;
                else {
                    String err = String.format("stmt_assign: class '%s' is not derived from class '%s'.\n", son.name, father.name);
        			throw new SemantException(lineNumber, err);
                }
            }
            //t2 is not a class
            if(t2 == TYPE_NIL.getInstance()) //Father FatherClassInstance := NIL
                return true;
            else{
				String err = String.format("stmt_assign: an instance of class '%s' can only be assigned ", father.name);
				err += String.format("with an instance of the same class (or a derived class), or with nil.\n");
    			throw new SemantException(lineNumber, err);
            }
        }
        //t1 is not a class
        if(t1.isArray())
		{
            TYPE_ARRAY varArrayType = (TYPE_ARRAY) t1;

            if (t2 == TYPE_NIL.getInstance()) //(SomeTypeArray) var := NIL
                return true;
            if(t2.isArray()) {
                TYPE_ARRAY expArrayType = (TYPE_ARRAY) t2;
                if (varArrayType.name.equals(expArrayType.name))
                    return true;
                else {
                  TYPE arrayType = SYMBOL_TABLE.getInstance().find(varArrayType.name, EntryCategory.Type); //IntArray type (=int)
       	    	  if(!arrayType.name.equals(expArrayType.name)){
       	    		String err = String.format("stmt_assign: '%s' (type: '%s[]'), new '%s[]'," +
       	    									" array types don't match.\n", varArrayType.name, arrayType.name, expArrayType.name);
       		  		throw new SemantException(lineNumber, err);
       	    	  }
                  return true;
                }
            }
            //t1 is Array, t2 is not Array nor NIL
            else {
                String err = String.format("stmt_assign: TYPE_ARRAY '%s' can only be assigned with a new '%s[<expression>]' or NIL.\n",
                        					varArrayType.name, varArrayType.name);
    			throw new SemantException(lineNumber,err);
            }
        }
        //t1 is not a class or an array, i.e t1 is a primitive type
        else{
            String err = String.format("stmt_assign: can't assign exp of type %s to var of type '%s'.\n", t2.name, t1.name);
			throw new SemantException(lineNumber, err);
        }
    }
}
