
package com.jsubmeter.execution.lang.java;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import com.jsubmeter.models.DataPerson;
import com.jsubmeter.util.ManipulatorObject;

public class JExecution extends JAbstractIOExecution {

	private JavaCompiler compiler;

	public JExecution(DataPerson data) {

		super(data);

	}

	@Override
	protected boolean compile() {

		this.compiler = ToolProvider.getSystemJavaCompiler();

		OutputStream output = new OutputStream() {
			private StringBuilder sb = new StringBuilder();

			@Override
			public void write(int x) {
				this.sb.append((char) x);
			}

			@Override
			public String toString() {
				return this.sb.toString();
			}
		};

		int result = this.compiler.run(null, output, output, data.getPathOutputFolder() + this.className);

		return result == 0 ? true : false;

	}
	@Override
	protected void run(String[] args) {
		
		if (args == null)
			args = new String[] { null };
		
		baseRun(args);
		
	}

	@SuppressWarnings("unchecked")
	private void baseRun(String[] args) {
		
		try {

			List<String> out = (ArrayList<String>) ManipulatorObject.convertObjectToList(
					method.invoke(this.newInstance, new Object[] { args } )
			);
			
			this.setListOutput(out);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	@Override
	protected void error() {

		System.out.println("Something went wrong in the compilation!");

	}

}

