
package com.jsubmeter.execution;

import java.util.List;

import com.jsubmeter.models.DataPerson;

/**
 * 
 * This class serves as a schematic basis for the functioning of the compilation and execution processes. 
 * It also prepares how compilation and execution times will be calculated.
 * 
 * @author José Lucas dos Santos da Silva
 *
 */

public abstract class AbstractExecution {

	protected DataPerson data;
	private long timeCompilation, timeExecution;
	private List<String> listOutput;
	
	public AbstractExecution(DataPerson data) {
		this.data = data;
	}

	protected abstract boolean compile();
	protected abstract void run(String[] args);
	protected abstract void prepareCompilation();
	protected abstract void prepareExecution();
	public abstract void deleteCreatedFiles();
	protected abstract void error();

	public void execute(String[] args) {

		prepareCompilation();

		timeCompilation = System.nanoTime();//start time

		boolean compiled = compile();

		timeCompilation = System.nanoTime() - timeCompilation;//final time - start time

		if (compiled) {

			prepareExecution();

			timeExecution = System.nanoTime();

			run(args);

			timeExecution = System.nanoTime() - timeExecution;

		} else
			error();

	}

	public long getTimeCompilation() {
		return timeCompilation;
	}

	public long getTimeExecution() {
		return timeExecution;
	}

	public List<String> getListOutput() {
		return listOutput;
	}

	protected void setListOutput(List<String> listOutput) {
		this.listOutput = listOutput;
	}

}

