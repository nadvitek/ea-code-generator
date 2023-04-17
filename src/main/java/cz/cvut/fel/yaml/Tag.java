package cz.cvut.fel.yaml;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Tag {

	private final String name;
	private final String DESC_TEMPLATE = "Operations with %s.";

	public static String makeTag(String path) {
		if (path == null) {
			return null;
		}

		for (int i = 1; i < path.length(); i++) {
			if (path.charAt(i) == '/') {
				return path.substring(1, i);
			}
		}
		return path.substring(1);
	}

	public String getDescription() {
		return String.format(DESC_TEMPLATE, name.substring(0, 1).toUpperCase() + name.substring(1));
	}

}
