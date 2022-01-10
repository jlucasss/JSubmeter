
package com.jsubmeter.execution;

/**
 * 
 * This class serves to receive the informed data, process them and save them.
 * 
 * @author José Lucas dos Santos da Silva
 * 
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jsubmeter.execution.lang.java.JExecution;
import com.jsubmeter.io.writer.Writer;
import com.jsubmeter.models.DataPerson;

public class Submeter {

	private DataPerson person;
	private String name;
	private boolean deleteTemporary,
				fileOutputWithTimeExecution,
				fileOutputWithOutput,
				printNewSourceCode;

	public Submeter(DataPerson person) {
		
		this.person = person;
		this.deleteTemporary = true;
		this.fileOutputWithTimeExecution = true;
		this.fileOutputWithOutput = true;
		this.printNewSourceCode = false;

	}

	public void submit() throws IOException, InterruptedException {

		//File input = new File(person.getPathInput());

		this.name = new File(person.getPathSolutionFile()).getName();

		AbstractExecution execution = defineClassExecution(this.name);

		execution.prepareAndExecute(null);

		if (isPrintNewSourceCode())
			execution.getNewSourceCode().forEach(txt -> System.out.println(txt));


		person.setListOutput(execution.getListOutput());

		person.setTimeExecution(execution.getTimeExecution());

		if (this.deleteTemporary)
			execution.deleteCreatedFiles();

	}

	private AbstractExecution preExecution;
	
	public void preSubmit() throws IOException, InterruptedException {

		File solution = new File(person.getPathSolutionFile());
		//File input = new File(person.getPathInput());

		this.name = solution.getName();

		preExecution = defineClassExecution(this.name);

		preExecution.prepareAndCompile();

		if (isPrintNewSourceCode())
			preExecution.getNewSourceCode().forEach(txt -> System.out.println(txt));

	}

	private AbstractExecution defineClassExecution(String name) {
		
		name = name.substring(name.indexOf(".")+1).toLowerCase();

		switch (name) {
		
		case "java":
			return new JExecution(person);
		
		}
		
		return null;
		
	}	

	public void finishSubmit(String fileName) {
		
		if (preExecution == null)
			throw new RuntimeException("You cold call method \'preSubmit\'.");
		
		preExecution.execute( new String[] { fileName } );

		person.setFileInputName(fileName);
		
		person.setListOutput(preExecution.getListOutput());

		person.setTimeExecution(preExecution.getTimeExecution());

		if (this.deleteTemporary)
			preExecution.deleteCreatedFiles();

	}
	
	
	public void saveOutputCurrent() {// Default

		saveOutputCurrent(this.name + ".txt");

	}

	public void saveOutputCurrent(String nameFileOutput) {

		List<String> output = new ArrayList<>();

		if (!fillOutput(output)) {// If you don't have what to fill

			System.out.println("It won't write anything, so it won't be saved.");
			return;

		}

		try {

			new Writer(person.getPathOutputFolder() + nameFileOutput)
					.writeLines(output);

		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	private boolean fillOutput(List<String> output) {
		
		if (this.isFileOutputWithTimeExecution())
			output.add("" + person.getTimeExecution());

		if (this.isFileOutputWithOutput())
			output.addAll(person.getListOutput());

		return ( this.isFileOutputWithTimeExecution() || 
			this.isFileOutputWithOutput() );

	}

	public void setDeleteTemporary(boolean deleteTemporary) {
		this.deleteTemporary = deleteTemporary;
	}

	public void setFileOutputWithTimeExecution(boolean fileOutputWithTimeExecution) {
		this.fileOutputWithTimeExecution = fileOutputWithTimeExecution;
	}

	public void setFileOutputWithOutput(boolean fileOutputWithOutput) {
		this.fileOutputWithOutput = fileOutputWithOutput;
	}

	public void setPrintNewSourceCode(boolean printNewSourceCode) {
		this.printNewSourceCode = printNewSourceCode;
	}

	public boolean isDeleteTemporary() {
		return this.deleteTemporary;
	}

	public boolean isFileOutputWithTimeExecution() {
		return this.fileOutputWithTimeExecution;
	}

	public boolean isFileOutputWithOutput() {
		return this.fileOutputWithOutput;
	}

	public boolean isPrintNewSourceCode() {
		return this.printNewSourceCode;
	}

}

