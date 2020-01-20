package IR;

import TEMP.*;
import MIPS.*;

import java.util.Set;

public class IRcommand_StoreAddress extends IRcommand {
	
	TEMP address;
	TEMP value;
	
	public IRcommand_StoreAddress(TEMP address, TEMP value)
	{
		this.address = address;
		this.value = value;
	}

	public void MIPSme()
	{
		sir_MIPS_a_lot.getInstance().storeInAddress(address, value);
	}

}
