package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import MIPS.*;

public class AST_VAR_SIMPLE extends AST_VAR
{
	/************************/
	/* simple variable name */
	/************************/
	public String name;

	private boolean isGlobal;
	private boolean isClassMember;
	private int localVarIdx;
	
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
			{
				this.isGlobal = SYMBOL_TABLE.getInstance().isGlobal(this.name);
				this.localVarIdx = SYMBOL_TABLE.getInstance().getOffset(this.name);
				this.isClassMember=false;

				return t;
			}
			else
			{
				String err = String.format("var_simple: '%s' doesn't exist in the SYMBOL_TABLE.\n", this.name);
				throw new SemantException(this.getLineNumber(), err);
			}
		}

		//var is used inside a class
		SYMBOL_TABLE symbol = SYMBOL_TABLE.getInstance();
		TYPE typeInScope=null, typeClassMember;
		TYPE varType = symbol.find(this.name, EntryCategory.Obj);
		typeClassMember = findNameInSuperClass(inClass);
		isGlobal = symbol.isGlobal(this.name);

		
		if(varType == null)
		{
			//var doesn't exist in a function or as a class member of the current class or as global var.
			if(typeClassMember == null)
			{
				String err = String.format(">> ERROR '%s' doesn't exist in SYMBOL_TABLE", this.name);
				throw new SemantException(this.getLineNumber(), err);
			}
			//var exist in super class!
			this.isClassMember = true;
			return typeClassMember;
		}
		//var exist in a method, as a class member of the current class or as a global var.
		if(isGlobal)
		{
			if(typeClassMember != null)
			{
				//prefer class member of super class over the global var
				isClassMember = true;
				return typeClassMember;
			}
			this.isClassMember = false;
			this.localVarIdx = symbol.getOffset(name);
			return varType;
		}
		//var exist in a method or as a class member
		this.localVarIdx = symbol.getOffset(name);
		this.isClassMember = symbol.isClassMember(name);
		return varType;
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

	public TEMP getMipsValue() 
	{
		TEMP_FACTORY factory = TEMP_FACTORY.getInstance();
		TEMP dst = factory.getFreshTEMP();
		sir_MIPS_a_lot.getInstance().load(dst, getMipsRepr());
		return dst;
	}

	public void setMipsValue(String dst, TEMP src) 
	{
		sir_MIPS_a_lot.getInstance().store(dst, src);
	}

	public String getMipsRepr() 
	{
		sir_MIPS_a_lot mips = sir_MIPS_a_lot.getInstance();
				
		if (this.isGlobal) 
		{
			return String.format("global_%s", this.name);
		} 
		else 
		{
			if(this.isClassMember)
			{
				TEMP object = TEMP_FACTORY.getInstance().getFreshTEMP();
				mips.load(object,"12($fp)");
				sir_MIPS_a_lot.getInstance().beqz(object, "_nullDereferenceError");
				return String.format("%d(%s)", this.localVarIdx*4, object);
			}
			int offset = -4 * this.localVarIdx;
			if (this.localVarIdx < 0)
			{ 
				offset += 12;
			}
			return String.format("%d($fp)", offset);
		}
	}
	
}
