
package com.jsubmeter;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jsubmeter.io.reader.Reader;
import com.jsubmeter.io.writer.Writer;

public class LibraryTest {

	private JTemporary temporary;

	@BeforeEach
	public void starting() {

		temporary = new JTemporary();

		temporary.createTemporary();

	}

	@AfterEach
	public void finishing() {

		//temporary.deleteTemporary();

	}

	@Test
	void testWithoutArguments() {

		String pathInput = null;

		try {
			pathInput = temporary.createExampleFile();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String[] args = new String[] {
			new File(pathInput).toString(), // Path Solution file
			new File(temporary.getPathTemporary()).toString(), // Path folder input file
			"input.in", // Input file
			new File(temporary.getPathTemporary()).toString() // Output
		};

		String pathOutput = temporary.getPathTemporary() + "/JSubmeter - Output/Example.java.txt";

		try {
			new Writer(args[1]+args[2]).writeLines(Arrays.asList("Hello World!"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Arguments = "+args[1]);

		Library.main(args);

		List<String> listOutput = null;

		try {
			listOutput = new Reader(pathOutput).readLines();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//String timeExecution = saida.get(0);

		//Assertions.assertThrows(NumberFormatException.class, 
		//	() -> Long.parseLong(time));
		Assertions.assertEquals("Hello World!", listOutput.get(1));

	}

}

