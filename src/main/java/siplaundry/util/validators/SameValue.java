package siplaundry.util.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SameValueValidator.class)
public @interface SameValue {
    String propertyPath() default "sameValue";
    String message() default "Nilai pada dua properti harus sama";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
