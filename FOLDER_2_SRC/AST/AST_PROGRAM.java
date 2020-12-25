package AST;

import TYPES.*;
import MIPS.sir_MIPS_a_lot;
import TEMP.*;

public class AST_PROGRAM extends AST_Node {
	
	public AST_DEC_LIST l;

	public static boolean stringConcatUsed = false;
    public static boolean stringCompareUsed = false;
	
	public AST_PROGRAM (AST_DEC_LIST decList) 
	{
		System.out.print("====================== program -> decs\n");
		l = decList;
	}
	
	
	public void PrintMe() {
		/************************************/
		/* AST NODE TYPE = PROGRAM (AST NODE) */
		/************************************/
		System.out.print("AST NODE: PROGRAM\n");

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (l != null) l.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"PROGRAM");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, l.SerialNumber);

	}

	public TYPE SemantMe() throws SemantException {
		return l.SemantMe();
	}

	public void MIPSme() {
		sir_MIPS_a_lot mips = sir_MIPS_a_lot.getInstance();

		// first run MIPSme on all of the program
		l.MIPSme();

		// then add string functions, if needed
		if(stringCompareUsed) {
			addStringCompareFunction();
		}
		if(stringConcatUsed) {
			addStringConcatFunction();
		}
		
		addExitWithErrorLabels();

		addExitWithoutErrorsLabel();
	}


	private void addStringCompareFunction() {
		sir_MIPS_a_lot mips = sir_MIPS_a_lot.getInstance();
		String while_start = sir_MIPS_a_lot.getFreshLabel("while_start");
		String while_end = sir_MIPS_a_lot.getFreshLabel("while_end");
		String while_false = sir_MIPS_a_lot.getFreshLabel("while_false");

		TEMP str1 = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP str2 = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP ch1 = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP ch2 = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP nullTerminator = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP result = TEMP_FACTORY.getInstance().getFreshTEMP();
		
		mips.label("_string_compare");
		mips.prologue(0);
		mips.load(str1, "8($fp)");
		mips.load(str2, "12($fp)");
		mips.li(ch1, 0); 
		mips.li(ch2, 0);
		mips.li(nullTerminator, 0);

		mips.label(while_start);
		mips.loadByte(ch1, str1);
		mips.loadByte(ch2, str2);
		mips.bne(ch1, ch2, while_false);
		mips.addi(str1, str1, 1);
		mips.addi(str2, str2, 1);
		mips.bne(ch1, nullTerminator, while_start);
		// if we got to this point, ch1[i] == ch2[i] == '\0'
		// we've reached the end of both strings
		mips.li(result, 0); // strings are identical
		mips.jump(while_end);

		mips.label(while_false);
		mips.li(result, 1); //strings are not identical

		mips.label(while_end);
		mips.returnVal(result);
		mips.epilogue();
	}


	private void addStringConcatFunction() {
		sir_MIPS_a_lot mips = sir_MIPS_a_lot.getInstance();
		String while_len_str1 = sir_MIPS_a_lot.getFreshLabel("while_len_str1");
		String while_len_str2 = sir_MIPS_a_lot.getFreshLabel("while_len_str2");
		String while_len_end = sir_MIPS_a_lot.getFreshLabel("while_len_end");
		String copy_str1 = sir_MIPS_a_lot.getFreshLabel("copy_str1");
		String copy_str2 = sir_MIPS_a_lot.getFreshLabel("copy_str2");
		String concatComplete = sir_MIPS_a_lot.getFreshLabel("concatComplete");
		TEMP str1 = TEMP_FACTORY.getInstance().getFreshTEMP(); 
		TEMP str2 = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP len = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP ch = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP newString = TEMP_FACTORY.getInstance().getFreshTEMP();
		
		
		mips.label("_string_concat");
		mips.prologue(0);
		mips.load(str1, "8($fp)");
		mips.load(str2, "12($fp)");
		mips.li(len, 0);
		
		// count the sum of both strings length
		mips.label(while_len_str1);
		mips.loadByte(ch, str1);
		mips.beqz(ch,  while_len_str2);
		mips.addi(len, len, 1);
		mips.addi(str1, str1, 1);
		mips.jump(while_len_str1);

		mips.label(while_len_str2);
		mips.loadByte(ch, str2);
		mips.beqz(ch, while_len_end);
		mips.addi(len, len, 1);
		mips.addi(str2, str2, 1);
		mips.jump(while_len_str2);

		mips.label(while_len_end);
		mips.addi(len, len, 1); // null terminator
		mips.allocateOnHeap(newString, len);
		// make str1 and str2 point to the start of each string
		mips.load(str1, "8($fp)");
		mips.load(str2, "12($fp)");

		mips.label(copy_str1);
		mips.loadByte(ch, str1);
		mips.storeByte(newString, ch);
		mips.beqz(ch, copy_str2);
		mips.addi(newString, newString, 1);
		mips.addi(str1, str1, 1);
		mips.jump(copy_str1);

		mips.label(copy_str2);
		mips.loadByte(ch, str2);
		mips.storeByte(newString, ch);
		mips.beqz(ch, concatComplete);
		mips.addi(newString, newString, 1);
		mips.addi(str2, str2, 1);
		mips.jump(copy_str2);
		
		mips.label(concatComplete);
		mips.addi(len, len, -1);
		// make newString point to the start
		mips.sub(newString, newString, len); 
		mips.returnVal(newString);
		mips.epilogue();
	}


	private void addExitWithErrorLabels() {
		sir_MIPS_a_lot mips = sir_MIPS_a_lot.getInstance();
		TEMP msg = TEMP_FACTORY.getInstance().getFreshTEMP();



		mips.label("_divZeroError");
		mips.loadAddress(msg, "string_illegal_div_by_0");
		mips.jump("_exitWithError");

		mips.label("_nullDereferenceError");
		mips.loadAddress(msg, "string_invalid_ptr_dref");
		mips.jump("_exitWithError");

		mips.label("_accessViolationError");
		mips.loadAddress(msg, "string_access_violation");
		mips.jump("_exitWithError");

		mips.label("_exitWithError");
		mips.printString(msg);
		mips.exit();
	}


	private void addExitWithoutErrorsLabel() {
		sir_MIPS_a_lot mips = sir_MIPS_a_lot.getInstance();
		mips.label("The_End");
		mips.exit();
	}

}
