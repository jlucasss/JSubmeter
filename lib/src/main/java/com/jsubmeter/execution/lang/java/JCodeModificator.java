package com.jsubmeter.execution.lang.java;

import java.util.List;

public class JCodeModificator {

	public static String addImports() {
		
		StringBuilder imports = new StringBuilder();
		
		imports.append("import java.io.FileInputStream;")
			.append("import java.io.IOException;")
			.append("import java.util.List;")
			.append("import java.util.ArrayList;");
		
		return imports.toString();
		
	}
	
	/*
	 * MAIN METHOD, change to a "normal" method and call it in a new method
	 */
	public static String createNewMethodToControl() {

		StringBuilder newSourceCode = new StringBuilder();
		newSourceCode
			.append("\n//-----------Code 1 for listOutput------------------\n")
			.append("\n	public static List<String> exitToVerificationIOExec = new ArrayList<>();")
			.append("\n")
			.append("\n	public static List<String> makeExecution(String[] args) {")
			.append("\n		try {")
			.append("\n			main2(args);")
			.append("\n		} catch (Exception e) {")
			.append("\n			e.printStackTrace();")
			.append("\n		}")
			.append("\n		return exitToVerificationIOExec;")
			.append("\n	}")
			.append("\n	public static void main2(String[] args) throws Exception {")
			.append("\n\n//----------End Code 1 for listOutput--------------\n");

		return newSourceCode.toString();

	}

	/*
	 * IN THE METHOD CONTAINING System.in, Put it to read the file with the template
	 */
	public static String replaceInputToReader(String pathInputFile, String pathFolder, String originalLine) {

		StringBuilder fileTry = new StringBuilder();

		fileTry
			.append("\n//----------Code 2 for listOutput-----------------\n")
			.append("\n		FileInputStream fileInputStreamFI = null;")
			.append("\n		try{")
			.append("\n			fileInputStreamFI = new FileInputStream(\"")
			.append(pathFolder)
			.append("\" + (args[0] == null ? \"")
			.append(pathInputFile)
			.append("\" : args[0]) );")
			.append("\n		} catch(Exception e) {")
			.append("\n			e.printStackTrace();")
			.append("\n		}")
			.append("\n\n//--------End Code 2 for listOutput-----------------\n\n");

		String out = fileTry.toString() + originalLine.replaceAll("System.in", "fileInputStreamFI");
		
		return out;
		
	}

	/*
	 * All cases of types of print(ln, f and just print)
	 */
	public static String replaceAllCasesOfPrint(String line) {

		String newPrintOutput = "";

		if (line.contains("println"))
			newPrintOutput = line.replaceFirst("System.out.println\\(", "exitToVerificationIOExec.add(\"\" + "); 

		else if (line.contains("printf")) {

			String content = line.trim().substring("System.out.printf(".length());

			content = removePrintfFormat(content) + ");";//str.substring(index);

			newPrintOutput = "exitToVerificationIOExec.add(\"\" + " + content;

		} else if (line.contains("print"))
			newPrintOutput = line.replaceAll("System.out.print\\(", 
					"exitToVerificationIOExec.add(\"\" + "); 

		return newPrintOutput;

	}

	/*
	 * If you didn't find an empty constructor and it's already on the last line
	 */
	public static String createEmptyConstructor(String originalCodeLine, String classNameOnly) {

		StringBuilder code = new StringBuilder();

		code
			.append("\n//----------Code 3 for Constructor-----------------\n")
			.append(originalCodeLine)
			.append("\n	public ")
			.append(classNameOnly)
			.append("() {}\n")
			.append("\n\n//--------End Code 3 for Constructor-----------------\n\n");

		return code.toString();
		
	}

	public static String removePrintfFormat(String str) {

		char[] array = str.toCharArray();
		
		char mainQuote = array[0];
		
		int indexLastMainQuote = -1;

		indexLastMainQuote = findLastMainQuote(array, mainQuote, indexLastMainQuote);
		
		return replaceAllReferencesPrintf(str, mainQuote, indexLastMainQuote+1);

	}

