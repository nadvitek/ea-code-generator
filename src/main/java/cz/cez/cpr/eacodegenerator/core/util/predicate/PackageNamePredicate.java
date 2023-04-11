package cz.cez.cpr.eacodegenerator.core.util.predicate;

import cz.cez.cpr.eacodegenerator.core.metamodel.TPackage;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.Validate;

public class PackageNamePredicate implements Predicate {

	private final String expectedPackageName;

	private final boolean ignoreCase;

	public PackageNamePredicate(String expectedPackageName, boolean ignoreCase) {
		Validate.notEmpty(expectedPackageName);

		this.expectedPackageName = expectedPackageName;
		this.ignoreCase = ignoreCase;
	}

	public boolean evaluate(Object obj) {
		if (obj instanceof TPackage) {
			TPackage pack = (TPackage) obj;
			if (ignoreCase) {
				return expectedPackageName.equalsIgnoreCase(pack.getName());
			} else {
				return expectedPackageName.equals(pack.getName());
			}
		}

		throw new IllegalArgumentException("Expected instance of TPackage.class, got "
				+ (obj != null ? obj.getClass() : "null"));
	}

}
