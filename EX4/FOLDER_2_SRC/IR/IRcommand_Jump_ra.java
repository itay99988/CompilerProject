package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;

import java.util.Set;

public class IRcommand_Jump_ra extends IRcommand {

	public IRcommand_Jump_ra()
	{
	}

	public void MIPSme() {
		sir_MIPS_a_lot.getInstance().jumpra();
	}

}
