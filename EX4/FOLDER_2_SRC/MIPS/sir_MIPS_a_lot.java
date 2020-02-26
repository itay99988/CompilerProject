/***********/
/* PACKAGE */
/***********/
package MIPS;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;
import java.util.*;
import java.io.*;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;

public class sir_MIPS_a_lot
{
	/***********************/
	/*   Class Members     */
	/***********************/
	private static String dirName = "./FOLDER_5_OUTPUT/";
	private static String finalFile = String.format("MIPS.txt");
	private static String dataFileName = "DATA_BLOCK.txt";
	private static String textFileName = "TEXT_BLOCK.txt";
	private static String initFileName = "INIT_BLOCK.txt";

	private int WORD_SIZE = 4;

	/***********************/
	/*    File writers     */
	/***********************/
	private PrintWriter textPartPrinter;
	private PrintWriter dataPartPrinter;
	private PrintWriter initPartPrinter;
	private PrintWriter writer;
	private PrintWriter finalWriter;

	//functions for switching from one writer to another
	public void writeText() 
	{
		writer = textPartPrinter;
	}
	public void writeInit() 
	{
		writer = initPartPrinter;
	}

	public void finalizeFile()
	{
		//push "main"
		initPartPrinter.print("\tla $t0, string_main\n");
		initPartPrinter.print("\taddi $sp, -4\n");
		initPartPrinter.print("\tsw $t0, 0($sp)\n");

		//call main
		initPartPrinter.print("\tjal _main\n");
		initPartPrinter.print("\tli $v0,10\n");
		initPartPrinter.print("\tsyscall\n");
		textPartPrinter.close();
		dataPartPrinter.close();
		initPartPrinter.close();

		File dataFile = new File(dirName + dataFileName);
		File initFile = new File(dirName + initFileName);
		File textFile = new File(dirName + textFileName);

		try 
		{
			finalWriter = new PrintWriter(dirName + finalFile);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		// Copy data segment to final file
		appendFileContentsToFinal(dataFile);

		// Copy init code to final file
		appendFileContentsToFinal(initFile);

		// Copy text segment to final file
		appendFileContentsToFinal(textFile);

		finalWriter.close();
	}

	private void appendFileContentsToFinal(File file) 
	{
		Scanner sc = null;
		try 
		{
			sc = new Scanner(file);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

        while(sc.hasNextLine()) 
		{
            String s = sc.nextLine();
            finalWriter.println(s);
        }
	}

	/******************************/
	/*   End of writers section   */
	/******************************/

	/******************************/
	/*      Label management      */
	/******************************/

	private static int label_counter = 0;

	public static String getFreshLabel(String msg) 
	{
		return String.format("Label_%d_%s", label_counter++, msg);
	}

	public static String getFunctionLabel(String className, String name) 
	{
		if (className != null) 
		{
			return String.format("%s_%s", className, name);
		} 
		else 
		{
			if (name.equals("main")) 
				return "_main";

			return "global_func_" + name;
		}
	}

	/******************************/
	/*   End of Label management  */
	/******************************/

	/******************************/
	/*       MIPS Commands        */
	/******************************/
	//checked
	public void print_int(TEMP src)
	{
		writer.format("\tmove $a0, %s\n", src);
		writer.format("\tli $v0, 1\n");
		writer.format("\tsyscall\n");

		writer.format("\tli $a0,32\n");
		writer.format("\tli $v0,11\n");
		writer.format("\tsyscall\n");
	}

	//checked
	public void allocateWord(String var_name)
	{
		writer.format(".data\n");
		writer.format("\tglobal_%s: .word 721\n",var_name);
	}

	private static int string_counter = 0;
	//checked
	public String allocateString(String str) 
	{
		String stringLabel = String.format("string_%d", string_counter++);
		dataPartPrinter.format("\t%s: .asciiz %s\n", stringLabel, str);
		return stringLabel;
	}
    //checked
	public void prologue(int localVarCount) 
	{
		writer.format("\taddi $sp, $sp, -8\n");
		writer.format("\tsw $fp, 0($sp)\n");
		writer.format("\tsw $ra, 4($sp)\n");
		writer.format("\tmove $fp, $sp\n");
		if (localVarCount > 0) 
		{
			writer.format("\taddi $sp, $sp, %d\n", -4*localVarCount);
		}
	}
    //checked
	public void epilogue() 
	{
		writer.format("\tmove $sp, $fp\n");
		writer.format("\tlw $fp, 0($sp)\n");
		writer.format("\tlw $ra, 4($sp)\n");
		writer.format("\taddi $sp, $sp, 8\n");
		writer.format("\tjr $ra\n");
	}
    //checked
	public void returnVal(TEMP val) 
	{
		writer.format("\tmove $v0, %s\n", val);
		this.epilogue();
	}

    //checked
	public void load(TEMP dst,String var_name)
	{
		writer.format("\tlw %s, %s\n", dst, var_name);
	}
    //checked
	public void load(TEMP dst, TEMP src, int offset)
	{
		writer.format("\tlw %s, %d(%s)\n", dst, offset, src);
	}
    //checked
	public void loadAddress(TEMP dst, String label)
	{
		writer.format("\tla %s, %s\n", dst, label);
	}

	//checked
	public void loadByte(TEMP dst, TEMP src){
		writer.format("\tlb %s, 0(%s)\n", dst, src);
	}

	public void storeByte(TEMP dst, TEMP src){
		writer.format("\tsb %s, 0(%s)\n", src, dst);
	}

	//checked
	public void store(String var_name,TEMP src)
	{
		int idxsrc=src.getSerialNumber();
		writer.format("\tsw %s, %s\n", src, var_name);	
	}
	//checked
	public void li(TEMP t,int value)
	{
		int idx=t.getSerialNumber();
		writer.format("\tli Temp_%d, %d\n",idx,value);
	}
	//checked
	public void add(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		writer.format("\tadd Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
	}
	//checked
	public void addi(TEMP dst, TEMP src, int i)
	{
		writer.format("\tadd %s, %s, %d\n",dst, src, i);
	}
	//checked
	public void sub(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		writer.format("\tsub Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
	}
	
	//checked
	public void mul(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		writer.format("\tmul Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
	}
	//checked
	public void div(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		writer.format("\tdiv Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
	}
	//checked
	public void label(String inlabel)
	{
		if (inlabel.equals("main")) 
		{
			writer.format("_main:\n");
		}
		writer.format("%s:\n", inlabel);
	}	
	//checked
	public void dataLabel(String inlabel) 
	{
		dataPartPrinter.format("%s:\n", inlabel);
	}
	//checked
	public void jump(String inlabel)
	{
		writer.format("\tj %s\n",inlabel);
	}
	
	public void jumpra() {
		writer.format("\tjr $ra \n");
	}	
	//checked
	public void blt(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		writer.format("\tblt Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	public void bgt(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		writer.format("\tbgt Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	//checked
	public void bge(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		writer.format("\tbge Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	public void ble(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		writer.format("\tble Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	//checked
	public void bne(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		writer.format("\tbne Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	//checked
	public void beq(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		writer.format("\tbeq Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	//checked
	public void beqz(TEMP oprnd1,String label)
	{
		int i1 =oprnd1.getSerialNumber();
				
		writer.format("\tbeq Temp_%d,$zero,%s\n",i1,label);				
	}
	
	public void storeInAddress(TEMP address, TEMP value) {
		writer.format("\tsw %s, (%s)\n", value.toString(), address.toString());
	}
	
	public void IncreaseSP(int wordsnum) {
		writer.format("\taddi $sp, $sp, %d\n", wordsnum*WORD_SIZE);
	}
	
	public void decreaseSP(int wordsnum) {
		writer.format("\taddi $sp, $sp, %d\n", wordsnum*WORD_SIZE*-1);
	}
	
	public void loadStringFromDataSegment(TEMP dst, String str) {
		writer.format("\tla %s, %s\n", dst.toString(), str);
	}

	//checked
	public void printString(TEMP str) {
		writer.format("\tmove $a0, %s\n", str);
		writer.format("\tli $v0, 4\n");
		writer.format("\tsyscall\n");
	}

	//checked
	public void exit(){
		writer.print("\tli $v0, 10\n");
		writer.print("\tsyscall\n");
	}

	/******************************/
	/*    END of MIPS Commands    */
	/******************************/
	
	

	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static sir_MIPS_a_lot instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected sir_MIPS_a_lot() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static sir_MIPS_a_lot getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new sir_MIPS_a_lot();

			try
			{
			/*********************************************************************************/
			/* [1] Open the MIPS text file and write data section with error message strings */
			/*********************************************************************************/
				instance.textPartPrinter = new PrintWriter(dirName + textFileName);
				instance.dataPartPrinter = new PrintWriter(dirName + dataFileName);
				instance.initPartPrinter = new PrintWriter(dirName + initFileName);

				//set the textPartPrinter to be the active one
				instance.writer = instance.textPartPrinter;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			/*****************************************************/
			/* [3] Print data section with error message strings */
			/*****************************************************/
			instance.dataPartPrinter.print(".data\n");
			instance.dataPartPrinter.print("string_access_violation: .asciiz \"Access Violation\"\n");
			instance.dataPartPrinter.print("string_illegal_div_by_0: .asciiz \"Illegal Division By Zero\"\n");
			instance.dataPartPrinter.print("string_invalid_ptr_dref: .asciiz \"Invalid Pointer Dereference\"\n");
			instance.dataPartPrinter.print("\tstring_main: .asciiz \"main\"\n");

			/*************************/
			/* [4] Init text section */
			/*************************/
			instance.initPartPrinter.println(".text");
			instance.initPartPrinter.println("main:");
		}

		return instance;
	}

}