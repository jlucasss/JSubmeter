package com.jsubmeter;

/**
 * 
 * This class serves to allow execution by the terminal.
 * 
 * @author José Lucas dos Santos da Silva
 * 
 */

import com.jsubmeter.execution.Submeter;
import com.jsubmeter.models.DataPerson;

public class Library {

	public static void main(String[] args) {

		defaultMessage(args.length);

		String pathSolutionFile = args[0],
				pathInputFolder = args[1],
				fileInputName = args[2],
				pathOutput = args[3];
		
		pathOutput += "/JSubmeter - Output/";// Create folder inside the place to not overwrite class with the same name

		DataPerson data = new DataPerson(pathSolutionFile, 
							pathInputFolder,
							fileInputName,
							pathOutput);

		Submeter submeter = new Submeter(data);

		if (args.length > 4)
			for (int i = 0; args.length > i; i++)
				switchArgs(args[i], submeter);

		try {

			submeter.submit();

		} catch(Exception e) {
			e.printStackTrace();
		}

		// Output

		submeter.saveOutputCurrent();

		System.out.println(data.toString());

	}

	private static void defaultMessage(int argsLength) {
		
		System.out.println("JSubmeter v0.0.1\n");	
		
		if (argsLength < 4) {

			System.out.println("java -jar jsubmeter-0.0.1.jar <SolutionFile> <InputFolder> <InputFileName> <OutputFolder> [options]");
			System.out.println(" <SolutionFile>: Path of the .java file(Ex:. 'C://Main.java').");
			System.out.println(" <InputFolder>: Path of the file containing the input(Ex:. 'C://input.txt').");
			System.out.println(" <InputFileName>: File name with extension(if contains) of input(Ex:. 'input.txt').");
			System.out.println(" <OutputFolder>: Path used to create temporary files and output(Ex:. 'C://').");
			System.out.println(" [options]: working specifications.");
			System.out.println("\nCan be [options]:");
			System.out.println(" --maintainTemporaries: do not delete files created for compilation and execution.");
			System.out.println(" --noSaveRuntime: don't save the runtime on output.");
			System.out.println(" --noSaveOutput: do not save console output.");
			System.out.println("Note: If all \"--noSave...\" arguments are used, nothing will be saved.\n");
			System.exit(0);
		}

	}

	private static void switchArgs(String arg, Submeter submeter) {

		switch (arg) {

			case "--maintainTemporaries":
			submeter.setDeleteTemporary(true);
			break;

			case "--noSaveRuntime":
			submeter.setFileOutputWithTimeExecution(false);
			break;

			case "--noSaveOutput":
			submeter.setFileOutputWithOutput(false);
			break;

			default:
				System.out.println("Argument does not exist.");
				System.exit(0);
			break;
		}
	}

}
