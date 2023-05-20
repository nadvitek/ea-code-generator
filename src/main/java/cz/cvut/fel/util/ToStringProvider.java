package cz.cvut.fel.util;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * This class provides String builder
 * for building toString methods of EA
 * entities
 */
public final class ToStringProvider {

	public static ToStringBuilder getBuilder(Object obj) {
		return new ToStringBuilder(obj, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
