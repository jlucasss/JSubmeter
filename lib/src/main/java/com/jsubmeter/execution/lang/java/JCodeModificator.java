package com.jsubmeter.execution.lang.java;

import java.util.List;
import java.util.ArrayList;

public class JCodeModificator {

	private String nameClass, nameMain2, 
			nameGetFileInput, pathInputFile, pathFolder;

	private List<String> codeOriginal;

	public JCodeModificator(String nameClass, String pathInputFile, String pathFolder, List<String> codeOriginal) {

		this.nameClass = nameClass;
		this.pathInputFile = pathInputFile;
		this.pathFolder = pathFolder;
		this.codeOriginal = codeOriginal;
		this.nameMain2 = "main2";

	}

	public List<String> newSourceCode() {

		// Inits

		String lineCurrent;

		List<String> array = new ArrayList<>();

		array.add(addImports());
		
		array.addAll(this.codeOriginal);

		// Remove all comments in code
		for (int i = 0; array.size() > i; i++) 
			i = removeComents(array, i);

		// Fors

		for (int i = 0; array.size() > i; i++) {

			lineCurrent = array.get(i);
			
			if (lineCurrent.contains("package ")) //Find, if exist, and remove package
				array.set(i, "");
			
			else if (lineCurrent.contains("class " + this.nameClass)) {
				int point = indexFirstOfArray(array, i, "{");

				String addedMethodMain2 = array.get(point) + createMethodMain2();
				// init method of input file
				array.set(point, addedMethodMain2);

				break; // stop for when complete

			}

		}

		return array;

	}


	public String addImports() {
		
		List<String> imports = new ArrayList<>();
		
		imports.add("import java.io.PrintStream;");
		imports.add("import java.io.FileOutputStream;");
		imports.add("import java.io.FileInputStream;");
		imports.add("import java.io.IOException;");

		removeAllExistingImports(imports);

		String out = "\n";

		for (int i = 0; imports.size() > i; i++)
			out += imports.get(i) + "\n"; 

		return out; 
		
	}

	private void removeAllExistingImports(List<String> imports) {

		List<String> importsExisting = new ArrayList<>();

		// Add list imports
		for (int i = 0; this.codeOriginal.size() > i; i++) {

			if (this.codeOriginal.get(i).contains("import"))
				addImportsInList(importsExisting, this.codeOriginal.get(i));

			if (this.codeOriginal.get(i).contains("class")) // break when found "class"
				break;

		}

		// Remove imports existings
		imports.removeAll(importsExisting);

	}

	private void addImportsInList(List<String> list, String line) {

		String[] imports = line.trim().split(";"); // Check if exist more one import in one line

		for (int i = 0; imports.length > i; i++) // Add one or more imports existing
			list.add(imports[i] + ";"); // The split remove ";", therefore add again

	}

	public String createMethodMain2() {

		StringBuilder newSourceCode = new StringBuilder();
		newSourceCode
			.append("\n")
			.append("\n	public static PrintStream " + this.nameMain2 + "(String input, String output) {")
			.append("\n		PrintStream fileOutput = null;")
			.append("\n		try {")
			.append("\n			FileInputStream fileInput = new FileInputStream(input);")
			.append("\n			fileOutput = new PrintStream(new FileOutputStream(output));")
			.append("\n			System.setIn(fileInput);")
			.append("\n			System.setOut(fileOutput);")
			.append("\n			main(null);")
			.append("\n		} catch (Exception e) {")
			.append("\n			e.printStackTrace();")
			.append("\n		}")
			.append("\n		return fileOutput;")
			.append("\n	}");

		return newSourceCode.toString();

	}

	/*
	 * IN THE METHOD CONTAINING System.in, Put it to read the file with the template
	 */
	public String replaceInputToReader(String originalLine) {

		return originalLine.replaceAll("System.in", 
					(this.nameClass + "." + this.nameGetFileInput + "()") );
		
	}

	/* 
	 * Return first occurrence of "str" in array, starting in "init" 
	 */
	public int indexFirstOfArray(List<String> list, int init, String str) {

		for (int i = init; list.size() > i; i++)
			if (list.get(i).contains(str))
				return i;

		return -1;

	}

	public int removeComents(List<String> array, int i) {
		
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

	private boolean checkIfIsIntoAspas(String line, String part) {

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


	/* Getters */

	public String getNameClass() {
		return this.nameClass;
	}

	public String getNameMain2() {
		return this.nameMain2;
	}

	public String getNameGetFileInput() {
		return this.nameGetFileInput;
	}

}
