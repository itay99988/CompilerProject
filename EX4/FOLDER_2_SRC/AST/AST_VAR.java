package AST;

import TYPES.*;
import TEMP.*;

public abstract class AST_VAR extends AST_Node
{
	public AST_VAR() {
		super();
	}

	public TYPE SemantMe() throws SemantException{
		return null;
	}
	
	public TEMP IRme()
	{
		return null;
	}
}
