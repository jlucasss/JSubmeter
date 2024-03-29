package com.jsubmeter.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ManipulatorObject {
	
	public static List<?> convertObjectToList(Object obj) {

		List<?> list = new ArrayList<>();

		if (obj.getClass().isArray())
			list = Arrays.asList((Object[])obj);
		else if (obj instanceof Collection)
			list = new ArrayList<>((Collection<?>)obj);
		
		return list;

	}
	
}
