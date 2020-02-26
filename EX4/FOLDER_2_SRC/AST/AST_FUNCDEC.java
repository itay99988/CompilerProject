package AST;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import MIPS.*;


public class AST_FUNCDEC extends AST_DEC {

	public String type;
	public String name;
	public AST_IDSCOMMA params;
	public AST_STMT_LIST body;
	
	private int localsNum;
	public int offset;
	public String className = null;

	public AST_FUNCDEC(String type, String name, int lineNumber) {
		this.type = type;
		this.name = name;
		this.setLineNumber(lineNumber);
	}

	public AST_FUNCDEC(String type, String name, String firstParamType, String firstParamName, AST_IDSCOMMA otherParams,
			AST_STMT_LIST body, int lineNumber) {
		this(type, name, lineNumber);
		this.params = new AST_IDSCOMMA(firstParamType, firstParamName, otherParams, lineNumber);
		this.body = body;

		System.out.format(
				"====================== funcDec -> ID( %s ) ID( %s ) LPAREN ID( %s ) ID( %s ) commaIdsLst RPAREN LBRACE body RBRACE\n",
				type, name, firstParamType, firstParamName);
	}

	public AST_FUNCDEC(String type, String name, AST_STMT_LIST body, int lineNumber) {
		this(type, name, lineNumber);
		this.body = body;

		System.out.format("====================== funcDec -> ID( %s ) ID( %s ) LPAREN RPAREN LBRACE body RBRACE\n",
				type, name);
	}

