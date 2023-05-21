package siplaundry.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import animatefx.animation.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
        Set<String> validatedFields = new HashSet<>();

        for(Map.Entry<String, Node> field: fields.entrySet()) {
            field.getValue().getStyleClass().remove("error");

            if(field.getValue().getParent() instanceof VBox) {
                removeErrorMessage((VBox) field.getValue().getParent());
            }
        }

        for(ConstraintViolation<T> vol: violations) {
            String field = vol.getPropertyPath().toString();

            if(validatedFields.contains(field)) continue;

            if(fields.containsKey(field)) {
                Node input = fields.get(field);
                VBox parent = (VBox) input.getParent();
                Text text = new Text(vol.getMessage());
                text.getStyleClass().add("error-message");

                input.getStyleClass().add("error");
                (new Pulse(input)).play();
                parent.getChildren().add(text);

                validatedFields.add(field);
            }
        }

        if(violations.size() > 0) throw new ValidationException("Account");
    }

    private static void removeErrorMessage(VBox parent) {
        ObservableList<Node> childs = parent.getChildren();
        int lastIndex = childs.size() - 1;

        if(childs.get(lastIndex) instanceof Text) {
            parent.getChildren().remove(lastIndex);
        }
    }
}
