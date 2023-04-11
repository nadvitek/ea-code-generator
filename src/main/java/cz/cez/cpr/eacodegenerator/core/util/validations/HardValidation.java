package cz.cez.cpr.eacodegenerator.core.util.validations;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class HardValidation<IN, OUT> extends Validation<IN, OUT> {


	protected HardValidation(Function<IN, OUT> extractor,
							 BiPredicate<IN, OUT> predicate,
							 BiFunction<IN, OUT, String> message) {
		super(extractor, predicate, message);
	}

	protected void notFulfilled(String errMessage) {
		if (!isMuted()) {
			throw new IllegalStateException(errMessage);
		}
	}

	@Override
	public String getTypeDescription() {
		return "exception will be thrown";
	}
}
