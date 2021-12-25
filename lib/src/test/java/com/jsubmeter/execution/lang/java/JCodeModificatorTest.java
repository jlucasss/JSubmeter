package com.jsubmeter.execution.lang.java;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class JCodeModificatorTest {

	// Method addImports
	
	@Test
	void addImportsCorrect() {
		
		String current = JCodeModificator.addImports();
		
		assertTrue(current.contains("FileInputStream"));
		assertTrue(current.contains("IOException"));
		assertTrue(current.contains("List"));
		assertTrue(current.contains("ArrayList"));
		
	}
	
	// Method createNewMethodToControl
	
	@Test
	void createNewMethodToControlCorrect() {
		
		String current = JCodeModificator.createNewMethodToControl();
		
		assertTrue(current.contains("public static List<String> exitToVerificationIOExec = new ArrayList<>();"));
		assertTrue(current.contains("main2"));
		assertTrue(current.contains("makeExecution(String[] args)"));
		assertTrue(current.contains("try {"));
		assertTrue(current.contains("} catch (Exception e) {"));
		assertTrue(current.contains("e.printStackTrace();"));
		
	}
	
	// Method replaceInputToReader
	
	@Test
	void replaceInputToReaderCorrect() {
		
		String pathFolder = "C://",
				pathInputFile = "input.txt";
		
		String code = "Scanner sc = new Scanner(System.in);";
		
		String current = JCodeModificator
				.replaceInputToReader(pathInputFile, pathFolder, code);
		
		assertFalse(current.contains("System.in"));
		assertTrue(current.contains("FileInputStream fileInputStreamFI = null;"));
		assertTrue(current.contains("try{"));
		assertTrue(current.contains("fileInputStreamFI = new FileInputStream(\"" + pathFolder + "\" + (args[0] == null ? \"" + pathInputFile + "\" : args[0]) );"));
		assertTrue(current.contains("} catch(Exception e) {"));
		assertTrue(current.contains("e.printStackTrace();"));
		
	}
	
	// Method replaceAllCasesOfPrint
	
	@Test
	void replaceAllCasesOfPrintCorrect() {
		
		String code = "System.out.print(name);";//Demo code: System.out.printf("Name = &s", name);

		String expected = "exitToVerificationIOExec.add(\"\" + name);";

		assertEquals(expected, JCodeModificator.replaceAllCasesOfPrint(code));
		
	}

	@Test
	void replaceAllCasesOfPrintlnCorrect() {
		
		String code = "System.out.println(name);";//Demo code: System.out.printf("Name = &s", name);

		String expected = "exitToVerificationIOExec.add(\"\" + name);";

		assertEquals(expected, JCodeModificator.replaceAllCasesOfPrint(code));
		
	}	

	@Test
	void replaceAllCasesOfPrintfCorrect() {
		
		String code = "System.out.printf(\"Name = &s\", name);";//Demo code: System.out.printf("Name = &s", name);

		String expected = "exitToVerificationIOExec.add(\"\" + \"Name = \" + name + \"\");";
		
		assertEquals(expected, JCodeModificator.replaceAllCasesOfPrint(code));

	}

	
	// Method createEmptyConstructor
	
	@Test
	void createEmptyConstructorCorrect() {
		
		String className = "Example",
				expectedContains = "public Example() {}";
		
		String createEmptyConstructor = JCodeModificator
				.createEmptyConstructor("", className);
		
		assertTrue(createEmptyConstructor.contains(expectedContains));
		
	}
	
	// Method removePrintfFormat
	
	@Test
	void removePrintfFormatCorrect() {
		
		String code = "System.out.printf(\"Name = &s\", name);";//Demo code: System.out.printf("Name = &s", name);

		String expected = "\"Name = \" + name + \"\"";
		
		code = code.trim().substring("System.out.printf(".length());

		assertEquals(expected, JCodeModificator.removePrintfFormat(code));

	}

	@Test
	void removePrintfFormatSeveralVariablesCorrect() {
		
		String code = "System.out.printf(\"Name = &s, &d\", name, 5);";//Demo code: System.out.printf("Name = &s, &d", name, 5);

		String expected = "\"Name = \" + name + \", \" + 5 + \"\"";
		
		code = code.trim().substring("System.out.printf(".length());

		assertEquals(expected, JCodeModificator.removePrintfFormat(code));

	}
	
	// Method findLastMainQuote
	
	@Test
	void findLastMainQuoteCorrect() {
		
		String code = "\',;\""; // Demo code: ',;"
		
		int expected = 3;

		assertEquals(expected, 
				JCodeModificator.findLastMainQuote(code.toCharArray(), "\"".toCharArray()[0], expected));
		
	}
	
	// Method removeComents
	
	@Test
	void removeComentsInOneLineCorrect() {
		
		List<String> code = new ArrayList<>();
		code.add("String name; //name = \"JSubmeter\";");//Demo code: "String name; //name = "JSubmeter";"
	
		String expected = "String name; "; // assertEquals no ignore spaces
		
		// call method
		JCodeModificator.removeComents(code, 0);
		
		assertEquals(expected, code.get(0));
		
	}
	
	@Test
	void removeComentsInsideOneLineCorrect() {
		
		List<String> code = new ArrayList<>();
		code.add("String name; /* name = \"JSubmeter\"; */");//Demo code: "String name; /* name = "JSubmeter"; */"
	
		String expected = "String name; "; // assertEquals no ignore spaces
		
		// call method
		JCodeModificator.removeComents(code, 0);
		
		assertEquals(expected, code.get(0));
		
	}
	
	@Test
	void removeComentsSeveralLinesCorrect() {
		
		List<String> code = new ArrayList<>();
		code.add("String name; /* name = \"JSubmeter\"; "); // Demo code: "String name; /* name = "JSubmeter"; */"
		code.add(" * This is a comment");
		code.add(" int y = 2x ");
		code.add(" 	* 3; */ public... ");
		
		List<String> expected = new ArrayList<>(); // assertEquals no ignore spaces
		expected.add("String name; ");
		expected.add("");
		expected.add("");
		expected.add(" public... ");
		
		// call method
		JCodeModificator.removeComents(code, 0);
		
		assertEquals(expected, code);
		
	}
	
	// Method checkIfIsIntoAspas
	
	@Test
	void checkIfIsIntoAspasCorrect() {
		
		String code = " \"//public Hello()\" "; // Demo code: "//public Hello()"
		String part = "//";
		
		assertTrue(JCodeModificator.checkIfIsIntoAspas(code, part));
		
	}
	
}
