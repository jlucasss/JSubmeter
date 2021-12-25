
package com.jsubmeter.models;

/**
 * 
 * This class serves to centralize and organize the main data required for compilation, execution and output.
 * 
 * @author José Lucas dos Santos da Silva
 * 
 */

import java.util.List;

import com.jsubmeter.util.ManipulatorString;

public class DataPerson {

	private final String pathSolutionFile, pathInputFolder, pathOutputFolder;
	private String fileInputName;
	private List<String> listOutput;
	private long timeExecution;

	public DataPerson(String pathSolutionFile, String pathInputFolder, String fileInputName, String pathOutputFolder) {

		this.pathSolutionFile = ManipulatorString.swap(pathSolutionFile);
		this.pathInputFolder = ManipulatorString.swap(pathInputFolder);
		this.fileInputName = fileInputName;
		this.pathOutputFolder = ManipulatorString.swap(pathOutputFolder);
		this.timeExecution = -1;

	}

	//gets finals

	public String getPathSolutionFile() {
		return this.pathSolutionFile;
	}

	public String getPathInputFolder() {
		return this.pathInputFolder;
	}

	public String getPathOutputFolder() {
		return this.pathOutputFolder;
	}

	// Others gets and sets

	public void setListOutput(List<String> listOutput) {
		this.listOutput = listOutput;
	}
	
	public List<String> getListOutput() {
		return listOutput;
	}
	
	public void setTimeExecution(long timeExecution) {
		this.timeExecution = timeExecution;
	}
	
	public long getTimeExecution() {
		return timeExecution;
	}

	public String getFileInputName() {
		return fileInputName;
	}

	public void setFileInputName(String fileInputName) {
		this.fileInputName = fileInputName;
	}

	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("[\n")
			.append(this.getPathOutputFolder())
			.append("\n")
			.append(this.getPathSolutionFile())
			.append("\n")
			.append(this.getPathInputFolder())
			.append("\n")
			.append(this.getTimeExecution())
			.append("\n]\n")
			.append("[");

		this.getListOutput().forEach(e -> sb.append("\n").append(e));
		sb.append("\n]\n");

		return sb.toString();//super.toString();
	}

}

