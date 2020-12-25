package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import MIPS.*;

public abstract class AST_VAR_DEC extends AST_DEC {

    public String name;
	public String type;
	public AST_EXP exp;

	public boolean isGlobal;
    public boolean isClassMember;
	public int offset;

    public AST_VAR_DEC(String type, String name, AST_EXP exp) {
        this.type = type;
		this.name = name;
		this.exp = exp;
    }

    
    public String toString() {
        return "varDec";
    }
	
	public String GetType()
	{
		return type;
	}
	
	public String GetName()
	{
		return name;
	}
	
	public TYPE SemantMe(AST_CLASSDEC inClass) throws SemantException {
		TYPE t;
		int lineNumber = this.getLineNumber();
		/****************************/
		/* [1] Check If Type exists */
		/****************************/
		t = SYMBOL_TABLE.getInstance().find(type, EntryCategory.Type);
		
		if (t == null) {
			String err = String.format("var_dec: '%s %s': type '%s' cannot be found.\n", this.type, this.name, this.type);
			throw new SemantException(lineNumber, err);
		}
		if(t == TYPE_VOID.getInstance()) {
			String err = String.format("var_dec: '%s %s', can't use type void.\n", this.type, this.name);
			throw new SemantException(lineNumber, err);
		}
		
		/**************************************/
		/* [2] Check that var name is legal   */
		/**************************************/
		checkVarName(inClass);

		/***************************************************/
		/* [3] Enter the Variable Type to the Symbol Table */
		/***************************************************/
		EntryCategory cat = (inClass==null) ? EntryCategory.Obj : EntryCategory.ClassMember;

		if (t.isArray()) {
			SYMBOL_TABLE.getInstance().enter(this.name, new TYPE_ARRAY(this.type, 0), cat);
		}
		else {
			SYMBOL_TABLE.getInstance().enter(this.name, t, cat);
		}

		/******************/
		/* [4] Semant exp */
		/******************/
		if(this.exp != null) {
			this.exp.SemantMe();
		}

		/**************************************/
		/* [5] add info to be used later on   */
		/**************************************/

        this.isGlobal = SYMBOL_TABLE.getInstance().isGlobal(this.name);
        this.offset = SYMBOL_TABLE.getInstance().getOffset(this.name);
        this.isClassMember= (inClass != null);

		/*********************/
		/* [6] Return type t */
		/*********************/

		return t;
	}



	void checkVarName(AST_CLASSDEC inClass) throws SemantException 
	{
    	if(SYMBOL_TABLE.getInstance().find(this.name, EntryCategory.Type) != null){
			String err = String.format("var_dec: var name '%s' already exists as a type.\n", this.name);
			throw new SemantException(this.getLineNumber(), err);
    	}
		if (SYMBOL_TABLE.getInstance().existInScope(this.name)) { //var name exists in scope
			String err = String.format("var_dec: variable name '%s' already exists in the current scope.\n", this.name);
			throw new SemantException(this.getLineNumber(), err);
        }
        if(inClass != null){
        	if(inClass.superClassName == null) //this class doesn't extend another class => var name is unique
        		return;
        	checkNameInSuperClasses(inClass); //check if var name exist in super class
        }
	}
	

	void checkNameInSuperClasses(AST_CLASSDEC inClass) throws SemantException 
	{
    	TYPE_CLASS superClass = (TYPE_CLASS)SYMBOL_TABLE.getInstance().find(inClass.superClassName, EntryCategory.Type);
    	
    	while (superClass != null) {
        	TYPE_CLASS_DATA_MEMBER_LIST cfieldTypes = superClass.data_members;
            while (cfieldTypes != null) {
                if (this.name.equals(cfieldTypes.head.name)) { 
					//cfieldTypes.head.name is either a function or a variable - doesn't matter!
					String err = String.format("var_dec: can't use var name '%s' in class '%s', as name already exists in super class '%s'.\n",
									this.name, inClass.className, superClass.name);
					throw new SemantException(this.getLineNumber(), err);
                }
                cfieldTypes = cfieldTypes.tail;
            }
            superClass = superClass.father;
        }
    }

	public abstract void MIPSme();

}
