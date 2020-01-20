package IR;

import java.util.Set;

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;

public class IRcommand_PushTempRegToStack extends IRcommand {

    private TempRegisters dest;

    public IRcommand_PushTempRegToStack(TempRegisters dest) {
        this.dest = dest;
    }
	
    @Override
    public void MIPSme() {
		sir_MIPS_a_lot.getInstance().pushRegToStack(dest);
    }
}
