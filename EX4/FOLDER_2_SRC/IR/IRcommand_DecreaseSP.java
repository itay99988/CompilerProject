package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;

import java.util.Set;

public class IRcommand_DecreaseSP extends IRcommand {

    private int wordsnum;

    public IRcommand_DecreaseSP(int wordsnum) {
        this.wordsnum = wordsnum;
    }


    @Override
    public void MIPSme() {
    	sir_MIPS_a_lot.getInstance().decreaseSP(wordsnum);
    }
}
