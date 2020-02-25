package REGISTER_ALLOCATION;

import TEMP.TEMP_FACTORY;

import java.io.PrintWriter;
import java.util.*;
import java.io.*;


public class RegisterAllocator {
    private List<Instruction> instructions;
    private Map<String, LabelInstruction> labels;
    private Map<String, List<JumpBranch>> labelToJumpsMap;
    private Graph interferenceGraph;

    private static final Set<String> JUMP_INSTRUCTIONS = new HashSet<>(Arrays.asList("j", "jal", "jr"));
    private static final Set<String> STORE_INSTRUCTIONS = new HashSet<>(Arrays.asList("sb", "sw"));
    private static final Set<String> BRANCH_INSTRUCTIONS = new HashSet<>(Arrays.asList("blt", "bge", "bne", "beq", "beqz", "bgt"));
    private static final int NUM_OF_REGISTERS = 10;
    private static final String TEMP = "Temp_";

    public RegisterAllocator(){
        this.instructions = new ArrayList<>();
        this.labels = new HashMap<>();
        this.labelToJumpsMap = new HashMap<>();
        this.interferenceGraph = null;
    }


    public void allocate(String outputFileName) throws Exception {
        buildInstructionList();
        updateSuccessors();
        livenessAnalysis();
        buildInterferenceGraph();
        Map<String, Integer> coloringMap = findGraphColoring();
        System.out.println(coloringMap.entrySet());
        replaceTempsWithRegisters(outputFileName, coloringMap);    
    }


