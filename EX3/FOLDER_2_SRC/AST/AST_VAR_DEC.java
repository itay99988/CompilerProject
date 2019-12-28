package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public abstract class AST_VAR_DEC extends AST_DEC {

    public String name;
	public String type;
	public AST_EXP exp;

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
	
	public TYPE SemantMe(AST_CLASSDEC inClass) throws SemantException
	{
		TYPE t;
		
		/****************************/
		/* [1] Check If Type exists */
		/****************************/
		t = SYMBOL_TABLE.getInstance().find(type);
		System.out.println("ast_ var_dec");
		if (t == null)
		{
			//System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,type);
			throw new SemantException(this.getLineNumber(), "TYPE is not found in symbol table");
			
		}
		
		/**************************************/
		/* [2] Check that var name is legal   */
		/**************************************/

		checkVarName(inClass);

		/***************************************************/
		/* [3] Enter the Function Type to the Symbol Table */
		/***************************************************/
		SYMBOL_TABLE.getInstance().enter(name,t);

		/*********************************************************/
		/* [4] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;
	}


	void checkVarName(AST_CLASSDEC inClass) throws SemantException {
    	if(SYMBOL_TABLE.getInstance().find(this.name, EntryKind.TypeEntry) != null){
			throw new SemantException(this.getLineNumber(), String.format("var_dec: var name '%s' already exists as a type", this.name));
    	}
    	if (SYMBOL_TABLE.getInstance().existInScope(this.name)) { //var name exists in scope
			throw new SemantException(this.getLineNumber(), String.format("var_dec: var name '%s' already exists in the current scope", this.name));
        }
        if(inClass != null){
        	if(inClass.extendingClassName == null) //this class doesn't extend another class => var name is unique
        		return;
        	checkNameInSuperClasses(inClass); //check if var name exist in super class
        }
	}
	

	void checkNameInSuperClasses(AST_CLASSDEC inClass) throws SemantException {
    	TYPE_CLASS superClass = (TYPE_CLASS)SYMBOL_TABLE.getInstance().find(inClass.extendingClassName, EntryKind.TypeEntry);
    	
    	while (superClass != null) {
        	TYPE_CLASS_DATA_MEMBER_LIST cfieldTypes = superClass.data_members;
            while (cfieldTypes != null) {
                if (this.name.equals(cfieldTypes.head.name)) { 
                	//cfieldTypes.head.name is either a function or a variable - doesn't matter!
					throw new SemantException(this.getLineNumber(), 
						String.format("var_dec: can't use var name '%s' in class '%s', as name already exists in super class '%s'",
									  this.name, inClass.className, superClass.name));
                }
                cfieldTypes = cfieldTypes.tail;
            }
            superClass = superClass.father;
        }
    }

}
