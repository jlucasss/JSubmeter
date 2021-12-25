
package com.jsubmeter.io.reader;

/**
 * 
 * This class serves to facilitate reading.
 * 
 * @author José Lucas dos Santos da Silva
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Reader {

	protected BufferedReader br;
	private File file;

	public Reader(String path) throws IOException {

		if (path == null)
			throw new NullPointerException("The path cannot be null.");
		if (path == "")
			throw new IllegalArgumentException("Path String is empty!");
		
		file = new File(path);

		if (!file.exists())
			throw new FileNotFoundException("The file \""+ path + "\" not found.");
			
		if (!file.canRead())
			file.setReadable(true);

		br = new BufferedReader(new FileReader(file));

	}

	public List<String> readLines() {

		List<String> lines = new ArrayList<>();

		String lineCurrent = null;

		try {

			while (br.ready()) {

				lineCurrent = br.readLine();

				if (lineCurrent != null)
					lines.add(lineCurrent);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return lines;

	}

	public int numberFoldersAndFiles() {
		return file.list().length;
	}

	public static int numberFoldersAndFiles(String path) {
		return new File(path).list().length;
	}

}

