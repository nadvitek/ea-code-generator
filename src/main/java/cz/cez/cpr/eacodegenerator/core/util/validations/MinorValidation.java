package cz.cez.cpr.eacodegenerator.core.util.validations;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class MinorValidation<IN, OUT> extends Validation<IN, OUT> {

	protected MinorValidation(Function<IN, OUT> extractor,
							  BiPredicate<IN, OUT> predicate,
							  BiFunction<IN, OUT, String> message) {
		super(extractor, predicate, message);
	}

	protected void notFulfilled(String errMessage) {
		// do nothing
	}

	@Override
	public String getTypeDescription() {
		return "nothing will be printed";
	}
}
