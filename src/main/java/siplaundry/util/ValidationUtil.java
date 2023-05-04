package siplaundry.util;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class ValidationUtil {
    private static ValidatorFactory factory;

    static {
        factory = Validation.buildDefaultValidatorFactory();
    }

    public static Validator getValidator() {
        return factory.getValidator();
    }

    public static <T> Set<ConstraintViolation<T>> validate(T validatee) {
        return getValidator().validate(validatee);
    }

    public static <T> String getErrorsAsString(Set<ConstraintViolation<T>> violations, String joiner) {
        StringBuilder builder = new StringBuilder();

        for(ConstraintViolation<T> violation: violations) {
            builder.insert(0, violation.getMessage() + joiner);
        }

        return builder.toString();
    }
}
