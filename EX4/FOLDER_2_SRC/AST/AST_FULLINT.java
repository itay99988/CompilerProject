package AST;

import TYPES.*;
import TEMP.*;
import MIPS.*;

public class AST_FULLINT extends AST_DEC {

	public AST_INT astInt;
	public boolean withMinus;
	

	public AST_FULLINT(AST_INT astInt, boolean withMinus) {
		this.astInt = astInt;
		this.withMinus = withMinus;

		String minusStr = withMinus ? " MINUS" : "";
		System.out.format("====================== fullInt ->%s int\n", minusStr);
	}


	public void PrintMe() {
		/*******************************/
		/* AST NODE TYPE = AST FULLINT */
		/*******************************/
		System.out.format("AST NODE: FULLINT\n", this);

		/********************************/
		/* RECURSIVELY PRINT astInt ... */
		/********************************/
		if (astInt != null) astInt.PrintMe();

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("FULLINT", this));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, astInt.SerialNumber);
	}
	
	public TYPE SemantMe(){
		return TYPE_INT.getInstance();
	}
	
	public int GetValue()
	{
		if (withMinus)
			return astInt.GetValue() * -1;
		
		return astInt.GetValue();
	}

	public TEMP MIPSme() 
	{
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
		int newVal=this.GetValue();
		
		if(newVal > Math.pow(2, 15)-1)
			newVal = (int)Math.pow(2, 15)-1;
		else
			if(newVal < -Math.pow(2, 15))
				newVal = -(int)Math.pow(2, 15);
		
		sir_MIPS_a_lot.getInstance().li(dst, newVal);
		
		return dst;
	}


}
	
