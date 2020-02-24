package AST;
import TYPES.*;
import TEMP.*;
import IR.*;

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
			String.format("EXP\nBINOP", op));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, left.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, op.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, right.SerialNumber);
	}
	
	public TYPE SemantMe() throws SemantException
	{

		TYPE t1 = null;
		TYPE t2 = null;
		
		if (left  != null) t1 = left.SemantMe();
		if (right != null) t2 = right.SemantMe();
		
		if ((t1 == TYPE_INT.getInstance()) && (t2 == TYPE_INT.getInstance()))
		{
			return TYPE_INT.getInstance();
		}
		if ((t1 == TYPE_STRING.getInstance()) && (op.toString() == "PLUS") && (t2 == TYPE_STRING.getInstance()))
		{
			this.isStringConcat = true;
			return TYPE_STRING.getInstance();
		}

        if(op.toString() == "EQ"){
            if(t1.isArray() && t2.isArray() && t1.name.equals(t2.name))
                return TYPE_INT.getInstance();
            if(t1 == TYPE_STRING.getInstance() && t2 == TYPE_STRING.getInstance())
			{
				this.isStringCompare = true;
				return TYPE_INT.getInstance();
			}
                
            if(t1 == TYPE_NIL.getInstance() || t2 == TYPE_NIL.getInstance()){
                if(t1.isArray() || t2.isArray())
                    return TYPE_INT.getInstance();
                if(t1.isClass() || t2.isClass())
                    return TYPE_INT.getInstance();
            }

            if(t1.isClass() && t2.isClass()){
                if(isComparable((TYPE_CLASS) t1, (TYPE_CLASS) t2))
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
	
	public TEMP IRme()
	{
		TEMP t1 = null;
		TEMP t2 = null;
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
				
		if (left  != null) t1 = left.IRme();
		System.out.println("Finished t1 ");
		if (right != null) t2 = right.IRme();
		System.out.println("Finished t2 ");
		
		if (op.toString() == "PLUS")
		{
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Binop_Add_Integers(dst,t1,t2));
		}
		if (op.toString() == "MINUS")
		{
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Binop_Sub_Integers(dst,t1,t2));
		}
		if (op.toString() == "TIMES")
		{
			System.out.println("Times t1 t2 ");
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Binop_Mul_Integers(dst,t1,t2));
		}
		if (op.toString() == "DIVIDE")
		{
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Binop_Div_Integers(dst,t1,t2));
		}
		if (op.toString() == "EQ")
		{
			System.out.println("EQ t1 t2 ");
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Binop_EQ_Integers(dst,t1,t2));
		}
		if (op.toString() == "LT")
		{
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Binop_LT_Integers(dst,t1,t2));
		}
		if (op.toString() == "GT")
		{
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Binop_GT_Integers(dst,t1,t2));
		}
		return dst;
	}
	
}