    public void buildInstructionList(){
        String dirName = "./FOLDER_5_OUTPUT/";
        String textFileName = "MIPS.txt";
        Scanner sc = null;
        File file = new File(dirName + textFileName);

        try {
            sc = new Scanner(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while(sc.hasNextLine()) {
            String s = sc.nextLine();
            addInstructionToList(s);
        }
        sc.close();
    }


    public void updateSuccessors(){
        int numOfInsts = this.instructions.size();
        for(int i = 0; i < numOfInsts; i++) {
            Instruction inst = this.instructions.get(i);
            if(inst instanceof JumpBranch) {
                if(labels.containsKey(((JumpBranch)inst).jumpLabel.name) && !inst.name.equals("jal")) {
                    inst.successors.add(((JumpBranch) inst).jumpLabel);
                }
                if(i < numOfInsts - 1 && (BRANCH_INSTRUCTIONS.contains(inst.name) || inst.name.equals("jal"))) {
                    inst.successors.add(this.instructions.get(i+1));
                }
            } else if(i < numOfInsts - 1) {
                inst.successors.add(this.instructions.get(i+1));
            }
        }
    }


    public void livenessAnalysis() {
        boolean changed = true;
        while (changed) {
            /*******************/
            /* [1] Make a step */
            /*******************/
            for(Instruction inst : instructions) {
                if(inst.liveOut != null) { inst.liveInTag.addAll(inst.liveOut); }
                if(inst.def != null) { inst.liveInTag.removeAll(inst.def); }
                if(inst.use != null) { inst.liveInTag.addAll(inst.use); }
            }
            
            /***************************************************************/
            /* [2] Propogate the effect of [1] to related nodes in the CFG */
            /***************************************************************/
            for(Instruction inst : instructions) {
                for (Instruction succ : inst.successors){
                    if(succ.liveInTag != null) { inst.liveOutTag.addAll(succ.liveInTag); }
                }
            }

            /**********************************/
            /* [3] Check if changes were made */
            /**********************************/
            changed = false;
            for(Instruction inst : instructions) {
                if (!inst.liveIn.equals(inst.liveInTag)) { changed = true; }
                if (!inst.liveOut.equals(inst.liveOutTag)) { changed = true; }
            }
            
            /*******************************************/
            /* [4] Add all liveness changes from tags  */
            /*******************************************/
            for(Instruction inst : instructions) {
                if(inst.liveInTag != null) inst.liveIn.addAll(inst.liveInTag);
                if(inst.liveOutTag != null) inst.liveOut.addAll(inst.liveOutTag);
            }
        }
    }


    public void buildInterferenceGraph() {
        int numOfVertices = TEMP_FACTORY.getInstance().getTempsCount();
        this.interferenceGraph = new Graph(numOfVertices);
        for(Instruction inst : instructions){
            for (String s1 : inst.liveOut){
                for (String s2 : inst.liveOut){
                    int u = Integer.parseInt(s1.substring(s1.lastIndexOf('_') + 1));
                    int v = Integer.parseInt(s2.substring(s2.lastIndexOf('_') + 1));
                    this.interferenceGraph.addEdge(u, v);
                }
            }
        }
    }


    public Map<String, Integer> findGraphColoring() throws Exception{
        Map<String, Integer> coloringMap = new HashMap<>();
        Integer[] coloringArray =  this.interferenceGraph.findColoring();
        int numOfColorsUsed = Collections.max(Arrays.asList(coloringArray)) + 1;
        if(numOfColorsUsed > NUM_OF_REGISTERS){
            final String error = String.format("Failed assigning Registers.\nAt least %d registers are needed.", numOfColorsUsed);
            throw new Exception(error);
        }
        for(int i = 0; i < coloringArray.length; i++) {
            String temp = String.format(TEMP + "%d", i);
            coloringMap.put(temp, coloringArray[i]);
        }
        return coloringMap;
    }


    public void replaceTempsWithRegisters(String outputFileName, Map<String, Integer> coloringMap) {
        String dirName = "./FOLDER_5_OUTPUT/";
        String inputFileName = "MIPS.txt";

        PrintWriter fileWriter = null;
        Scanner scanner = null;
        File outputFile;
        if(outputFileName.startsWith("/") || outputFileName.startsWith("./")) {
            outputFile = new File(outputFileName);
        } else {
            outputFile = new File("./" + outputFileName);
        }
        File inputFile = new File(dirName + inputFileName);
        
        int tempsCount = TEMP_FACTORY.getInstance().getTempsCount();
        try {
            fileWriter = new PrintWriter(outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            scanner = new Scanner(inputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while(scanner.hasNextLine()) {
            String s = scanner.nextLine();
            String line = s;
            for(int i = tempsCount; i >= 1; i--) {
                String temp = String.format(TEMP + "%d", i);
                String reg = String.format("\\$t%d", coloringMap.get(temp));
                line = line.replaceAll(temp, reg);

            }
            fileWriter.println(line);
            fileWriter.flush();
        }
        scanner.close();
        fileWriter.close();
    }


    private void addInstructionToList(String mipsInstruction) {
        String[] parts = mipsInstruction.trim().split("[(,|\\s|\\(|\\))]+");
        String name = parts[0];
        int length = parts.length;
        if(mipsInstruction.charAt(mipsInstruction.length()-1) == ':') {
            addLabelInstruction(mipsInstruction);

        }else if(JUMP_INSTRUCTIONS.contains(name) || BRANCH_INSTRUCTIONS.contains(name)) {
            addJumpBranchInstruction(parts, name, length);

        } else {
            addBasicInstruction(parts, name, length);
        }
    }


    public void addLabelInstruction(String labelNameWithColon){
        String labelName = labelNameWithColon.substring(0, labelNameWithColon.length() -1);
        LabelInstruction label = labelFactory(labelName);
        instructions.add(label);
        labels.put(labelName, label);
    }


    public void addJumpBranchInstruction(String[] instParts, String instName, int instLength){
        String labelName = instParts[instLength - 1];
        LabelInstruction label = labelFactory(labelName);
        JumpBranch jump = new JumpBranch(instName, label);
        for (int i=1; i < instLength; i++) {
            if (instParts[i].startsWith(TEMP)){
                jump.use.add(instParts[i]);
            }
        }
        if(!labelName.startsWith(TEMP) && !labelName.startsWith("$ra")) {
            labels.put(labelName, label);
            List<JumpBranch>  jumpsToTheSameLabel = labelToJumpsMap.get(labelName);
            if(jumpsToTheSameLabel == null) {
                jumpsToTheSameLabel = new ArrayList<>();
            }
            jumpsToTheSameLabel.add(jump);
            labelToJumpsMap.put(labelName, jumpsToTheSameLabel);
        }
        instructions.add(jump);
    }


    public void addBasicInstruction(String[] instParts, String instName, int instLength){
        Instruction inst = new Instruction(instName);
        if(STORE_INSTRUCTIONS.contains(instName)) {
            for(int i = 1; i < instLength; i++) {
                if(instParts[i].startsWith(TEMP)) {
                    inst.use.add(instParts[i]);
                }
            }
        } else {
            for(int i = 1; i < instLength; i++) {
                if(i == 1 && instParts[i].startsWith(TEMP)) {
                    inst.def.add(instParts[i]);
                } else if(instParts[i].startsWith(TEMP)){
                    inst.use.add(instParts[i]);
                }
            }
        }
        instructions.add(inst);
    }


    public LabelInstruction labelFactory(String name){
        LabelInstruction label;
        if(this.labels.containsKey(name)) {
            label = this.labels.get(name);
        } else {
            label = new LabelInstruction(name);
        }
        return label;
    }

}
