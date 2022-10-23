
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

import java.nio.file.Files;
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

	protected String className;
	protected Method method;
	
	protected String temporaryOutput;
	protected String[] parameters;

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

			// Init class

			ClassLoader classLoader = new URLClassLoader(
				new URL[] {
					new File(data.getPathOutputFolder()).toURI().toURL()
				});


			Class classe = classLoader.loadClass(className.replaceAll(".java", ""));

			this.newInstance = classe.getDeclaredConstructor().newInstance();

			// Temporary outputs of prints

			String nameFile = data.getFileInputName().replace("/", "").replace("\\", "");

			this.temporaryOutput = data.getPathOutputFolder() + "temporary-" + nameFile + ".tmp";

			File file = new File(this.temporaryOutput);

			if (!file.exists())
				file.createNewFile();

			// About method added

			this.parameters = new String[] {data.getPathInputFolder(), this.temporaryOutput};

			this.method = classe.getDeclaredMethod("main2", String.class, String.class);//parameters);//.getClass());

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

		return new JCodeModificator(this.className.replaceAll(".java", ""), // classNameOnly, 
						data.getFileInputName(),// pathInputFile, 
						data.getPathInputFolder(),// pathFolder, 
						originalSourceCode)
					.newSourceCode();

	}

}
