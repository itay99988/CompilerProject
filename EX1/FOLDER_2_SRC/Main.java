   
import java.io.*;
import java.io.PrintWriter;
import java_cup.runtime.Symbol;
   
public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Symbol s;
		FileReader file_reader;
		PrintWriter file_writer;
		String inputFilename = argv[0];
		String outputFilename = argv[1];
		
		try
		{
			/********************************/
			/* [1] Initialize a file reader */
			/********************************/
			file_reader = new FileReader(inputFilename);

			/********************************/
			/* [2] Initialize a file writer */
			/********************************/
			file_writer = new PrintWriter(outputFilename);
			
			/******************************/
			/* [3] Initialize a new lexer */
			/******************************/
			l = new Lexer(file_reader);

			/***********************/
			/* [4] Read next token */
			/***********************/
			s = l.next_token();

			/********************************/
			/* [5] Main reading tokens loop */
			/********************************/
			while (s.sym != TokenNames.EOF)
			{
				/************************/
				/* [6] Print to console */
				/************************/
				
				if (s.sym == TokenNames.ERROR)
				{
					file_writer.close();
					file_writer = new PrintWriter(outputFilename);
					file_writer.print("ERROR");
					file_writer.close();
					return;
				}
				
				file_writer.print(GetTypeName(s.sym));
				if (s.sym == TokenNames.NUMBER || s.sym == TokenNames.ID || s.sym == TokenNames.STRING)
					file_writer.print("("+s.value+")");
				file_writer.print("[");
				file_writer.print(l.getLine());
				file_writer.print(",");
				file_writer.print(l.getTokenStartPosition());
				file_writer.print("]");
				file_writer.print("\n");
				
				/***********************/
				/* [8] Read next token */
				/***********************/
				s = l.next_token();
			}
			
			/******************************/
			/* [9] Close lexer input file */
			/******************************/
			l.yyclose();

			/**************************/
			/* [10] Close output file */
			/**************************/
			file_writer.close();
    	}
			     
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	static public String GetTypeName(int type)
	{
		switch (type)
		{
			case TokenNames.CLASS:
				return "CLASS";
			case TokenNames.ARRAY:
				return "ARRAY";
			case TokenNames.EXTENDS:
				return "EXTENDS";
			case TokenNames.RETURN:
				return "RETURN";
			case TokenNames.WHILE:
				return "WHILE";
			case TokenNames.IF:
				return "IF";
			case TokenNames.NEW:
				return "NEW";
			case TokenNames.NIL:
				return "NIL";
			case TokenNames.LPAREN:
				return "LPAREN";
			case TokenNames.RPAREN:
				return "RPAREN";
			case TokenNames.LBRACK:
				return "LBRACK";
			case TokenNames.RBRACK:
				return "RBRACK";
			case TokenNames.LBRACE:
				return "LBRACE";
			case TokenNames.RBRACE:
				return "RBRACE";
			case TokenNames.PLUS:
				return "PLUS";
			case TokenNames.MINUS:
				return "MINUS";
			case TokenNames.TIMES:
				return "TIMES";
			case TokenNames.DIVIDE:
				return "DIVIDE";
			case TokenNames.COMMA:
				return "COMMA";
			case TokenNames.DOT:
				return "DOT";
			case TokenNames.SEMICOLON:
				return "SEMICOLON";
			case TokenNames.ELLIPSIS:
				return "ELLIPSIS";
			case TokenNames.ASSIGN:
				return "ASSIGN";
			case TokenNames.EQ:
				return "EQ";
			case TokenNames.LT:
				return "LT";
			case TokenNames.GT:
				return "GT";
			case TokenNames.NUMBER:
				return "INT";
			case TokenNames.ID:
				return "ID";
			case TokenNames.STRING:
				return "STRING";
			default:
				return "";
		}
	}
}