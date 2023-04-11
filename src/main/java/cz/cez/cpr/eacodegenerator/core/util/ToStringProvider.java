package cz.cez.cpr.eacodegenerator.core.util;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public final class ToStringProvider {

	public static ToStringBuilder getBuilder(Object obj) {
		return new ToStringBuilder(obj, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
