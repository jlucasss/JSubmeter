package com.jsubmeter.util;

/**
 * 
 * This class serves to store useful string handling methods.
 * 
 * @author José Lucas dos Santos da Silva
 *
 */

public class ManipulatorString {

	public static String swap(String str) {// "\" to  "/"
		return str.replaceAll("\\\\", "/");
	}
	
/*	public static String[] splitPathFile(String path) {
		
		File file = new File(path);
		
		String[] out = new String[2];

		if (file.isFile()) {// The file must exist to work
			
			out[0] = file.getName();
			out[1] = path.replaceAll(out[0], "");

			return out;
			
		}
		
		throw new RuntimeException("The path is a directory: " + path);

	}*/

}
