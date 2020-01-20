/***********/
/* PACKAGE */
/***********/
package IR;

import TEMP.*;
import java.util.Map;
import java.util.HashMap;

public class IR
{
	private IRcommand head=null;
	private IRcommandList tail=null;
	private Map<TEMP, Integer> mapTempRegtoInt; 

	/******************/
	/* Add IR command */
	/******************/
	public void Add_IRcommand(IRcommand cmd)
	{
		System.out.println ("command added");
		if ((head == null) && (tail == null))
		{
			this.head = cmd;
		}
		else if ((head != null) && (tail == null))
		{
			this.tail = new IRcommandList(cmd,null);
		}
		else
		{
			IRcommandList it = tail;
			while ((it != null) && (it.tail != null))
			{
				it = it.tail;
			}
			it.tail = new IRcommandList(cmd,null);
		}
	}
	
	public static void storeRegistersInStack(){
		IR.getInstance().Add_IRcommand(new IRcommand_PushTempRegToStack(TempRegisters.t0));
		IR.getInstance().Add_IRcommand(new IRcommand_PushTempRegToStack(TempRegisters.t1));
		IR.getInstance().Add_IRcommand(new IRcommand_PushTempRegToStack(TempRegisters.t2));
		IR.getInstance().Add_IRcommand(new IRcommand_PushTempRegToStack(TempRegisters.t3));
		IR.getInstance().Add_IRcommand(new IRcommand_PushTempRegToStack(TempRegisters.t4));
		IR.getInstance().Add_IRcommand(new IRcommand_PushTempRegToStack(TempRegisters.t5));
		IR.getInstance().Add_IRcommand(new IRcommand_PushTempRegToStack(TempRegisters.t6));
		IR.getInstance().Add_IRcommand(new IRcommand_PushTempRegToStack(TempRegisters.t7));
		IR.getInstance().Add_IRcommand(new IRcommand_PushTempRegToStack(TempRegisters.t8));
		IR.getInstance().Add_IRcommand(new IRcommand_PushTempRegToStack(TempRegisters.t9));
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		System.out.println("IR: calling IR");
		if (head != null) head.MIPSme();
		if (tail != null) 
		{
			tail.MIPSme();
		}
	}

	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static IR instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected IR() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static IR getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new IR();
		}
		return instance;
	}
	
	public Map<TEMP, Integer> getTempRegtoIntMapping() {
		return mapTempRegtoInt;
	}
	
	public void putTempRegtoIntMapping(TEMP t)
	{
		if (mapTempRegtoInt == null)
			mapTempRegtoInt = new HashMap<>();
		
		mapTempRegtoInt.put(t, 5);
	}
	
}
