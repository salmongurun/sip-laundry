package siplaundry.util;

import java.util.Map;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.scene.Node;
import siplaundry.entity.UserEntity;
import siplaundry.exceptions.ValidationException;

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

    public static <T> void renderErrors(Set<ConstraintViolation<T>> violations, Map<String, Node> fields) {
        for(Map.Entry<String, Node> field: fields.entrySet()) {
            field.getValue().getStyleClass().remove("error");
        }

        for(ConstraintViolation<T> vol: violations) {
            String field = vol.getPropertyPath().toString();

            if(fields.containsKey(field)) {
                Node input = fields.get(field);
                input.getStyleClass().add("error");
            }
        }

        if(violations.size() > 0) throw new ValidationException("Account");
    }
}
