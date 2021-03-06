package fi.gekkio.drumfish.lang;

import java.io.Serializable;

import com.google.common.base.Function;

/**
 * Functions for Option objects.
 * <p>
 * All objects returned by the methods are serializable if all the method parameters are too.
 */
public final class OptionFunctions {

    private OptionFunctions() {
    }

    private static final class GetValueFunction<T> implements Function<Option<? extends T>, T>, Serializable {
        private static final long serialVersionUID = 5753367640741973339L;

        @SuppressWarnings("rawtypes")
        public static final GetValueFunction INSTANCE = new GetValueFunction();

        @Override
        public T apply(Option<? extends T> input) {
            return input.getValue();
        }
    }

    /**
     * Returns a function that calls getValue() on input values.
     * 
     * @return function
     */
    @SuppressWarnings("unchecked")
    public static <T> Function<Option<? extends T>, T> getValue() {
        return GetValueFunction.INSTANCE;
    }

    private static final class OptionFunction<T> implements Function<T, Option<T>>, Serializable {
        private static final long serialVersionUID = -7512216163114972210L;

        @SuppressWarnings("rawtypes")
        public static final OptionFunction INSTANCE = new OptionFunction();

        @Override
        public Option<T> apply(T input) {
            return Option.option(input);
        }
    }

    /**
     * Returns a function that calls Option.option() on input values effectively wrapping them into Option objects.
     * 
     * @return function
     */
    @SuppressWarnings("unchecked")
    public static <T> Function<T, Option<T>> option() {
        return OptionFunction.INSTANCE;
    }

}
