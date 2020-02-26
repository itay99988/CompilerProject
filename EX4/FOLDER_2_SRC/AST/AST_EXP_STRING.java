package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import MIPS.*;

public class AST_EXP_STRING extends AST_EXP {

    public String str;


    public AST_EXP_STRING(String str) {
        this.str = str;

        System.out.format("====================== exp -> STRING( %s )\n", str);
    }


	public void PrintMe() {
		/**********************************/
		/* AST NODE TYPE = AST EXP STRING */
		/**********************************/
		System.out.format("AST NODE: EXP_STR( %s )\n", this.str);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
        String val = str.replace('"', '\'');
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("EXP_STR( '%s' )", val));
	}
	
	public TYPE SemantMe() throws SemantException
	{
		return TYPE_STRING.getInstance();
	}
	
	public TEMP MIPSme() 
	{
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
		sir_MIPS_a_lot mips = sir_MIPS_a_lot.getInstance();
		String stringLabel = mips.allocateString(this.str);
		mips.loadAddress(dst, stringLabel);
		
		return dst;
	}
    
}