package cz.cez.cpr.eacodegenerator.core.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class YamlCreatorTest {

	private static final Logger log = LoggerFactory.getLogger(YamlCreatorTest.class);

	public static final String JSON = "[{\"key\":\"TYP\",   \r   \"value\":\"HISTORICAL\"},\n{\"key\":\"KOMODITA\", \"value\":\"E\"}]";
	public static final String WRAPPED_JSON = "\"[{\\\"key\\\":\\\"TYP\\\", \\\"value\\\":\\\"HISTORICAL\\\"}, " +
			"{\\\"key\\\":\\\"KOMODITA\\\", \\\"value\\\":\\\"E\\\"}]\"";

	@Test
	public void testText() {
		YamlCreator.Text text = new YamlCreator.Text(JSON);
		log.info(text.toString());
		assertEquals(WRAPPED_JSON, text.toString());
	}
}
