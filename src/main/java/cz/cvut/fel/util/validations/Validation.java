package cz.cvut.fel.util.validations;

import cz.cvut.fel.metamodel.TConnector;
import cz.cvut.fel.metamodel.TObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class Validation<IN, OUT> {

	private static final Logger log = LoggerFactory.getLogger(Validation.class);

	private boolean mute;
	private final Function<IN, OUT> extractor;
	private final BiPredicate<IN, OUT> predicate;
	private final BiFunction<IN, OUT, String> message;

	protected Validation(Function<IN, OUT> extractor,
						 BiPredicate<IN, OUT> predicate,
						 BiFunction<IN, OUT, String> message) {
		this.extractor = extractor;
		this.predicate = predicate;
		this.message = message;
	}

	public static <IN, OUT> ValidationBuilder<IN, OUT> create(
			Function<IN, OUT> extractor,
			Predicate<OUT> predicate,
			BiFunction<IN, OUT, String> message) {
		return new ValidationBuilder<>(extractor, (in, out) -> predicate.test(out), message);
	}

	public static <IN, OUT> ValidationBuilder<IN, OUT> create(
			Function<IN, OUT> extractor,
			BiPredicate<IN, OUT> predicate,
			BiFunction<IN, OUT, String> message) {
		return new ValidationBuilder<>(extractor, predicate, message);
	}

	public OUT valid(IN in) {
		OUT out = extractor.apply(in);
		if (!predicate.test(in, out)) {
			String err = this.message.apply(in, out);
			notFulfilled(err);
		}
		return out;
	}

	protected abstract void notFulfilled(String errMessage);

	public static ValidationBuilder<TObject, String> notNullProperty(String name) {
		return create(o -> o.getPropertyValueByName(name),
				Objects::nonNull, (in, out) -> String.format("Property '%s' is required, actual: %s", name, out));
	}

	public static ValidationBuilder<TObject, String> notNullPropertyIgnoreCase(String name) {
		return create(o -> o.getPropertyValueByNameIgnoreCase(name),
				Objects::nonNull, (in, out) -> String.format("Property '%s' is required, actual: %s", name, out));
	}

	public static ValidationBuilder<TObject, String> notNullObjectName() {
		return create(TObject::getName,
				Objects::nonNull, (in, out) -> String.format("Name of object is required, actual: %s", out));
	}

	public static ValidationBuilder<TConnector, String> notNullConnectorName() {
		return create(TConnector::getName,
				Objects::nonNull, (in, out) -> String.format("Name of connector is required, actual: %s", out));
	}

	public static ValidationBuilder<TObject, String> notNullAttribute(String name) {
		return create(o -> o.getAttributeTypeByName(name),
				Objects::nonNull, (in, out) -> String.format("Attribute '%s' is required, actual: %s", name, out));
	}

	public void mute() {
		mute = true;
	}

	public boolean isMuted() {
		return mute;
	}

	public static class ValidationBuilder<IN, OUT> {

		private final Function<IN, OUT> extractor;
		private final BiPredicate<IN, OUT> predicate;
		private final BiFunction<IN, OUT, String> message;

		private ValidationBuilder(Function<IN, OUT> extractor,
								  BiPredicate<IN, OUT> predicate,
								  BiFunction<IN, OUT, String> message) {
			this.extractor = extractor;
			this.predicate = predicate;
			this.message = message;
		}

		public MinorValidation<IN, OUT> minor() {
			MinorValidation<IN, OUT> validation = new MinorValidation<>(extractor, predicate, message);
			Validations.register(validation);
			return validation;
		}

		public SoftValidation<IN, OUT> soft() {
			SoftValidation<IN, OUT> validation = new SoftValidation<>(extractor, predicate, message);
			Validations.register(validation);
			return validation;
		}

		public HardValidation<IN, OUT> hard() {
			HardValidation<IN, OUT> validation = new HardValidation<>(extractor, predicate, message);
			Validations.register(validation);
			return validation;
		}
	}

	@Override
	public String toString() {
		return message.apply(null, null);
	}

	public abstract String getTypeDescription();
}
