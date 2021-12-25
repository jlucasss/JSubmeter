
package com.jsubmeter;

import java.util.List;

import com.jsubmeter.io.writer.Writer;

import java.util.Arrays;

import java.io.File;
import java.io.IOException;

public class JTemporary {

	private String pathTemporary;
	private File folders;

	public JTemporary() {

		String pathTemporaryOS = System.getProperty("java.io.tmpdir");
		this.pathTemporary = pathTemporaryOS + "/JSUBMETER-TEMPORARY/";
		folders = new File(this.pathTemporary);

	}

	public String getPathTemporary() {
		return this.pathTemporary;
	}

	public void createTemporary() {

		folders.mkdirs();

	}

	public void deleteTemporary() {

		folders.delete();

	}

	public String createExampleFile() throws IOException {
		
		String path = getPathTemporary() + "Example.java";

		createExampleFile(path,
			Arrays.asList(
				"import java.util.Scanner;",
				"public class Example {",
				"	public static void main(String[] args) {",
				"		Scanner in = new Scanner(System.in);",
				"		String received = in.nextLine();",
				"		System.out.println(received);",
				"	}",
				"}"
			)
		);

		return path;

	}

	public void createExampleFile(String path, List<String> content) throws IOException {
		
		Writer.createFile(path);

		Writer writer = new Writer(path);

		writer.writeLines(content);

	}

}

