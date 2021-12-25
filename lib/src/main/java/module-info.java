module com.jsubmeter {

	requires java.compiler;

	opens com.jsubmeter.models;
	opens com.jsubmeter.execution;

	exports com.jsubmeter.models;
	exports com.jsubmeter.execution;

}
