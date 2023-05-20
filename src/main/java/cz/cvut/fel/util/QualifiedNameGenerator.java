package cz.cvut.fel.util;

import cz.cvut.fel.metamodel.TObject;
import cz.cvut.fel.metamodel.TPackage;
import cz.cvut.fel.util.predicate.EqualStringPredicate;
import cz.cvut.fel.util.predicate.PackageNamePredicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This class is for getting full path
 * of an object from EA
 */
public final class QualifiedNameGenerator {

	public static final String PACKAGE_SEPARATOR = "/";

	public static String getFullName(TObject tObject, Set<String> rootPackages, String prefix) {
		Validate.notNull(tObject, "tObject cannot be null");
		return (getFullPackagePath(tObject.getTpackage(), rootPackages, prefix) + PACKAGE_SEPARATOR + tObject.getNameFirstUpper());
	}

	public static String getFullPackagePath(TPackage tPackage, Set<String> rootPackages, String prefix) {
		if (tPackage == null || rootPackages == null) {
			return "";
		}

		StringBuilder sbPath = new StringBuilder();

		if (StringUtils.isNotBlank(prefix)) {
			sbPath.append(prefix);
		}

		boolean skipPackage = hasRootPackageInFullPath(tPackage.getFullPathObjs(), rootPackages);
		for (TPackage pack : tPackage.getFullPathObjs()) {
			if (skipPackage) {
				skipPackage = !CollectionUtils.exists(rootPackages, new EqualStringPredicate(pack.getName(), true));
				continue;
			}
			sbPath.append(pack.getName());
			sbPath.append('/');
		}
		return (StringUtils.substringBeforeLast(sbPath.toString().toLowerCase(), "/"));
	}

	private static boolean hasRootPackageInFullPath(List<TPackage> fullPackagePath, Set<String> rootPackages) {
		for (String rootPackage : rootPackages) {
			if (CollectionUtils.exists(fullPackagePath, new PackageNamePredicate(rootPackage, true))) {
				return true;
			}
		}
		return false;
	}
}
