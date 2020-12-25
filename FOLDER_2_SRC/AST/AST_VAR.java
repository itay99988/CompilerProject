package AST;

import TYPES.*;
import TEMP.*;
import SYMBOL_TABLE.*;

public abstract class AST_VAR extends AST_Node
{
	public AST_VAR() {
		super();
	}

	public TYPE SemantMe() throws SemantException{
		return null;
	}

	public abstract TEMP getMipsValue();

	public abstract void setMipsValue(String dst, TEMP src);

	public abstract String getMipsRepr();

}
