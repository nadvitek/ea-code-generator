package cz.cvut.fel.util.validations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class SoftValidation<IN, OUT> extends Validation<IN, OUT> {

	private static final Logger log = LoggerFactory.getLogger(SoftValidation.class);

	protected SoftValidation(Function<IN, OUT> extractor,
							 BiPredicate<IN, OUT> predicate,
							 BiFunction<IN, OUT, String> message) {
		super(extractor, predicate, message);
	}

	protected void notFulfilled(String errMessage) {
		log.warn(errMessage);
	}

	@Override
	public String getTypeDescription() {
		return "warning will be printed";
	}
}
