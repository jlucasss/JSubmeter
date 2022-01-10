
package com.jsubmeter.execution;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jsubmeter.models.DataPerson;

public class AbstractExecutionTest {

	AbstractExecution abstractExecution;

	@BeforeEach
	public void starting() {

		DataPerson data = null;
		
		this.abstractExecution = new AbstractExecution(data) {

			protected void prepareCompilation() {}
			protected void prepareExecution() {}
			public void deleteCreatedFiles() {}
			protected void error() {}

			@Override
			protected boolean compile() {// Simulate compilation

				int total = 100000;

				int[] array = new int[total];

				int count = 0;
				for (int i = 0; total > i; i++) {
					count += i;
					array[i] = count;
				}

				return true;
			}

			@Override
			public void run(String[] args) {// Simulate execution

				int total = Integer.parseInt(args[0]);

				int[] array = new int[total];

				int count = 0;
				for (int i = 0; total > i; i++) {
					count += i;
					array[i] = count;
				}

			}
		};

	}

	@Test
	void measuresSimpleExecution() {

		abstractExecution.prepareAndExecute(new String[]{"5"});

		long tc = abstractExecution.getTimeCompilation(),
			te = abstractExecution.getTimeExecution();

		Assertions.assertTrue(tc > 0);
		Assertions.assertTrue(te > 0);

	}

	@Test
	void measuresSimpleExecutionGroup() {

		int numExecutions = 10,
			numData = 2,
			j = 0;

		long[] grupo = new long[numData*numExecutions];

		for (int i = 0; numExecutions > i; i++) { 

			abstractExecution.prepareAndExecute(new String[] {"5"});

			grupo[j] = abstractExecution.getTimeCompilation();
			grupo[j++] = abstractExecution.getTimeExecution();

		}

		boolean containZero = Arrays.binarySearch(grupo, 0) >= 0;

		Assertions.assertFalse( containZero );

	}

}

