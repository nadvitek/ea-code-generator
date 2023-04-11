package test;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, defaultType = Test.class)
public class Test {
	private int test = 10;
}