	private static String replaceAllReferencesPrintf(String str, char mainQuote, int indexLastMainQuote) {
		
		char[] array;
		
		String formatPrintf = str.substring(0, indexLastMainQuote);// text: "&s"

		String argsPrintf = str.substring(formatPrintf.length()).trim(); // content: ", name, ...
		argsPrintf = argsPrintf.substring(1, argsPrintf.length()-2).trim();// Remove first ",", ");" and spaces
				
		String[] splitVarArgsPrintf = argsPrintf.split(",");

		int indexReference,
			countReferences = 0;

		// Replace "&"s by variables
		for (int i = 0; splitVarArgsPrintf.length > i; i++) {
			
			array = formatPrintf.toCharArray();
			indexReference = formatPrintf.indexOf("&", countReferences);
			
			if (indexReference > -1) {
			
				if (array[indexReference+1] == '&')
					continue;
				
				// Replace the first "&" as to find
				formatPrintf = formatPrintf.replaceFirst("&"+(array[indexReference+1]), 
						(mainQuote + " + " + splitVarArgsPrintf[i].trim() + " + " + mainQuote) );
				
				indexReference = i;
				
			} else 
				break;
			
		}

		return formatPrintf;

	}

	protected static int findLastMainQuote(char[] array, char mainQuote, int indexLastMainQuote) {

		// Find index of last main quote
		for (int i = 1; array.length > i; i++)
			if (array[i] == mainQuote) {
				
				if (array[i] == "\\".toCharArray()[0])
					continue;
				else {
		
					indexLastMainQuote = i;
					break;
				
				}
				
			}
		return indexLastMainQuote;

	}

	public static int removeComents(List<String> array, int i) {
		
		String lineCurrent = array.get(i);
		
		if (lineCurrent.contains("//") &&
				!checkIfIsIntoAspas(lineCurrent, "//")) {

			lineCurrent = lineCurrent.substring(0, lineCurrent.indexOf("//"));
			array.set(i, lineCurrent);
		}

		// Serves for " /* " and " /** "
		if (lineCurrent.contains("/*")) {
		
			int lineFinish = -1;
			
			// Find finish of " /* "
			for (int j = i; array.size() > j; j++)
				if (array.get(j).contains("*/")) {
					lineFinish = j;
					break;
				}

			// If it's into same line
			if (i == lineFinish) {
				
				String firstPart = lineCurrent.substring(0, lineCurrent.indexOf("/*"));
				String secondPart = lineCurrent.substring(lineCurrent.indexOf("*/")+2); // Count +2 of "*/"
			
				lineCurrent = firstPart + secondPart;
				array.set(i, lineCurrent);
				
			} else {
				
				String line = lineCurrent;
				
				// Remove of first line
				line = line.substring(0, line.indexOf("/*"));
				array.set(i, line);
				
				// Clean lines as no contains " *\ "
				for (int j = i+1; lineFinish > j; j++)
					array.set(j, "");
				
				// Remove of last line
				line = array.get(lineFinish);
				line = line.substring(line.indexOf("*/")+2); // Count +2 of "*/"
				array.set(lineFinish, line);
				
			}
			
		}
		
		return i;
		
	}

	public static boolean checkIfIsIntoAspas(String line, String part) {

		int indexPart = line.indexOf(part);

		line = line.replaceAll("\\\\\"", ""); // Remove aspas no contables
		
		char[] array = line.toCharArray();
		
		int countAspa = 0;

		char aspaDupla = "\"".toCharArray()[0];

		for (int i = 0; indexPart >= i; i++) {

			// Find aspas
			
			if (array[i] == aspaDupla)
				countAspa++;

			// If (i is in "part") and (found some aspa) and (number of aspas found is impar, aspas no closed) 
			if ( (i == indexPart) && 
					(countAspa > 0) && 
					(countAspa % 2 != 0) )
				return true;
			
		}
		
		return false;

	}

	/*private boolean copyClass(String origin, String destiny) {

		try {

			Files.copy(new File(origin).toPath(), new FileOutputStream(new File(destiny)));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;

	}*/

	
}
