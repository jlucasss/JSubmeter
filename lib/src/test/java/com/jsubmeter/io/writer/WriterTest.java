package com.jsubmeter.io.writer;

import java.io.File;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.BeforeAll;//BeforeAll is giving an error, that's why it is being used BeforeEach
//import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import com.jsubmeter.JTemporary;
import com.jsubmeter.io.reader.Reader;

class WriterTest {

	public Writer writer;
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
			
			Assertions.assertThrows(NullPointerException.class, () -> new Writer(null));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Test
	void inputEmpty() {

		try {
			
			Assertions.assertThrows(IllegalArgumentException.class, () -> new Writer(""));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Test
	void createFile() {

		try {
			
			Writer.createFile(pathDefault+"file-created.txt");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Assertions.assertTrue(new File(pathDefault+"file-created.txt").exists());
		
	}

	@Test
	void createDirectory() {

		try {
			
			Writer.createDiretory(pathDefault+"lists/folder/");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Assertions.assertTrue(new File(pathDefault+"lists/folder/").exists());
		
	}	

	@Test
	void writeInExistingFile() {

		try {
			
			writer = new Writer(pathDefault+"ExistingFile.txt");
			
			writer.writeLines(Arrays.asList("Text"));
			
			Assertions.assertEquals(
						new Reader(pathDefault+"ExistingFile.txt").readLines(), Arrays.asList("Text"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Test
	void writeInNonexistentFile() {

		try {
			
			writer = new Writer(pathDefault+"File2.txt");
			
			writer.writeLines(Arrays.asList("Text"));
			
			Assertions.assertEquals(
						new Reader(pathDefault+"File2.txt").readLines(), Arrays.asList("Text"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}	

}
