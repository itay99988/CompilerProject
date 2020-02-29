package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import MIPS.*;

public class AST_EXP_BINOP extends AST_EXP 
{
	public AST_BINOP op;
	public AST_EXP left;
	public AST_EXP right;

	public boolean isStringConcat = false;
	public boolean isStringCompare = false;
	

	public AST_EXP_BINOP(AST_EXP left,AST_EXP right, AST_BINOP op, int lineNumber) {

		System.out.print("====================== exp -> exp BinOp exp\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.left = left;
		this.right = right;
		this.op = op;

		this.setLineNumber(lineNumber);
	}
	
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe() {
		/*********************************/
		/* AST NODE TYPE = AST EXP BINOP */
		/*********************************/
		System.out.format("AST NODE: EXP BINOP\n", op);

		/*********************************************/
		/* RECURSIVELY PRINT left, op, and right ... */
		/*********************************************/
		if (left != null) left.PrintMe();
		if(op != null) op.PrintMe();
		if (right != null) right.PrintMe();

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("EXP\nBINOP"));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, left.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, op.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, right.SerialNumber);
	}
	
	public TYPE SemantMe() throws SemantException
	{

		TYPE leftType = null;
		TYPE rightType = null;
		
		if (left  != null) leftType = left.SemantMe();
		if (right != null) rightType = right.SemantMe();
		
		if ((leftType == TYPE_INT.getInstance()) && (rightType == TYPE_INT.getInstance()))
		{
			return TYPE_INT.getInstance();
		}
		if ((leftType == TYPE_STRING.getInstance()) && (op.toString() == "PLUS") && (rightType == TYPE_STRING.getInstance()))
		{
			this.isStringConcat = true;
			return TYPE_STRING.getInstance();
		}

        if(op.toString() == "EQ"){
            if(leftType.isArray() && rightType.isArray() && leftType.name.equals(rightType.name))
                return TYPE_INT.getInstance();
            if(leftType == TYPE_STRING.getInstance() && rightType == TYPE_STRING.getInstance())
			{
				this.isStringCompare = true;
				return TYPE_INT.getInstance();
			}
                
            if(leftType == TYPE_NIL.getInstance() || rightType == TYPE_NIL.getInstance()){
                if(leftType.isArray() || rightType.isArray())
                    return TYPE_INT.getInstance();
                if(leftType.isClass() || rightType.isClass())
                    return TYPE_INT.getInstance();
            }

            if(leftType.isClass() && rightType.isClass()){
                if(isComparable((TYPE_CLASS) leftType, (TYPE_CLASS) rightType))
                    return TYPE_INT.getInstance();
            }
        }
		
        throw new SemantException(this.getLineNumber(), "exp_binop: binop: types don't match.\n");
	}


	public boolean isComparable(TYPE_CLASS t1, TYPE_CLASS t2)
	{
	    while(t1.father != null)
	        t1 = t1.father;
	    while(t2.father != null)
	        t2 = t2.father;
	    return t1.name.equals(t2.name);
    }


	public TEMP MIPSme() 
	{
		//generate the actual operators code 
		TEMP t1 = left.MIPSme();
		TEMP t2 = right.MIPSme();

		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
		sir_MIPS_a_lot mips = sir_MIPS_a_lot.getInstance();

		String opType = this.op.toString();
		//choose which code to generate according to the chosen operator
		if( opType.equals("PLUS") )
		{
			// +
			if(this.isStringConcat)
				this.stringConcatMIPSme(dst, t1, t2);
			else
				mips.add(dst, t1, t2);
		}
		else if( opType.equals("MINUS") )
		{
			// -
			mips.sub(dst, t1, t2);
		}
		else if( opType.equals("TIMES") )
		{
			// *
			mips.mul(dst, t1, t2);
		}
		else if( opType.equals("DIVIDE") )
		{
			// /
			mips.beqz(t2, "_divZeroError");
			mips.div(dst, t1, t2);
		}
		else if( opType.equals("LT") )
		{
			// <
			this.ltMIPSme(dst, t1, t2);
		}
		else if( opType.equals("GT") )
		{
			// >
			this.ltMIPSme(dst, t2, t1);
		}
		else if( opType.equals("EQ") )
		{
			// =
			if(this.isStringCompare)
				this.stringCompareMIPSme(dst, t1, t2);
			else
				this.eqMIPSme(dst, t1, t2);
		}

		 //if OP is add, sub, mul or div - we want to limit the integer result
		if( (opType.equals("PLUS") || opType.equals("MINUS") ||
			 opType.equals("TIMES") || opType.equals("DIVIDE")) && !this.isStringConcat )
		{
			limitIntMIPSme(dst);
		}
		return dst;
	}

	private void ltMIPSme(TEMP dst, TEMP t1, TEMP t2) 
	{
		/*******************************/
		/* [1] Allocate 2 fresh labels */
		/*******************************/
		String label_end        = sir_MIPS_a_lot.getFreshLabel("end");
		String label_AssignOne  = sir_MIPS_a_lot.getFreshLabel("AssignOne");
		String label_AssignZero = sir_MIPS_a_lot.getFreshLabel("AssignZero");
		
		/******************************************/
		/* [2] if (t1< t2) goto label_AssignOne;  */
		/*     if (t1>=t2) goto label_AssignZero; */
		/******************************************/
		sir_MIPS_a_lot.getInstance().blt(t1,t2,label_AssignOne);
		sir_MIPS_a_lot.getInstance().bge(t1,t2,label_AssignZero);

		/************************/
		/* [3] label_AssignOne: */
		/*                      */
		/*         t3 := 1      */
		/*         goto end;    */
		/*                      */
		/************************/
		sir_MIPS_a_lot.getInstance().label(label_AssignOne);
		sir_MIPS_a_lot.getInstance().li(dst,1);
		sir_MIPS_a_lot.getInstance().jump(label_end);

		/*************************/
		/* [4] label_AssignZero: */
		/*                       */
		/*         t3 := 1       */
		/*         goto end;     */
		/*                       */
		/*************************/
		sir_MIPS_a_lot.getInstance().label(label_AssignZero);
		sir_MIPS_a_lot.getInstance().li(dst,0);
		sir_MIPS_a_lot.getInstance().jump(label_end);

		/******************/
		/* [5] label_end: */
		/******************/
		sir_MIPS_a_lot.getInstance().label(label_end);
	}

	private void eqMIPSme(TEMP dst, TEMP t1, TEMP t2) 
	{
		/*******************************/
		/* [1] Allocate 3 fresh labels */
		/*******************************/
		String label_end        = sir_MIPS_a_lot.getFreshLabel("end");
		String label_AssignOne  = sir_MIPS_a_lot.getFreshLabel("AssignOne");
		String label_AssignZero = sir_MIPS_a_lot.getFreshLabel("AssignZero");
		
		/******************************************/
		/* [2] if (t1==t2) goto label_AssignOne;  */
		/*     if (t1!=t2) goto label_AssignZero; */
		/******************************************/
		sir_MIPS_a_lot.getInstance().beq(t1,t2,label_AssignOne);
		sir_MIPS_a_lot.getInstance().bne(t1,t2,label_AssignZero);

		/************************/
		/* [3] label_AssignOne: */
		/*                      */
		/*         t3 := 1      */
		/*         goto end;    */
		/*                      */
		/************************/
		sir_MIPS_a_lot.getInstance().label(label_AssignOne);
		sir_MIPS_a_lot.getInstance().li(dst,1);
		sir_MIPS_a_lot.getInstance().jump(label_end);

		/*************************/
		/* [4] label_AssignZero: */
		/*                       */
		/*         t3 := 1       */
		/*         goto end;     */
		/*                       */
		/*************************/
		sir_MIPS_a_lot.getInstance().label(label_AssignZero);
		sir_MIPS_a_lot.getInstance().li(dst,0);
		sir_MIPS_a_lot.getInstance().jump(label_end);

		/******************/
		/* [5] label_end: */
		/******************/
		sir_MIPS_a_lot.getInstance().label(label_end);
	}
	
	private void limitIntMIPSme(TEMP dst)
	{
		int MAX = (int)Math.pow(2, 15)-1;
		int MIN = -(int)Math.pow(2, 15);
		sir_MIPS_a_lot mips = sir_MIPS_a_lot.getInstance();
		
		TEMP intMax = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP intMin = TEMP_FACTORY.getInstance().getFreshTEMP();
		String Assign_intMin = sir_MIPS_a_lot.getFreshLabel("Assign_intMin");
		String Assign_intMax = sir_MIPS_a_lot.getFreshLabel("Assign_intMax");
		String Binop_end = sir_MIPS_a_lot.getFreshLabel("Binop_end");
	
		mips.li(intMax, MAX);
		mips.li(intMin, MIN);
		mips.blt(dst, intMin, Assign_intMin); //if dst<intMin=> $dst=intMin
		mips.bge(dst, intMax, Assign_intMax); //if dst>=intMax=> $dst=intMax
		mips.jump(Binop_end);
		
		mips.label(Assign_intMin);
		mips.move(dst, intMin);
		mips.jump(Binop_end);
		
		mips.label(Assign_intMax);
		mips.move(dst, intMax);
		
		mips.label(Binop_end);
	}

	private void stringCompareMIPSme(TEMP dst, TEMP t1, TEMP t2)
	{
		AST_PROGRAM.stringCompareUsed=true;
		sir_MIPS_a_lot mips = sir_MIPS_a_lot.getInstance();
		mips.storeTempsInStack();
		mips.push(t2);
		mips.push(t1);
		mips.jal("_string_compare");
		
		//after call
		mips.popArgs(2);
		mips.loadTempsFromStack();
		mips.getReturnValue(dst);
	}
	
	private void stringConcatMIPSme(TEMP dst, TEMP t1, TEMP t2)
	{
		AST_PROGRAM.stringConcatUsed=true;
		sir_MIPS_a_lot mips = sir_MIPS_a_lot.getInstance();
		mips.storeTempsInStack();
		mips.push(t2);
		mips.push(t1);
		mips.jal("_string_concat");
		
		//after call
		mips.popArgs(2);
		mips.loadTempsFromStack();
		mips.getReturnValue(dst);
	}
	
}
