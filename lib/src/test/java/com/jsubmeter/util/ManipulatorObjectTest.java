package com.jsubmeter.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class ManipulatorObjectTest {

	@Test
	void testConvertObjectToList() {
		
		List<String> list = new ArrayList<>();
		list.add("Hello");
		list.add("World!");
		
		assertEquals(list, ManipulatorObject.convertObjectToList(list));
		
	}

}
