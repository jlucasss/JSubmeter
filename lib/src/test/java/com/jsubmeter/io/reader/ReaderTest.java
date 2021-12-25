package com.jsubmeter.io.reader;

import java.io.FileNotFoundException;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeAll; //BeforeAll da erro, por enquanto esta sendo usado BeforeEach
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jsubmeter.JTemporary;
import com.jsubmeter.io.writer.Writer;

class ReaderTest {

	public Reader reader;
	private String pathDefault;
	
	@BeforeEach
	void starting() {
		
		JTemporary temp = new JTemporary();
		temp.createTemporary();

		this.pathDefault = temp.getPathTemporary();
		
	}

	@Test
	void inputNull() {

		try {
			
			Assertions.assertThrows(NullPointerException.class, () -> new Reader(null));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Test
	void inputEmpty() {

		try {
			
			Assertions.assertThrows(IllegalArgumentException.class, () -> new Reader(""));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Test
	void fileNonexistent() {

		try {
			
			Assertions.assertThrows(FileNotFoundException.class, 
					() -> new Reader(this.pathDefault + "/FileNonexistent.txt"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	void fileWithText() {

		try {

			new Writer(this.pathDefault + "File.txt")
				.writeLines(Arrays.asList("Text"));

			reader = new Reader(this.pathDefault + "/File.txt");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Assertions.assertEquals(Arrays.asList("Text"), reader.readLines());
		
	}

	@Test
	void fileEmpty() {

		try {
	
			Writer.createFile(this.pathDefault + "FileEmpty.txt");
		
			reader = new Reader(this.pathDefault + "/FileEmpty.txt");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Assertions.assertEquals(Arrays.asList(), reader.readLines());
		
	}	
	
}
