package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;

import java.util.Set;

public class IRcommand_GetRegister extends IRcommand {

    private SystemRegisters src;
    private TEMP dest;

    public IRcommand_GetRegister(TEMP dest, SystemRegisters src) {
        this.dest = dest;
        this.src = src;
    }

    @Override
    public void MIPSme() {
		sir_MIPS_a_lot.getInstance().moveSystemRegisterToTempRegister(dest, src);
    }
}
