package AST;

import TYPES.*;

import java.util.LinkedHashMap;

import MIPS.sir_MIPS_a_lot;
import SYMBOL_TABLE.*;
import TEMP.*;

public class AST_CLASSDEC extends AST_DEC {
	
	public String className;
	public String superClassName;
	public AST_CFIELD_LIST body;
	public int numOfDataMembers;
	public int numOfMethods;



	public AST_CLASSDEC(String className, String superClassName, AST_CFIELD_LIST cfieldList, int lineNumber) {
		this.className = className;
		this.superClassName = superClassName;
		this.body = cfieldList;
		
		String extStr = superClassName == null ? "" : String.format(" EXTENDS ( %s )", superClassName);

		this.setLineNumber(lineNumber);
		System.out.format("====================== classDec -> CLASS ID( %s )%s LBRACE cFieldList RBRACE\n", className, extStr);
	}
	

	public void PrintMe(){
		/***************************************/
		/* AST NODE TYPE = CLASS DEC (AST NODE) */
		/***************************************/
		System.out.print("AST NODE: CLASS DEC\n");

		/******************************/
		/* RECURSIVELY PRINT body ... */
		/******************************/
		if (body != null) body.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"CLASS\nDEC");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, body.SerialNumber);
	}


	public String toString() {
		return "classDec";
	}


	public TYPE SemantMe() throws SemantException {
		int lineNumber = this.getLineNumber();
		TYPE_CLASS superClassType = null;

		// check if class extends a super class
		// if it does - check if the name of the super class is legal
		// and if it is -- find out its type
		if(this.superClassName != null){
			TYPE t = SYMBOL_TABLE.getInstance().find(superClassName, EntryCategory.Type);
			if(t == null){
				String err = String.format("class_dec: class '%s' cannot extend '%s'.\nclass '%s' does not exist.\n",
								this.className, this.superClassName, this.superClassName);
				throw new SemantException(lineNumber, err);
			}
			if(t instanceof TYPE_CLASS == false){
				String err = String.format("class_dec: class '%s' cannot extend '%s': '%s' is not a class.\n",
								this.className, this.superClassName, this.superClassName);
				throw new SemantException(lineNumber, err);
			}
			superClassType = (TYPE_CLASS) t;
		}
		
		// check if class name already exists in visible scopes
		if(SYMBOL_TABLE.getInstance().find(className, EntryCategory.Type) != null ||
		   SYMBOL_TABLE.getInstance().find(className, EntryCategory.Obj) != null){
			String err = String.format("class_dec: name '%s' was already declared.\n", this.className);
			throw new SemantException(lineNumber, err);
		}

		/*************************/
		/* [1] Begin Class Scope */
		/*************************/
		SYMBOL_TABLE.getInstance().beginScope(true);
		//add class just so we could use objects of type ClassName inside the class scope
		TYPE_CLASS tc = new TYPE_CLASS(superClassType, this.className, null);
		SYMBOL_TABLE.getInstance().enter(className, tc, EntryCategory.Type);
		SYMBOL_TABLE.getInstance().currClass = tc;

		/***************************/
		/* [2] Semant Data Members */
		/***************************/
		tc.data_members = body.SemantMe(this);
		semantClassMethods();

		numOfMethods = SYMBOL_TABLE.getInstance().getFuncCount();

		numOfDataMembers = SYMBOL_TABLE.getInstance().getVarCount();

		int objectSizeInBytes = 0;
		if(superClassType != null) { // this class extends another class
			objectSizeInBytes = superClassType.ObjSizeInBytes - 4;
		}
		objectSizeInBytes += 4 * numOfDataMembers + 4; // +4 added for the vtable pointer
		tc.ObjSizeInBytes = objectSizeInBytes;

		/*****************/
		/* [3] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().currClass = null;
		SYMBOL_TABLE.getInstance().endScope(true);

		/************************************************/
		/* [4] Enter the Class Type to the Symbol Table */
		/************************************************/
		SYMBOL_TABLE.getInstance().enter(className, tc, EntryCategory.Type);

		/*********************************************************/
		/* [5] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;		
	}

	private void semantClassMethods() throws SemantException {
		AST_CFIELD_LIST lst = this.body;

		while(lst != null) {
			AST_DEC curDec = lst.clsField.dec;
			lst = lst.clsFieldList;
			if(curDec == null || !(curDec instanceof AST_FUNCDEC)) {
				continue;
			}
			AST_FUNCDEC curFunc = (AST_FUNCDEC)curDec;
			TYPE_FUNCTION typeFunction = (TYPE_FUNCTION)SYMBOL_TABLE.getInstance().find(curFunc.name, EntryCategory.Obj);
			SYMBOL_TABLE.getInstance().beginScope(true);
			curFunc.SemantFunctionArguments();
			curFunc.body.SemantMe(typeFunction.returnType);
			curFunc.localsNum = SYMBOL_TABLE.getInstance().getVarCount();
			SYMBOL_TABLE.getInstance().endScope(true);
			curFunc.className = this.className;
		}
	}

	public void MIPSme() {
		TYPE_CLASS tc = (TYPE_CLASS)SYMBOL_TABLE.getInstance().find(this.className, EntryCategory.Type);
		
		// vtable
		LinkedHashMap<String, VtableEntry> vtable = tc.functionTable;
		sir_MIPS_a_lot mips = sir_MIPS_a_lot.getInstance();
		mips.dataLabel("Vtable_" + this.className);
		for(String str : vtable.keySet()) {
			VtableEntry entry = vtable.get(str);
			mips.addVtableEntry(entry.toString());
		}

		// class methods
		AST_CFIELD_LIST lst = this.body;
		while(lst != null) {
			AST_DEC curDec = lst.clsField.dec;
			lst = lst.clsFieldList;
			if(curDec == null || !(curDec instanceof AST_FUNCDEC)) {
				continue;
			}
			AST_FUNCDEC curFunc = (AST_FUNCDEC)curDec;
			curFunc.MIPSme();
		}
	}

}
	
