package AST;

import TYPES.*;
import MIPS.sir_MIPS_a_lot;
import SYMBOL_TABLE.*;
import TEMP.*;

public class AST_FUNC_CALL extends AST_DEC {

	public String name;
	public AST_BRACESEXP args;
	public AST_VAR var;

	public String funcNameStr; 
    public String funcClassName;
    public int funcOffset;


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


    public TYPE SemantMe() throws SemantException 
	{
		int lineNumber = this.getLineNumber();
        TYPE t = checkFuncExists();
        if (t instanceof TYPE_FUNCTION == false) {
            String err = String.format("func_call: variable '%s' is not a function.\n", this.name);
			throw new SemantException(lineNumber, err);	
		}
		
		TYPE_FUNCTION typeFunction = (TYPE_FUNCTION) t;
        TYPE_LIST funcParamList = typeFunction.params; //a TYPE_LIST of the function argumants' type
		TYPE_LIST argTypeList = new TYPE_LIST(null, null);
		int argListLen = args == null? 0 : args.length();
		
		if (typeFunction.paramsLen < argListLen) {
			String err = String.format("func_call: too many arguments when calling function '%s'\n", this.name);
            throw new SemantException(lineNumber, err);
        }
        if (typeFunction.paramsLen > argListLen) {
			String err = String.format("func_call: too few arguments when calling function '%s'.\n", this.name);
            throw new SemantException(lineNumber, err);
        }

		//update info for next stages (MIPS code of func call)
		this.funcNameStr = typeFunction.name;
        this.funcClassName = typeFunction.className;
        if(funcClassName!=null)
        	this.funcOffset = ((TYPE_CLASS)SYMBOL_TABLE.getInstance().find(funcClassName, EntryCategory.Type)).functionTable.get(funcNameStr).offset;
        else
        	this.funcOffset = SYMBOL_TABLE.getInstance().getOffset(funcNameStr);
		//

		if(this.args == null) 
		{
			return typeFunction.returnType;
		}

		this.args.SemantMe();

		AST_COMMAEXP otherExps = this.args.commaExpsList;
		TYPE currExpType = args.exp.SemantMe();
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
				String err = String.format("func_call: can't call function '%s', argument #%d type doesn't match.",
								this.name, argIndex);
				err += String.format("\nexpected: '%s'; got: '%s'.\n", funcParamList.head, argTypeList.head);
				throw new SemantException(lineNumber, err);
            }
            argTypeList = argTypeList.tail;
            funcParamList = funcParamList.tail;
            argIndex--;
        }

        // all parameter types match the argument types
        return typeFunction.returnType;
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


	public void MIPSme() {
		sir_MIPS_a_lot mips = sir_MIPS_a_lot.getInstance();

		mips.storeTempsInStack();

		int numOfArgs = args == null? 0 : args.length();

		mips.decRegister("$sp", 4*numOfArgs);
		pushArgumentsToStack();

		// get function name
		TEMP funcName = TEMP_FACTORY.getInstance().getFreshTEMP();
		String name = mips.allocateString("\"" + funcNameStr + "\"");
		mips.loadAddress(funcName, name);

		
		if(funcClassName == null) {
			mips.push(TEMP_FACTORY.zero);
			mips.push(funcName);
			mips.jal(MIPS.sir_MIPS_a_lot.getFunctionLabel(null, this.funcNameStr));
		}
		else { // the function is a class method
			methodMIPSme(funcName);
		}
		// the function name + object were added
		numOfArgs += 2;

		// after function call
		mips.popArgs(numOfArgs);
		mips.loadTempsFromStack();
	}


	public void pushArgumentsToStack() {
		if (this.args == null) {
			return;
		}
		TEMP tmp = this.args.exp.MIPSme();
		sir_MIPS_a_lot.getInstance().store("$sp", tmp, 0);
		pushArgumentsToStack(this.args.commaExpsList, 1);
	}


	private void pushArgumentsToStack(AST_COMMAEXP args, int argumentOffset) {
		if (args == null) {
			return;
		}
		TEMP tmp = args.exp.MIPSme();
		sir_MIPS_a_lot.getInstance().store("$sp", tmp, 4*argumentOffset);
		pushArgumentsToStack(args.commaExpsList, argumentOffset+1);
	}


	private void methodMIPSme(TEMP funcNameTemp) {
		sir_MIPS_a_lot mips = sir_MIPS_a_lot.getInstance();
		
		if(this.var == null) {
			// the caller is a method inside the class.
			// we need to load the vtable, assuming the object
			// is the first argument of the caller
			TEMP object = TEMP_FACTORY.getInstance().getFreshTEMP();
			mips.load(object, "12($fp)");  //get the address of the object
			mips.beqz(object, "_nullDereferenceError");
			TEMP vtable = TEMP_FACTORY.getInstance().getFreshTEMP();
			mips.load(vtable, object, 0); //$vtable = address of object's vtable
			
			TEMP method = TEMP_FACTORY.getInstance().getFreshTEMP();
			mips.load(method, vtable, 4*this.funcOffset);
			mips.push(object);
			mips.push(funcNameTemp);
			mips.jal(method.toString());
			return;
		}
			
		// a class instance is calling the method from outside the class.
		// we need to load the object and then load the vtable
		AST_VAR objectVar ;
		if(this.var instanceof AST_VAR_FIELD) {				
			objectVar = ((AST_VAR_FIELD)var).var;
		}
		else { //funcName is instanceof AST_VAR_SUBSCRIPT
			objectVar = ((AST_VAR_SUBSCRIPT)var).var;
		}
		TEMP object = objectVar.getMipsValue(); //get the address of the object
		mips.beqz(object, "_nullDereferenceError");
		TEMP vtable = TEMP_FACTORY.getInstance().getFreshTEMP();
		mips.load(vtable, object, 0); //$vtable = address of object's vtable

		TEMP method = TEMP_FACTORY.getInstance().getFreshTEMP();
		mips.load(method, vtable, 4*funcOffset); //$function = address of funcName
		mips.push(object); //push object as the first argument of the callee
		mips.push(funcNameTemp);
		mips.jal(method.toString());

	}


}
	
