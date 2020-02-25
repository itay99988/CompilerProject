package REGISTER_ALLOCATION;


class JumpBranch extends Instruction {

    public LabelInstruction jumpLabel;


    public JumpBranch(String name, LabelInstruction jumpLabel) {
        super(name);
        this.jumpLabel = jumpLabel;
    }


    public String toString() {
        String successorsStr="[";
        for(Instruction successor: this.successors) {
            successorsStr = successorsStr + successor.name + ", ";
        }
        successorsStr=successorsStr + "]";
        
        String ret = String.format("JUMP: %s ==> (%s), : use: %s, def: %s, succ: %s, [liveIn: %s, liveOut%s], [liveIn': %s, liveOut'%s]\n",
                        name, jumpLabel.name, this.use, this.def, successorsStr, liveIn, liveOut, liveInTag, liveOutTag);
        return ret;
    }

}