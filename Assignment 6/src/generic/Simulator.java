package generic;

import processor.Clock;
import processor.Processor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;
import generic.Instruction.OperationType;
import generic.Operand.OperandType;
import processor.memorysystem.MainMemory;
import processor.pipeline.RegisterFile;
import java.lang.Math;
import java.io.*;


public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	static EventQueue eventQueue;
	static HashMap<String, Integer> symtab = new HashMap<String,Integer>();
	
	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		eventQueue = new EventQueue();
		simulationComplete = false;
	}

	public static EventQueue getEventQueue ( )
	{
		return eventQueue ;
	}
	
	static void loadProgram(String assemblyProgramFile)
	{
		/*
		 * TODO
		 * 1. load the program into memory according to the program layout described
		 *    in the ISA specification
		 * 2. set PC to the address of the first instruction in the main
		 * 3. set the following registers:
		 *     x0 = 0
		 *     x1 = 65535
		 *     x2 = 65535
		 */
		MainMemory mainMemory = processor.getMainMemory();
		RegisterFile registerFile = processor.getRegisterFile();
		FileInputStream fis = null;
		DataInputStream dis = null;
		try
		{
			fis = new FileInputStream(assemblyProgramFile);
			dis = new DataInputStream(fis);
			int val=dis.readInt(),i = 0;
			while(dis.available() > 0){
				mainMemory.setWord(i,dis.readInt());
				i++;
			}
			registerFile.setProgramCounter(val);
			// System.out.println("yy"+val);
			registerFile.setValue(1,65535);
			registerFile.setValue(2,65535);
			processor.setRegisterFile(registerFile);
			processor.setMainMemory(mainMemory);
		}
		catch (IOException ex) {
    		ex.printStackTrace();
		}				
	}
	
	public static void simulate()
	{
		int cycles = 0;
		while(simulationComplete == false)
		{
			processor.getRWUnit().performRW();
			processor.getMAUnit().performMA();
			processor.getEXUnit().performEX();
			eventQueue.processEvents();
			processor.getOFUnit().performOF();
			processor.getIFUnit().performIF();
			Clock.incrementClock();
			cycles++;
			// System.out.println(Clock.getCurrentTime()+"..............................");
		}
		
		// TODO
		// set statistics
		Statistics stats = new Statistics();
		// stats.setNumberOfInstructions(processor.getNumIns());
		// stats.setNumberOfCycles(processor.getNumCycles());
		stats.setNumberOfCycles(cycles);
		// System.out.println(processor.getNumIns());
		// System.out.println(processor.getNumCycles());
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
	
}