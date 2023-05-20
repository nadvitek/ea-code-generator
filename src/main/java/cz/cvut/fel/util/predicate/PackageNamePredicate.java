package cz.cvut.fel.util.predicate;

import cz.cvut.fel.metamodel.TPackage;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.Validate;

/**
 * This class compares names of packages
 */
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
