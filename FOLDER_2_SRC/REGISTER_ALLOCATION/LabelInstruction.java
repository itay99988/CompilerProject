package REGISTER_ALLOCATION;

class LabelInstruction extends Instruction {

    public LabelInstruction(String name) {
        super(name);
    }


    public String toString() {
        String successorsStr="[";
        for(Instruction successor : this.successors) {
            successorsStr = successorsStr + successor.name + ", ";
        }
        successorsStr=successorsStr + "]";
        
        String ret = String.format("LABEL: %s, : use: %s, def: %s, succ: %s, [liveIn: %s, liveOut%s], [liveIn': %s, liveOut'%s]\n",
                        name, this.use, this.def, successorsStr, liveIn, liveOut, liveInTag, liveOutTag);
        return ret;
    }

}
