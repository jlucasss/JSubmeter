
package com.jsubmeter.execution;

import java.util.List;
import java.util.ArrayList;

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
	private List<String> listOutput, newSourceCode;
	
	public AbstractExecution(DataPerson data) {
		this.data = data;
		this.newSourceCode = new ArrayList<String>();
	}

	protected abstract boolean compile();
	protected abstract void run(String[] args);
	protected abstract void prepareCompilation();
	protected abstract void prepareExecution();
	protected abstract void afterExecution();
	public abstract void deleteCreatedFiles();
	protected abstract void error();

	public boolean prepareAndCompile() {

		prepareCompilation();

		timeCompilation = System.nanoTime();//start time

		boolean compiled = compile();

		timeCompilation = System.nanoTime() - timeCompilation;//final time - start time

		if (!compiled)
			error();

		return compiled;

	}

	public void execute(String[] args) {

		prepareExecution();

		timeExecution = System.nanoTime();

		run(args);

		timeExecution = System.nanoTime() - timeExecution;

		afterExecution();

	}

	public void prepareAndExecute(String[] args) {

		if (prepareAndCompile())
			execute(args);

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

	public List<String> getNewSourceCode() {
		return this.newSourceCode;
	}

	protected void setNewSourceCode(List<String> newSourceCode) {
		this.newSourceCode = newSourceCode;
	}

}

