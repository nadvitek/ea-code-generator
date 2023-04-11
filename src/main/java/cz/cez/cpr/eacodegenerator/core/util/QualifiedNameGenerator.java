package cz.cez.cpr.eacodegenerator.core.util;

import cz.cez.cpr.eacodegenerator.core.metamodel.TObject;
import cz.cez.cpr.eacodegenerator.core.metamodel.TPackage;
import cz.cez.cpr.eacodegenerator.core.util.predicate.EqualStringPredicate;
import cz.cez.cpr.eacodegenerator.core.util.predicate.PackageNamePredicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class QualifiedNameGenerator {

	public static final String PACKAGE_SEPARATOR = "/";
	private final List<TPackage> packagePath;

	public QualifiedNameGenerator(List<TPackage> fullPackagePath, TObject clazz, Set<String> rootPackages) {
		Validate.notEmpty(fullPackagePath, "fullPackagePath cannot be null or empty");
		Validate.notNull(clazz, "clazz cannot be null");
		Validate.notNull(rootPackages, "rootPackages cannot be null");

		this.packagePath = new ArrayList<TPackage>();

		boolean skipPackage = hasRootPackageInFullPath(fullPackagePath, rootPackages);
		for (TPackage pack : fullPackagePath) {
			if (skipPackage) {
				skipPackage = !CollectionUtils.exists(rootPackages, new EqualStringPredicate(pack.getName(), true));
				continue;
			}
			this.packagePath.add(pack);
		}
		Validate.notEmpty(this.packagePath, "packagePath cannot be null or empty");
	}

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