	public void PrintMe() {
		/***************************************/
		/* AST NODE TYPE = FUNC DEC (AST NODE) */
		/***************************************/
		System.out.print("AST NODE: FUNC DEC\n");

		/****************************************/
		/* RECURSIVELY PRINT params and body .. */
		/****************************************/
		if (params != null)
			params.PrintMe();
		if (body != null)
			body.PrintMe();

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "FUNC\nDEC");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (params != null) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, params.SerialNumber);
		}
		if (body != null) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, body.SerialNumber);
		}
	}

	public String toString() {
		return "funcDec";
	}

	public String GetType() {
		return type;
	}

	public String GetName() {
		return name;
	}


	public TYPE SemantMe(AST_CLASSDEC inClass) throws SemantException 
	{
        TYPE_FUNCTION typeFunction = SemantFunctionPrototype(inClass);

        /****************************/
        /* [1] Begin Function Scope */
        /****************************/
        SYMBOL_TABLE.getInstance().beginScope(true);

		//check that all parameter names are unique:
		SemantFunctionArguments();

        /*******************/
        /* [2] Semant Body */
		/*******************/
		body.SemantMe(typeFunction.returnType);

		/********************************************/
        /* [3] Count local vars number for IRme use */
		/********************************************/
        this.localsNum = SYMBOL_TABLE.getInstance().getVarCount();
        this.offset = SYMBOL_TABLE.getInstance().getOffset(this.name);

		if(inClass!=null)
        	this.className = inClass.className;

        /*****************/
        /* [4] End Scope */
        /*****************/
		SYMBOL_TABLE.getInstance().endScope(true);
		
        /****************************/
        /* [5] Return function type */
        /****************************/
        return typeFunction;
	}


	/**
	 * enter all parameters to the function's scope,
	 * and make sure that there are no duplications
	 */
	public void SemantFunctionArguments() throws SemantException {
		TYPE t;
        for (AST_IDSCOMMA param = this.params; param != null; param = param.commaIdsLst) {
            t = SYMBOL_TABLE.getInstance().find(param.type, EntryCategory.Type);
            boolean paramNameExists = SYMBOL_TABLE.getInstance().existInScope(param.name);
            if (paramNameExists) {
				String err = String.format("func_dec: in function '%s': two or more parameters share the same name: '%s'.\n", this.name, param.name);
				throw new SemantException(this.getLineNumber(), err);	
			}
			if(t.isArray()) {
				t = new TYPE_ARRAY(param.type);
			}
            SYMBOL_TABLE.getInstance().enter(param.name, t, EntryCategory.Argument);
        }
    }


	public TYPE_FUNCTION SemantFunctionPrototype(AST_CLASSDEC inClass) throws SemantException {
        TYPE t;
        TYPE returnType = null;
		TYPE_LIST paramTypeList = new TYPE_LIST(null, null);
		int lineNumber = this.getLineNumber();
        /*******************/
        /* [1] return type */
		/*******************/
        returnType = SYMBOL_TABLE.getInstance().find(this.type, EntryCategory.Type);
        if (returnType == null) {
			String err = String.format("func_dec: non existing return type: '%s'\n", this.type);
            throw new SemantException(lineNumber, err);
        }
        if(returnType.isArray()){
        	 returnType = new TYPE_ARRAY(this.type);
        }

        /********************************/
        /* [2] Semant Input Param Types */
		/********************************/
        int paramsLen = 0;
        for (AST_IDSCOMMA param = this.params; param != null; param = param.commaIdsLst) {
            t = SYMBOL_TABLE.getInstance().find(param.type, EntryCategory.Type);
            if (t == null) {
				String err = String.format("func_dec: non existing parameter type: '%s'.\n", param.type);
				throw new SemantException(lineNumber, err);	
            }
            if(t == TYPE_VOID.getInstance()){
				String err = "func_dec: can't use type void for argument.\n";
				throw new SemantException(lineNumber, err);	
            }
            else{ // t isn't null nor void
                if (t.isArray()){
                    paramTypeList = new TYPE_LIST(new TYPE_ARRAY(param.type), paramTypeList);
                }
                else{
                    paramTypeList = new TYPE_LIST(t, paramTypeList);
                }
            paramsLen++;
           }
		}
		
        /*************************************************/
        /* [3] Make sure that the function name is legal */
		/*************************************************/
		isLegalFunctionName(inClass, returnType, paramTypeList, paramsLen);
		
        /*************************************/
        /* [4] Create function type instance */
		/*************************************/
        String className = null;
        if(inClass!=null) {
			className = inClass.className;
		}
        TYPE_FUNCTION type_func = new TYPE_FUNCTION(returnType, this.name, paramTypeList, paramsLen, className);

        /***************************************************/
        /* [5] Enter the Function Type to the Symbol Table */
		/***************************************************/
        SYMBOL_TABLE.getInstance().enter(name, type_func, EntryCategory.Obj);
        return type_func;
    }
	

    public boolean isLegalFunctionName(AST_CLASSDEC inClass, 
                                       TYPE returnType,
                                       TYPE_LIST paramTypeList,
                                       int paramsLen) throws SemantException {
		int lineNumber = this.getLineNumber();
        TYPE isObj = SYMBOL_TABLE.getInstance().find(this.name, EntryCategory.Obj);
        TYPE isType = SYMBOL_TABLE.getInstance().find(this.name, EntryCategory.Type);
        if (isType != null) {
			String err = String.format("func_dec: function name '%s' is already taken by another class or array.\n", this.name);
			throw new SemantException(lineNumber, err);
		}
		
        if (inClass == null) {
            if (isObj == null) { //function name is unique
                return true;
			}
			else {
				String err = String.format("func_dec: function name '%s' is already taken by another function or variable.\n", this.name);
				throw new SemantException(lineNumber, err);
            }
		}
		
        if (SYMBOL_TABLE.getInstance().existInScope(this.name)) {
			String err = String.format("func_dec: function name '%s' was already declared in this class.\n", this.name);
			throw new SemantException(lineNumber, err);
        }
		else if (inClass.superClassName == null) { // class doesn't extend another class
            return true;
		}
		
		// search function name in super classes
        TYPE_CLASS superClass = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(inClass.superClassName, EntryCategory.Type);
        while (superClass != null) {
        	TYPE_CLASS_DATA_MEMBER_LIST cfieldTypes = superClass.data_members;
            while (cfieldTypes != null) {
                if (this.name.equals(cfieldTypes.head.name)) {
                    if (cfieldTypes.head.type instanceof TYPE_FUNCTION) {
                        TYPE_FUNCTION superFunction = (TYPE_FUNCTION) cfieldTypes.head.type;

						//check that both functions have the same return type and input parameters
						if(superFunction.returnType.name.equals(returnType.name) &&
							typeListsMatch(paramTypeList, paramsLen, superFunction.params, superFunction.paramsLen)) {
								// method was overloaded -- legal
								cfieldTypes = cfieldTypes.tail;
								continue;
						}
						// else: shadowing
						if(!superFunction.returnType.name.equals(returnType.name)) {
							String err = String.format("func_dec: overrided method '%s' must have the same return type.", this.name);
							err += String.format("\nexpected return type: '%s'; got: '%s'.\n", superFunction.returnType.name, returnType.name);
							throw new SemantException(lineNumber, err);
						}
						else {
							String err = String.format("func_dec: overriding method '%s' in super class '%s': parameter types mismatch.\n",
											this.name, superFunction.returnType.name);
							throw new SemantException(lineNumber, err);
						}
					}
					else {
						String err = String.format("func_dec: function name '%s' was already declared in super class '%s'", 
										this.name, superClass.name);
						err += " as a variable or an array.\n";
						throw new SemantException(lineNumber, err);
					}
				}
				cfieldTypes = cfieldTypes.tail;
            }
            superClass = superClass.father;
        }
        return true;
	}
	

    public boolean typeListsMatch(TYPE_LIST tl1, int tl1_len, TYPE_LIST tl2, int tl2_len) 
	{
        if (tl1_len != tl2_len) 
		{
            return false;
        }
        // loop over type lists and make sure that all names match
        while (tl1.head != null) 
		{
            if (!tl1.head.name.equals(tl2.head.name)) 
			{
                return false;
            }
            tl1 = tl1.tail;
            tl2 = tl2.tail;
        }
        return true;
    }

	public void MIPSme() 
	{
		sir_MIPS_a_lot mips = sir_MIPS_a_lot.getInstance();
		mips.label(sir_MIPS_a_lot.getFunctionLabel(this.className, this.name));
		mips.prologue(this.localsNum);
		body.MIPSme();
		mips.epilogue();
	}
}
