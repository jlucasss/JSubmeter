
package com.jsubmeter.execution.lang.java;

/**
 * 
 * This class modifies the solution class so that it doesn't print the output, 
 * 	but all the content is stored in a list (the "exitToVerificationIOExec") 
 * 	and written to a file of the variable "newClassPath".
 * 
 * @author José Lucas dos Santos da Silva
 * 
 */

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import com.jsubmeter.execution.AbstractExecution;
import com.jsubmeter.io.reader.Reader;
import com.jsubmeter.io.writer.Writer;
import com.jsubmeter.models.DataPerson;

public abstract class JAbstractIOExecution extends AbstractExecution {

	//protected String pathInput, pathSolutionFile, pathOutputCurrent, 
	protected String className;
	protected Method method;
	
	public JAbstractIOExecution(DataPerson data) {

			super(data);
			this.className = new File(data.getPathSolutionFile()).getName();

	}

	@Override
	protected abstract boolean compile();
	@Override
	protected abstract void run(String[] args);
	@Override
	protected abstract void error();

	protected Object newInstance;

	@Override
	protected void prepareCompilation() {

		try {

			// Create new class
			List<String> sourceCode = new Reader(data.getPathSolutionFile()).readLines();
			this.setNewSourceCode(createNewSourceCode(sourceCode));

			// this.newSourceCode.forEach(txt -> System.out.println(txt));

			//Path new class
			String newClassPath = data.getPathOutputFolder() + this.className;

			new Writer(newClassPath).writeLines(this.getNewSourceCode());

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}

	@SuppressWarnings({ "rawtypes", "resource", "unchecked" })
	@Override
	protected void prepareExecution() {

		try {

			ClassLoader classLoader = new URLClassLoader(
				new URL[] {
					new File(data.getPathOutputFolder()).toURI().toURL()
				});


			Class classe = classLoader.loadClass(className.replaceAll(".java", ""));

			this.newInstance = classe.getDeclaredConstructor().newInstance();

			String[] parameters = {};

			this.method = classe.getDeclaredMethod("makeExecution", parameters.getClass());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteCreatedFiles() {// Deletes compilation and execution files (does not delete listOutput)

		String newClassPath = data.getPathOutputFolder() + this.className;

		new File(newClassPath).deleteOnExit();// Delete .java
		new File(newClassPath.replaceAll(".java", ".class")).deleteOnExit();// Delete .class

	}

	private List<String> createNewSourceCode(List<String> originalSourceCode) {

		int pointClassInit = -1;

		boolean findConstructor = false;

		String classNameOnly = this.className.replaceAll(".java", "");
		
		String pathInputFile = data.getFileInputName();
		String pathFolder = data.getPathInputFolder();
		
		List<String> array = new ArrayList<>();
		
		array.add(JCodeModificator.addImports());
		
		array.addAll(originalSourceCode);

		String lineCurrent;

		// Remove all comments in code
		for (int i = 0; array.size() > i; i++) 
			i = JCodeModificator.removeComents(array, i);
		
		for (int i = 0; array.size() > i; i++) {

			lineCurrent = array.get(i);
			
			if (lineCurrent.contains("package ")) //Find, if exist, and remove package
				array.set(i, "");
			
			else if (lineCurrent.contains("class " + classNameOnly)) // Find "class" that of the class and mark its line to, if necessary, use it to create an empty constructor
				pointClassInit = i;
			
			else if (lineCurrent.contains("public " + classNameOnly + "()")) // Search for parameterless constructors in the class 
				findConstructor = true;
			
			else if (lineCurrent.contains("public static void main(")) 
				array.set(i, JCodeModificator.createNewMethodToControl());

			else if (lineCurrent.contains("System.in")) 
				array.set(i, JCodeModificator.replaceInputToReader(pathInputFile, pathFolder, array.get(i)) );

			else if (lineCurrent.contains("System.out.print"))
				array.set(i, JCodeModificator.replaceAllCasesOfPrint(array.get(i)));

			if (!findConstructor && (i == (array.size()-1) ) ) 
				array.set(pointClassInit, 
							JCodeModificator.createEmptyConstructor(array.get(pointClassInit), classNameOnly)
						);

		}

		return array;

	}

}
