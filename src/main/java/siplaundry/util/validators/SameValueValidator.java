package siplaundry.util.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import siplaundry.entity.PasswordEntity;

public class SameValueValidator implements ConstraintValidator<SameValue, PasswordEntity> {
    @Override
    public boolean isValid(PasswordEntity entity, ConstraintValidatorContext context) {
        if(entity == null) {
            return true;
        }

        String prop1 = entity.getPassword();
        String prop2 = entity.getConfirm();
        boolean isValid = prop1 != null && prop1.equals(prop2);

        if(!isValid) {
            context.buildConstraintViolationWithTemplate("Konfirmasi password harus valid")
                    .addPropertyNode("confirm")
                    .addConstraintViolation();
        }

        return isValid;
    }
}
