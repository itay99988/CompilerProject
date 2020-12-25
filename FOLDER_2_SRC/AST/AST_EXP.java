package AST;

import TYPES.*;
import TEMP.*;
import MIPS.*;

public abstract class AST_EXP extends AST_Node {
	
	public TYPE SemantMe() throws SemantException
	{
		return null;
	}

	public abstract TEMP MIPSme();

}