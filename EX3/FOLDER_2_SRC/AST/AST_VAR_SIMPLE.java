package AST;
import TYPES.*;
import SYMBOL_TABLE.*;


public class AST_VAR_SIMPLE extends AST_VAR
{
	/************************/
	/* simple variable name */
	/************************/
	public String name;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SIMPLE(String name, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> ID( %s )\n",name);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name = name;

		this.setLineNumber(lineNumber);
	}

	/**************************************************/
	/* The printing message for a simple var AST node */
	/**************************************************/
	public void PrintMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST SIMPLE VAR */
		/**********************************/
		System.out.format("AST NODE: SIMPLE VAR( %s )\n",name);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("SIMPLE\nVAR\n( %s )",name));
	}

	public TYPE SemantMe() throws SemantException
	{
		TYPE_CLASS inClass = SYMBOL_TABLE.getInstance().currClass;
		
		if(inClass == null)
		{ //find var in current class or in global scope.
			TYPE t = SYMBOL_TABLE.getInstance().find(this.name, EntryCategory.Obj);
			if(t != null)
				return t;
			else
			{
				String err = String.format("var_simple: '%s' doesn't exist in the SYMBOL_TABLE.\n", this.name);
				throw new SemantException(this.getLineNumber(), err);
			}
		}

		//var is used inside a class
		TYPE t = findNameInSuperClass(inClass); //var may be in a current or super class
		if(t != null)
			return t;
		return SYMBOL_TABLE.getInstance().find(this.name, EntryCategory.Obj); //return from global scope
	}

	public TYPE findNameInSuperClass(TYPE_CLASS inClass)
	{
		TYPE_CLASS currClass = inClass;
    	
    	while (currClass != null) 
		{
        	TYPE_CLASS_DATA_MEMBER_LIST cfieldTypes = currClass.data_members;
            while (cfieldTypes != null) 
			{
                if (this.name.equals(cfieldTypes.head.name))
                	return cfieldTypes.head.type;
                cfieldTypes = cfieldTypes.tail;
            }
            currClass = currClass.father;
        }
    	return null;
	}
}
