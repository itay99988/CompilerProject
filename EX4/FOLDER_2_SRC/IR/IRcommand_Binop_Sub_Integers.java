package IR;

import MIPS.*;
import TEMP.*;

public class IRcommand_Binop_Sub_Integers extends IRcommand {

    private TEMP t1;
    private TEMP t2;
    private TEMP dst;

    public IRcommand_Binop_Sub_Integers(TEMP dst, TEMP t1, TEMP t2) {
        this.dst = dst;
        this.t1 = t1;
        this.t2 = t2;
    }

    public void MIPSme() {
    	sir_MIPS_a_lot.getInstance().sub(dst,t1,t2);
    }
}
