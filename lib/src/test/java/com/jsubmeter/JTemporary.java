
package com.jsubmeter;

import java.util.List;

import com.jsubmeter.io.writer.Writer;
import com.jsubmeter.io.reader.Reader;

import java.util.Arrays;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JTemporary {

	private String pathTemporary;
	private File folders;

	public JTemporary() {

		String pathTemporaryOS = System.getProperty("java.io.tmpdir").replaceAll("\\\\", "/");
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

	public String createClassExampleFile(EnumAction action, String className) {

		try {

			// Find files text with class examples

			Path resourceDirectory = Paths.get("src","test","resources");
			String absolutePath = resourceDirectory.toFile().getAbsolutePath();

			String pathClass = absolutePath + "\\com\\jsubmeter\\examples\\" + action.toString() + "\\" + className + ".txt";

			// Copy and creat class

			List<String> lines = new Reader(pathClass).readLines();

			String pathOutput = getPathTemporary() + className + ".java";

			createExampleFile(pathOutput, lines);

			return pathOutput;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public void createExampleFile(String path, List<String> content) throws IOException {
		
		Writer.createFile(path);

		Writer writer = new Writer(path);

		writer.writeLines(content);

	}

	public static enum EnumAction {
		READER("readers"),
		WRITER("writers");

		protected String action;

		EnumAction(String action) {
			this.action = action;
		}

		public String toString() {
			return this.action;
		}

	}

}

