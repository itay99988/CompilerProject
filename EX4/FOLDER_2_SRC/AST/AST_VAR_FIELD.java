package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import MIPS.*;

public class AST_VAR_FIELD extends AST_VAR
{
	public AST_VAR var;
	public String fieldName;
	
	public boolean isGlobal = false;
	public int fieldIndex;
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_FIELD(AST_VAR var,String fieldName, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> var DOT ID( %s )\n",fieldName);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.fieldName = fieldName;

		this.setLineNumber(lineNumber);
	}

	/*************************************************/
	/* The printing message for a field var AST node */
	/*************************************************/
	public void PrintMe()
	{
		/*********************************/
		/* AST NODE TYPE = AST FIELD VAR */
		/*********************************/
		System.out.print("AST NODE: FIELD VAR\n");

		/**********************************************/
		/* RECURSIVELY PRINT VAR, then FIELD NAME ... */
		/**********************************************/
		if (var != null) var.PrintMe();
		System.out.format("FIELD NAME( %s )\n",fieldName);

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("FIELD\nVAR\n( %s )",fieldName));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
	}

	public TYPE SemantMe() throws SemantException
	{

		TYPE t = this.var.SemantMe(); //we expect TYPE_CLASS

		if(t == null || !t.isClass())
		{
			String err = String.format("var_field: variable '%s' is not a class object.\n", this.fieldName);
			throw new SemantException(this.getLineNumber(), err);
		}

		TYPE_CLASS typeClass = (TYPE_CLASS)SYMBOL_TABLE.getInstance().find(t.name, EntryCategory.Type);

		while(typeClass != null)
		{
			TYPE_CLASS_DATA_MEMBER_LIST memList = typeClass.data_members;
			while(memList != null)
			{
				if(memList.head.name.equals(this.fieldName))
				{
					this.fieldIndex = memList.head.offset;
					return memList.head.type;
				}

				memList = memList.tail;
			}
			typeClass = typeClass.father;
		}
		return null;
	}

	public TEMP getMipsValue() 
	{
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
		
		sir_MIPS_a_lot.getInstance().load(dst, this.getMipsRepr());
		return dst;
	}

	public void setMipsValue(String dst, TEMP src) 
	{
		sir_MIPS_a_lot.getInstance().store(dst, src);
	}

	public String getMipsRepr() 
	{
		TEMP baseAddr = this.var.getMipsValue();
		sir_MIPS_a_lot.getInstance().beqz(baseAddr, "_nullDereferenceError");
		return String.format("%d(%s)", 4*this.fieldIndex, baseAddr);
	}
	
}
