package com.jsubmeter.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ManipulatorStringTest {

	@Test
	void testSwap() {

		assertEquals("/test/path/local/", ManipulatorString.swap("\\test\\path\\local\\"));
	
	}
	
	
/*	@Test
	void testSplitPathFile() {
		
		String path = AppTest.class.getResource("").toString().replaceAll("file:/", "");

		String[] outCorrect = new String[] {"AppTest.class", path};
		
		path += "AppTest.class";
		
		String[] outCurrent = ManipulatorString.splitPathFile(path);
		
		assertEquals(outCorrect[0], outCurrent[0]);
		assertEquals(outCorrect[1], outCurrent[1]);
		
	}*/

}
