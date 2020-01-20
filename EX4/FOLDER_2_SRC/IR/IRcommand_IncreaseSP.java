package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;

import java.util.Set;

public class IRcommand_IncreaseSP extends IRcommand {

    private int wordsnum;

    public IRcommand_IncreaseSP(int wordsnum) {
        this.wordsnum = wordsnum;
    }


    @Override
    public void MIPSme() {
    	sir_MIPS_a_lot.getInstance().IncreaseSP(wordsnum);
    }
}
