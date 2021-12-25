
/**
* 
* CODE EXTRACTED AND MODIFIED FROM https://ideone.com/cu1GhE#view_edit_box
* 
*/

package com.jsubmeter.execution.lang.java;
import javax.tools.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import java.security.SecureClassLoader;

public class CompilerBase {

	public static class CharSequenceJavaFileObject extends SimpleJavaFileObject {
		
		/**
         * CharSequence representing the source code to be compiled
         */
		
		private CharSequence content;
 
		/**
         * This constructor will store the source code in the
         * internal "content" variable and register it as a
         * source code, using a URI containing the class full name
         *
         * @param className name of the public class in the source code
         * @param content source code to compile
         */
		
		public CharSequenceJavaFileObject(String className, CharSequence content) {
			super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
			this.content = content;
		}
 
		@Override
		public CharSequence getCharContent(boolean ignoreEncodingErrors) {
			return content;
		}
	}
 
	@SuppressWarnings("rawtypes")
	public static class ClassFileManager extends ForwardingJavaFileManager {
		private JavaClassObject javaClassObject;
 
		@SuppressWarnings({ "unchecked" })
		public ClassFileManager(StandardJavaFileManager standardManager) {
			super(standardManager);
		}

		/**
         * Will be used by us to get the class loader for our
         * compiled class. It creates an anonymous class
         * extending the SecureClassLoader which uses the
         * byte code created by the compiler and stored in
         * the JavaClassObject, and returns the Class for it
         */
		
		@Override
		public ClassLoader getClassLoader(Location location) {
			return new SecureClassLoader() {
				@Override
				protected Class<?> findClass(String name) throws ClassNotFoundException {
					byte[] b = javaClassObject.getBytes();
					return super.defineClass(name, javaClassObject.getBytes(), 0, b.length);
				}
			};
		}
 
		@Override
		public JavaFileObject getJavaFileForOutput(Location location, String className, 
						JavaFileObject.Kind kind, FileObject sibling) throws IOException {
			this.javaClassObject = new JavaClassObject(className, kind);
			return this.javaClassObject;
		}
	}

	public static class JavaClassObject extends SimpleJavaFileObject {

		/**
         * Byte code created by the compiler will be stored in this
         * ByteArrayOutputStream so that we can later get the
         * byte array out of it
         * and put it in the memory as an instance of our class.
         */
		
		protected final ByteArrayOutputStream bos =
			new ByteArrayOutputStream();
 

        /**
         * Registers the compiled class object under URI
         * containing the class full name
         *
         * @param name
         *            Full name of the compiled class
         * @param kind
         *            Kind of the data. It will be CLASS in our case
         */
		
		public JavaClassObject(String name, Kind kind) {
			super(URI.create("string:///" + name.replace('.', '/')
				+ kind.extension), kind);
		}

		/**
         * Will be used by our file manager to get the byte code that
         * can be put into memory to instantiate our class
         *
         * @return compiled byte code
         */
		
		public byte[] getBytes() {
			return bos.toByteArray();
		}

		/**
         * Will provide the compiler with an output stream that leads
         * to our byte array. This way the compiler will write everything
         * into the byte array that we will instantiate later
         */
		
		@Override
		public OutputStream openOutputStream() throws IOException {
			return bos;
		}
	}

}

