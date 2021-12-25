package com.jsubmeter.execution;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.jsubmeter.JTemporary;
import com.jsubmeter.io.reader.Reader;
import com.jsubmeter.io.writer.Writer;
import com.jsubmeter.models.DataPerson;

class SubmeterTest {

	private static JTemporary temporary;
	private static Submeter submeter;
	private static String pathOutput;
	private static DataPerson data;
	
	/*
	 * This test only prepares once for two input files (which can be more)
	 */
	
	@BeforeAll
	public static void starting() {

		temporary = new JTemporary();

		temporary.createTemporary();
		
		String pathSolutionFile = null;

		try {
			pathSolutionFile = temporary.createExampleFile();
		} catch (Exception e) {
			e.printStackTrace();
		}

		SubmeterTest.pathOutput = temporary.getPathTemporary() + "/Example.java.txt";

		// Create input files
		
		try {
		
			new Writer(temporary.getPathTemporary() + "/input1.in")
				.writeLines(Arrays.asList("Hello World!"));
			
			new Writer(temporary.getPathTemporary() + "/input2.in")
				.writeLines(Arrays.asList("Hello World! 2"));
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Create submit

		data = new DataPerson(pathSolutionFile,
							temporary.getPathTemporary(),
							"/input1.in",
							temporary.getPathTemporary().toString());

		SubmeterTest.submeter = new Submeter(data);

		try {

			submeter.preSubmit();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	void input1() {

		List<String> listOutput = null;
		
		// Submit File 1

		submeter.finishSubmit("input1.in");
		submeter.saveOutputCurrent();
		
		// Test 1
		
		try {
			listOutput = new Reader(pathOutput).readLines();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Assertions.assertEquals("Hello World!", listOutput.get(1));
		
	}

	@Test
	void input2() {

		List<String> listOutput = null;

		// Submit File 2

		submeter.finishSubmit("input2.in");
		submeter.saveOutputCurrent();
		
		// Test 2
		
		try {
			listOutput = new Reader(SubmeterTest.pathOutput).readLines();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Output = " + data.getListOutput().toString());
		
		Assertions.assertEquals("Hello World! 2", listOutput.get(1));
		
	}

}
