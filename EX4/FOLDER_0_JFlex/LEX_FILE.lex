/***************************/
/* FILE NAME: LEX_FILE.lex */
/***************************/

/***************************/
/* AUTHOR: OREN ISH SHALOM */
/***************************/

/*************/
/* USER CODE */
/*************/
   
import java_cup.runtime.*;

/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/
      
%%
   
/************************************/
/* OPTIONS AND DECLARATIONS SECTION */
/************************************/
   
/*****************************************************/ 
/* Lexer is the name of the class JFlex will create. */
/* The code will be written to the file Lexer.java.  */
/*****************************************************/ 
%class Lexer

/********************************************************************/
/* The current line number can be accessed with the variable yyline */
/* and the current column number with the variable yycolumn.        */
/********************************************************************/
%line
%column
    
/*******************************************************************************/
/* Note that this has to be the EXACT smae name of the class the CUP generates */
/*******************************************************************************/
%cupsym TokenNames

/******************************************************************/
/* CUP compatibility mode interfaces with a CUP generated parser. */
/******************************************************************/
%cup
   
/****************/
/* DECLARATIONS */
/****************/
/*****************************************************************************/   
/* Code between %{ and %}, both of which must be at the beginning of a line, */
/* will be copied letter to letter into the Lexer class code.                */
/* Here you declare member variables and functions that are used inside the  */
/* scanner actions.                                                          */  
/*****************************************************************************/   
%{
	/*********************************************************************************/
	/* Create a new java_cup.runtime.Symbol with information about the current token */
	/*********************************************************************************/
	private Symbol symbol(int type)               {return new Symbol(type, yyline, yycolumn);}
	private Symbol symbol(int type, Object value) {return new Symbol(type, yyline, yycolumn, value);}

	/*******************************************/
	/* Enable line number extraction from main */
	/*******************************************/
	public int getLine()    { return yyline + 1; } 
	public int getCharPos() { return yycolumn;   } 
%}

/***********************/
/* MACRO DECALARATIONS */
/***********************/

LineTerminator	= \r|\n|\r\n
WhiteSpace		= [ \t\f]
INTEGER 		= [0-9] | [1-9][0-9] | [1-9][0-9][0-9] | [1-9][0-9][0-9][0-9] | [1-2][0-9][0-9][0-9][0-9] | 3[0-1][0-9][0-9][0-9]| 32[0-6][0-9][0-9] | 327[0-5][0-9] | 3276[0-8]
ZEROS           = 00+
SEVERALINTS     = {INTEGER}{INTEGER}+
NONINTEGER      = {ZEROS} | {SEVERALINTS}
LPAREN 			= \( 
RPAREN 			= \)
LBRACK			= \[
RBRACK 			= \]
LBRACE			= \{
RBRACE			= \}
PLUS			= \+
MINUS			= \-
TIMES			= \*
DIVIDE			= \/
DOT				= \.
COMMA			= \,
SEMICOLON		= \;
ELLIPSIS		= \.\.\.
ASSIGN			= \:\=
EQ				= \=
LT				= \<
GT				= \>
ID				= [a-z|A-Z]+[a-z|A-Z|0-9]*
InputCharacter = [0-9]|[a-z]|[A-Z]| {WhiteSpace} |{LPAREN} | {RPAREN} | {LBRACK} | {RBRACK} | {LBRACE} | {RBRACE} | \? | \! | {PLUS} | {MINUS} | {TIMES} | {DIVIDE} | {DOT} | {SEMICOLON} 
TraditionalCharacter = {InputCharacter}|{LineTerminator}

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment} //| {EndOfFileComment}

TraditionalComment   = "/*"{TraditionalCharacter}*"*/" 

// Comment can be the last line of the file, without line terminator.
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}?

STRING 			= \"[a-zA-Z]*\"
   
/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************************************/
/* LEXER matches regular expressions to actions (Java code) */
/************************************************************/
   
/**************************************************************/
/* YYINITIAL is the state at which the lexer begins scanning. */
/* So these regular expressions will only be matched if the   */
/* scanner is in the start state YYINITIAL.                   */

/**************************************************************/

/****************** our code **************/
<YYINITIAL> {
"class"				{ return symbol(TokenNames.CLASS) ;}
"nil"				{ return symbol(TokenNames.NIL) ;}
"array"				{ return symbol(TokenNames.ARRAY) ;}
"while"				{ return symbol(TokenNames.WHILE) ;}
"extends"			{ return symbol(TokenNames.EXTENDS) ;}
"return"			{ return symbol(TokenNames.RETURN) ;}
"new"				{ return symbol(TokenNames.NEW) ;}
"if"				{ return symbol(TokenNames.IF) ;}
{LT}				{ return symbol(TokenNames.LT);}
{DIVIDE}			{ return symbol(TokenNames.DIVIDE);}
{LPAREN}			{ return symbol(TokenNames.LPAREN);}
{RPAREN}			{ return symbol(TokenNames.RPAREN);}
{RBRACK}			{ return symbol(TokenNames.RBRACK);}
{LBRACK}			{ return symbol(TokenNames.LBRACK);}
{LBRACE}			{ return symbol(TokenNames.LBRACE);}
{RBRACE}			{ return symbol(TokenNames.RBRACE);}
{PLUS}				{ return symbol(TokenNames.PLUS);}
{MINUS}				{ return symbol(TokenNames.MINUS);}
{TIMES}				{ return symbol(TokenNames.TIMES);}
{DIVIDE}			{ return symbol(TokenNames.DIVIDE);}
{DOT}				{ return symbol(TokenNames.DOT);}
{COMMA}				{ return symbol(TokenNames.COMMA);}
{ELLIPSIS}			{ return symbol(TokenNames.ELLIPSIS);}
{ASSIGN}			{ return symbol(TokenNames.ASSIGN);}
{EQ}				{ return symbol(TokenNames.EQ);}
{GT}				{ return symbol(TokenNames.GT);}
{SEMICOLON}			{ return symbol(TokenNames.SEMICOLON);}
{INTEGER}			{ return symbol(TokenNames.INT, new Integer(yytext()));}
{ID}				{ return symbol(TokenNames.ID,     new String( yytext()));}   
{STRING}			{ return symbol(TokenNames.STRING,     new String( yytext()));}   
{WhiteSpace}		{ /* just skip what was found, do nothing */ }
{LineTerminator}		{ /* just skip what was found, do nothing*/  }

 /* comments */
      {Comment}                      { /* ignore */ }
<<EOF>>				{ return symbol(TokenNames.EOF);}
[^]					{ return symbol(TokenNames.ERROR);}
{NONINTEGER}	    { return symbol(TokenNames.ERROR);}
}