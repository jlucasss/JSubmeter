
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
import com.jsubmeter.JTemporary.EnumAction;

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

	/* Reader examples */

	@Test
	void readerExample1() {
		Assertions.assertEquals("Hello World!", readerExampleX(1));
	}

	@Test
	void readerExample2() {
		Assertions.assertEquals("Hello World!", readerExampleX(2));
	}

	@Test
	void readerExample3() {
		Assertions.assertEquals("Hello World!", readerExampleX(3));
	}

	@Test
	void readerExample4() {
		Assertions.assertEquals("Hello World!", readerExampleX(4));
	}

	@Test
	void readerExample5() {
		Assertions.assertEquals("Hello World!", readerExampleX(5));
	}

	/* Writer examples */

	@Test
	void writerExample1() {
		Assertions.assertEquals("Hello World!", writerExampleX(1));
	}

	@Test
	void writerExample2() {
		Assertions.assertEquals("Hello World!", writerExampleX(2));
	}

	@Test
	void writerExample3() {
		Assertions.assertEquals("Hello World!", writerExampleX(3));
	}

	@Test
	void writerExample4() {
		Assertions.assertEquals("Hello World!", writerExampleX(4));
	}

	/* Base from examples */

	private String readerExampleX(int number) {

		try {

			String path = temporary.createClassExampleFile(EnumAction.READER, "Example"+number);

			List<String> output = exampleWithoutArguments(path, "Example"+number+".java.txt", 
								(List<String>) Arrays.asList("Hello World!"));

			return output.get(1);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	private String writerExampleX(int number) {

		try {

			String path = temporary.createClassExampleFile(EnumAction.WRITER, "Example"+number);

			List<String> output = exampleWithoutArguments(path, "Example"+number+".java.txt", 
								(List<String>) Arrays.asList("Hello World!"));

			return output.get(1);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	private List<String> exampleWithoutArguments(String pathInput, String outputFileName, List<String> lines) {

		String[] args = new String[] {
			new File(pathInput).toString(), // Path Solution file
			new File(temporary.getPathTemporary()).toString(), // Path folder input file
			"input.in", // Input file
			new File(temporary.getPathTemporary()).toString() // Output
			//,"--printNewSourceCode"
			//,"--maintainTemporaries"
		};

		String pathOutput = temporary.getPathTemporary() + "/JSubmeter - Output/" + outputFileName;

		try {
			new Writer(args[1]+args[2]).writeLines(lines);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Library.main(args);

		List<String> listOutput = null;

		try {
			listOutput = new Reader(pathOutput).readLines();
		} catch (Exception e) {
			e.printStackTrace();
		}


		return listOutput;

	}

}

