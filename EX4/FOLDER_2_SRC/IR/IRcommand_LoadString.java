package IR;

import TEMP.*;
import MIPS.*;

import java.util.Set;

public class IRcommand_LoadString extends IRcommand
{
	TEMP dst;
	String string;
	
	public IRcommand_LoadString(TEMP dst,String string)
	{
		this.dst      = dst;
		this.string = string;
	}

	public void MIPSme() {
		sir_MIPS_a_lot.getInstance().loadStringFromDataSegment(dst, string);

	}
}
