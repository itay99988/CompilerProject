package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public abstract class AST_VAR_DEC extends AST_DEC{

    public String name;
    public String type;

    public AST_VAR_DEC(String type, String name) {
        this.type = type;
        this.name = name;
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
		t = SYMBOL_TABLE.getInstance().find(type, EntryCategory.Type);
		System.out.println("ast_ var_dec");
		if (t == null)
		{
			//System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,type);
			throw new SemantException(this.getLineNumber(), "TYPE is not found in symbol table");
			
		}
		
		/**************************************/
		/* [2] Check That Name does NOT exist */
		/**************************************/
		if (SYMBOL_TABLE.getInstance().find(name, EntryCategory.Type) != null)
		{
			//System.out.format(">> ERROR [%d:%d] variable %s already exists in scope\n",2,2,name);	
			throw new SemantException(this.getLineNumber(), "name already exists in symbol table");
			
		}

		/***************************************************/
		/* [3] Enter the Variable Type to the Symbol Table */
		/***************************************************/
		SYMBOL_TABLE.getInstance().enter(name,t, EntryCategory.Obj);

		/*********************************************************/
		/* [4] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;
	}
}
