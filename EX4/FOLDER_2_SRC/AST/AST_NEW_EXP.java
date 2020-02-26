package AST;


import TYPES.*;
import MIPS.*;
import SYMBOL_TABLE.*;
import TEMP.*;

public class AST_NEW_EXP extends AST_EXP
{
	String type;
	
	public AST_NEW_EXP (String type, int lineNumber){
		this.type = type;

		this.setLineNumber(lineNumber);
		System.out.format("====================== newExp -> NEW ID( %s )\n", type);
	}
	
	public void PrintMe() {
		/**************************************/
		/* AST NODE TYPE = NEW EXP (AST NODE) */
		/**************************************/
		System.out.print("AST NODE: VAR DEC EXP\n");
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"NEW\nEXP");
	}

	public TYPE SemantMe() throws SemantException
	{
		TYPE t = SYMBOL_TABLE.getInstance().find(this.type, EntryCategory.Type);
		if(t == null){
		    String err = String.format("new_exp: class '%s' is not in SYMBOL_TABLE.\n", this.type);
		    throw new SemantException(this.getLineNumber(), err);
		}
		if(t.isClass())
		    return t;
		String err = String.format("new_exp: new expression can't be followed by '%s' (it's not a class or an array).\n", this.type);
		throw new SemantException(this.getLineNumber(), err);
	}

	public TEMP MIPSme() 
	{
		sir_MIPS_a_lot mips = sir_MIPS_a_lot.getInstance();
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP sizeInBytes = TEMP_FACTORY.getInstance().getFreshTEMP();
		
		TYPE_CLASS classType = (TYPE_CLASS)SYMBOL_TABLE.getInstance().find(this.type, EntryCategory.Type);
	
		//Memory allocation for expression
		mips.li(sizeInBytes, classType.ObjSizeInBytes);
		mips.allocateOnHeap(dst, sizeInBytes);

		//set Vtable
		TEMP vtable = TEMP_FACTORY.getInstance().getFreshTEMP();
		mips.loadAddress(vtable, "Vtable_"+this.type); //$vtable = address of class 'name' vtable
		mips.store(dst,vtable,0); //store $vtable in 0($dst)
		
		//initialize data members
		TEMP value = TEMP_FACTORY.getInstance().getFreshTEMP();
		MIPSInitAllClassMembers(classType, dst, value);
		
		return dst;
	}

	public void MIPSInitAllClassMembers(TYPE_CLASS classType, TEMP dst, TEMP value)
	{
		if(classType.father != null)
		{ //initialize super class members first
			MIPSInitAllClassMembers(classType.father, dst, value);
		}
		
		TEMP zero = TEMP_FACTORY.zero;
		sir_MIPS_a_lot mips = sir_MIPS_a_lot.getInstance();
		
		TYPE_CLASS_DATA_MEMBER_LIST memberList = classType.data_members;

		while(memberList != null && memberList.head != null)
		{
			TYPE_CLASS_DATA_MEMBER curr = memberList.head;
			if(curr.type instanceof TYPE_INT)
			{
				if(curr.value==null)
					curr.value = 0; //initialize integer to 0
				mips.li(value, Integer.parseInt(curr.value.toString()));
				mips.store(dst, value, curr.offset*4);
			}
			if(curr.type instanceof TYPE_STRING)
			{
				if(curr.value!=null){
					String label = mips.allocateString(curr.value.toString());
					mips.loadAddress(value, label);
					mips.store(dst, value, curr.offset*4);
				}
				else
					mips.store(dst,zero, curr.offset*4);
			}
			if(curr.type instanceof TYPE_CLASS || curr.type instanceof TYPE_ARRAY)
			{ 
				//arrays and classes may be initialized to null
				mips.store(dst, zero, curr.offset*4);
			}
			//else - it's a function or type void => do nothing
			memberList = memberList.tail;
		}
	}

}
	
