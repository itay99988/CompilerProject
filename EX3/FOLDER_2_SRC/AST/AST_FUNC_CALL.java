package AST;
import TYPES.*;
import SYMBOL_TABLE.*;


public class AST_FUNC_CALL extends AST_DEC {

	public String name;
	public AST_BRACESEXP args;
	public AST_VAR var;


	public AST_FUNC_CALL(String name, AST_BRACESEXP args, AST_VAR var, int lineNumber){
		this.name = name;
		this.args = args;
		this.var = var;

		String varString = var == null ? "" : "var DOT";

		this.setLineNumber(lineNumber);
		System.out.format("====================== funcCall ->%s ID( %s ) braceExps\n", varString, name);
	}
	

	public void PrintMe() {
		/****************************************/
		/* AST NODE TYPE = FUNC_CALL (AST NODE) */
		/****************************************/
		System.out.print("AST NODE: VAR DEC EXP\n");

		/***************************************/
		/* RECURSIVELY PRINT args and var... */
		/***************************************/
		if (args != null) args.PrintMe();
		if (var != null) var.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"FUNC\nCALL");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if(args != null) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, args.SerialNumber);
		}
		if(var != null) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
		}

	}


    public TYPE SemantMe() throws SemantException {
		int lineNumber = this.getLineNumber();
        TYPE t = checkFuncExists();
        if (t instanceof TYPE_FUNCTION == false) {
            String err = String.format("variable %s is not a function", this.name);
			throw new SemantException(lineNumber, err);	
		}
		
		this.args.SemantMe();

        TYPE_FUNCTION type_function = (TYPE_FUNCTION) t;
        TYPE_LIST funcParamList = type_function.params; //a TYPE_LIST of the function argumants' type
		TYPE_LIST argTypeList = new TYPE_LIST(null, null);

        TYPE currExpType;
		AST_COMMAEXP otherExps = this.args.commaExpsList;
		int argListLen = args.length();

		if (type_function.paramsLen < argListLen) {
			String err = String.format("ast_func_call: too many arguments when calling function %s", this.name);
            throw new SemantException(lineNumber, err);
        }
        if (type_function.paramsLen > argListLen) {
			String err = String.format("ast_func_call: too few arguments when calling function %s", this.name);
            throw new SemantException(lineNumber, err);
        }

		currExpType = args.exp.SemantMe();
		argTypeList = new TYPE_LIST(currExpType, argTypeList);
        while (otherExps != null) {
            currExpType = otherExps.exp.SemantMe();
            argTypeList = new TYPE_LIST(currExpType, argTypeList);
            otherExps = otherExps.commaExpsList;
        }

        // loop over paramTypeList and function argType and make sure that they match
        int argIndex = argListLen;
        while (argTypeList.head != null) {
            if (!AST_STMT_ASSIGN.isValidAssignment(funcParamList.head, argTypeList.head, lineNumber)) {
				String err = String.format("ast_func_call: can't call function %s, argument #%d type doesn't match.",
								this.name, argIndex);
				err += String.format("\nExpected: %s; got: %s.", funcParamList.head, argTypeList.head);
				throw new SemantException(lineNumber, err);
            }
            argTypeList = argTypeList.tail;
            funcParamList = funcParamList.tail;
            argIndex--;
        }

        // all parameter types match the argument types
        return type_function.returnType;
	}
	

    public TYPE checkFuncExists() throws SemantException {
    	if(this.var != null){
			AST_VAR_FIELD fieldFunc = new AST_VAR_FIELD(this.var, this.name, this.getLineNumber());
			return fieldFunc.SemantMe();
		}

		// else (this.var == null)

        TYPE t = SYMBOL_TABLE.getInstance().find(this.name, EntryCategory.Obj);
        if(t != null) {	// function name exists in current scope or a higher scope
			return t;
		}
    	TYPE_CLASS curClass = SYMBOL_TABLE.getInstance().currClass;
    	if(curClass == null) {
			// function name does not exist in any visible scope, and is not inside a class
			return null;
		}
		AST_VAR_SIMPLE funcVar = new AST_VAR_SIMPLE(this.name, this.getLineNumber());
    	return funcVar.findNameInSuperClass(curClass); //check if name exists in super class
    }

}
	
