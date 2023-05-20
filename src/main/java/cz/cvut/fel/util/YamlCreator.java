package cz.cvut.fel.util;

import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is for generating
 * Strings with tabs from Yaml objects
 * that goes into the Swagger file
 */
public class YamlCreator {

	public static final String TAB = "  ";
	private final StringBuilder sb = new StringBuilder();
	private int tabs = 0;

	public YamlCreator ln(int i, YamlConsumer consumer) {
		int reset = tabs;
		tabs += i;
		consumer.accept(this);
		tabs = reset;
		return this;
	}

	public YamlCreator ln(int t, Object... o) {
		if (!isFulfilled(o) || hasNullValues(o)) {
			return this;
		}
		YamlConsumer consumer = getConsumer(o);
		if (consumer != null) {
			return ln(t, consumer);
		}

		String text = Stream.of(o).map(String::valueOf).collect(Collectors.joining());
		Scanner scanner = new Scanner(text);
		while (scanner.hasNextLine()) {
			if (sb.length() > 0) {
				sb.append(br());
			}
			sb.append(tabs(t + tabs)).append(scanner.nextLine());
		}
		return this;
	}

	private boolean hasNullValues(Object... objects) {
		return Stream.of(objects)
				.filter(v -> v instanceof Value)
				.map(v -> (Value) v)
				.anyMatch(Value::isNotNull);
	}

	public boolean isFulfilled(Object... objects) {
		return Stream.of(objects)
				.filter(c -> c instanceof Condition)
				.map(c -> (Condition) c)
				.allMatch(Condition::isFulfilled);
	}

	public YamlConsumer getConsumer(Object... objects) {
		return Stream.of(objects)
				.filter(c -> c instanceof YamlConsumer)
				.map(c -> (YamlConsumer) c)
				.findAny()
				.orElse(null);
	}

	public static String br() {
		return "\n";
	}

	private String tabs(int times) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < times; i++) {
			sb.append(TAB);
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return sb.toString();
	}

	public static class Value {

		protected final Object value;

		public Value(Object value) {
			this.value = value;
		}

		public boolean isNotNull() {
			return value == null;
		}

		@Override
		public String toString() {
			return value + "";
		}
	}

	public static class Text extends Value {

		public Text(Object value) {
			super(value);
		}

		@Override
		public String toString() {
			return value == null ? null : "\"" +
					value.toString()
							.replaceAll("\r", " ")
							.replaceAll("\n", " ")
							.replaceAll("\"", "\\\\\"")
							.replaceAll(" +", " ")
					+ "\"";
		}
	}

	public static class Condition {

		private final boolean result;

		public Condition(boolean result) {
			this.result = result;
		}

		public boolean isFulfilled() {
			return result;
		}

		@Override
		public String toString() {
			return "";
		}
	}

	@FunctionalInterface
	public static interface YamlConsumer {
		void accept(YamlCreator y);
	}
}
