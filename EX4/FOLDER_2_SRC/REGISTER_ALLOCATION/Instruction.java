package REGISTER_ALLOCATION;

import java.util.*;

public class Instruction {

    public String name;
    public Set<String> liveIn, liveOut, liveInTag, liveOutTag, use, def;
    public Set<Instruction> successors;


    public Instruction(String name) {
        this.name = name;
        use = new HashSet<>();
        def = new HashSet<>();
        successors = new HashSet<>();
        liveIn = new HashSet<>();
        liveOut = new HashSet<>();
        liveInTag = new HashSet<>();
        liveOutTag = new HashSet<>();
    }


    public String toString(){
        String successorsStr="[";
        for(Instruction successor: successors){
            successorsStr = successorsStr + successor.name + ", ";
        }
        successorsStr=successorsStr + "]";
        
        String ret = String.format("%s : use: %s, def: %s, succ: %s, [liveIn: %s, liveOut%s], [liveIn': %s, liveOut'%s]\n",
                        name.toUpperCase(), use, def, successorsStr, liveIn, liveOut, liveInTag, liveOutTag);
        return ret;
    }
}






