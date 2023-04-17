package cz.cvut.fel.util.predicate;

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.Validate;

public class EqualStringPredicate implements Predicate {

	private final String expectedString;

	private final boolean ignoreCase;

	public EqualStringPredicate(String expectedString, boolean ignoreCase) {
		Validate.notEmpty(expectedString);

		this.expectedString = expectedString;
		this.ignoreCase = ignoreCase;
	}

	public boolean evaluate(Object obj) {
		if (obj instanceof String) {
			String str = (String) obj;
			if (ignoreCase) {
				return expectedString.equalsIgnoreCase(str);
			} else {
				return expectedString.equals(str);
			}
		}

		throw new IllegalArgumentException("Expected instance of String.class, got "
				+ (obj != null ? obj.getClass() : "null"));
	}

}
