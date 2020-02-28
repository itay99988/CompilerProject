
import java.io.*;
import java.io.PrintWriter;
import java_cup.runtime.Symbol;
import AST.*;
import MIPS.*;
import REGISTER_ALLOCATION.*;

public class Main {
	static public void main(String argv[]) {
		Lexer l;
		Parser p;
		Symbol s;
		boolean isException = false;
		AST_PROGRAM AST = null;
		FileReader file_reader = null;
		PrintWriter file_writer = null;
		String inputFilename = argv[0];
		String outputFilename = argv[1];
		try {
			/********************************/
			/* [1] Initialize a file reader */
			/********************************/
			file_reader = new FileReader(inputFilename);

			/********************************/
			/* [2] Initialize a file writer */
			/********************************/
			file_writer = new PrintWriter(outputFilename);
		} catch (FileNotFoundException e) {
			System.out.println("file not found Exception");
			e.printStackTrace();
			System.exit(0);
		}

		/******************************/
		/* [3] Initialize a new lexer */
		/******************************/
		try {
			l = new Lexer(file_reader);

			/*******************************/
			/* [4] Initialize a new parser */
			/*******************************/
			p = new Parser(l, outputFilename);

			/***********************************/
			/* [5] 3 ... 2 ... 1 ... Parse !!! */
			/***********************************/
			AST = (AST_PROGRAM) p.parse().value;

			/*************************/
			/* [6] Print the AST ... */
			/*************************/
			AST.PrintMe();
		} catch (Exception e) {
			System.out.println("parseException");
			e.printStackTrace();
			System.exit(0);
		}
		try {
			/**************************/
			/* [7] Semant the AST ... */
			/**************************/
			AST.SemantMe();
		} catch (SemantException se) {
			file_writer.print(String.format("ERROR(%d)", se.getLineNumber()));
			file_writer.close();
			se.printStackTrace();
			System.exit(0);
		}

		/**********************************************/
		/* [8] Print OK and close the output file ... */
		/**********************************************/
		file_writer.print("OK");
		System.out.println("OK");
		file_writer.close();

		/*************************************/
		/* [9] Finalize AST GRAPHIZ DOT file */
		/*************************************/
		AST_GRAPHVIZ.getInstance().finalizeFile();

		/***************************/
		/* [10] Generate MIPS code */
		/***************************/
		try {
			AST.MIPSme();
		} catch (Exception e) {
			System.out.println("MIPSme Error");
			e.printStackTrace();
			System.exit(0);
		}

		/**************************************************/
		/* [11] Finalize the file */
		/* (with temporaries instead of actual registers) */
		/**************************/
		try {
			sir_MIPS_a_lot.getInstance().finalizeFile();
		} catch (Exception e) {
			System.out.println("ERROR: Could not finalize MIPS file.");
			e.printStackTrace();
			System.exit(0);
		}

		/***************************/
		/* [12] Allocate registers */
		/***************************/
		RegisterAllocator registerAllocator = new RegisterAllocator();
		try {
			registerAllocator.allocate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*********************************************************************/
		/* [13] replace temporaries with actual registers in the output file */
		/*********************************************************************/
		try {
			registerAllocator.finalizeFile(outputFilename);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
