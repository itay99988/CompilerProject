   
import java.io.*;
import java.io.PrintWriter;
import java_cup.runtime.Symbol;
import AST.*;

public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Parser p;
		Symbol s;
		boolean isException = false;
		AST_PROGRAM AST = null;
		FileReader file_reader = null;
		PrintWriter file_writer = null;
		String inputFilename = argv[0];
		String outputFilename = argv[1];
			/********************************/
			/* [1] Initialize a file reader */
			/********************************/
			
			try{
			file_reader = new FileReader(inputFilename);

			/********************************/
			/* [2] Initialize a file writer */
			/********************************/
			file_writer = new PrintWriter(outputFilename);
			}
			catch(FileNotFoundException e){
				System.out.println("file not found Exception");
				isException = true;
				e.printStackTrace();
			}
				
		
			/******************************/
			/* [3] Initialize a new lexer */
			/******************************/
		try{
			System.out.println("step 3");

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
		}
		catch (Exception e) /* parseException */
		{
			System.out.println("parseException");
			isException = true;
			e.printStackTrace();
		}
		try{
			System.out.println("step 4");

			/**************************/
			/* [7] Semant the AST ... */
			/**************************/
			AST.SemantMe();
			
		}
		catch (SemantException se) 
		{
			isException = true;
			file_writer.print(String.format("ERROR(%d)",se.getLine()));
			file_writer.close();
			se.printStackTrace();
			System.exit(0);
		}
			/*************************/
			/* [8] Close output file */
			/*************************/
		try{
			if (!isException)
			{
				file_writer.print("OK");
				System.out.println("OK");
			}
			System.out.println("step 8");
			file_writer.close();

			/*************************************/
			/* [9] Finalize AST GRAPHIZ DOT file */
			/*************************************/
			AST_GRAPHVIZ.getInstance().finalizeFile();			
			}
		catch (Exception e)
			{
				e.printStackTrace();
			}
    	}
			     
	}



