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

	public static final String VERSION = "0.0.4";
	public static final int INIT_OPTIONS = 5;

	public static void main(String[] args) {

		defaultMessage(args.length);

		String pathSolutionFile = args[0],
				pathInputFolder = args[1],
				fileInputName = args[2],
				pathOutput = args[3],
				maxRuntime = args[4];
		
		pathOutput += "/JSubmeter - Output/";// Create folder inside the place to not overwrite class with the same name

		DataPerson data = new DataPerson(pathSolutionFile, 
							pathInputFolder,
							fileInputName,
							pathOutput);

		Submeter submeter = new Submeter(data);

		if (args.length > Library.INIT_OPTIONS)
			for (int i = Library.INIT_OPTIONS; args.length > i; i++)
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
		
		System.out.println("JSubmeter v" + VERSION + "\n");	
		
		if (argsLength < Library.INIT_OPTIONS) {

			System.out.println("java -jar jsubmeter-" + VERSION + ".jar <SolutionFile> <InputFolder> <InputFileName> <OutputFolder> <MaxRuntime> [options]");
			System.out.println(" <SolutionFile>: Path of the .java file(Ex:. 'C://Main.java').");
			System.out.println(" <InputFolder>: Path of the file containing the input(Ex:. 'C://input.txt').");
			System.out.println(" <InputFileName>: File name with extension(if contains) of input(Ex:. 'input.txt').");
			System.out.println(" <OutputFolder>: Path used to create temporary files and output(Ex:. 'C://').");
			System.out.println(" <MaxRuntime>: Maximum runtime of solution file in nanoseconds(Ex:. '1000000000').");
			System.out.println(" [options]: working specifications.");
			System.out.println("\nCan be [options]:");
			System.out.println(" --maintainTemporaries: do not delete files created for compilation and execution.[default: true]");
			System.out.println(" --noSaveRuntime: don't save the runtime on output.[default: true]");
			System.out.println(" --noSaveOutput: do not save console output.[default: true]");
			System.out.println(" --printNewSourceCode: prints new source code (if the current one has been modified).[default: false]");
			System.out.println("Note: If all \"--noSave...\" arguments are used, nothing will be saved.\n");
			System.exit(0);
		}

	}

	private static void switchArgs(String arg, Submeter submeter) {

		switch (arg.trim()) {

			case "--maintainTemporaries":
			submeter.setDeleteTemporary(true);
			break;

			case "--noSaveRuntime":
			submeter.setFileOutputWithTimeExecution(false);
			break;

			case "--noSaveOutput":
			submeter.setFileOutputWithOutput(false);
			break;

			case "--printNewSourceCode":
			submeter.setPrintNewSourceCode(true);
			break;

			default:
				System.out.println("Argument \"" + arg + "\" does not exist.");
				System.exit(0);
			break;
		}
	}

}
