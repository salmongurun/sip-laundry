package siplaundry.util.violations;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.metadata.ConstraintDescriptor;

import java.util.Iterator;

public class MatchViolation<T> implements ConstraintViolation<T> {
    private String fieldName;
    private String aliasName;

    public MatchViolation(String field, String alias) {
        this.fieldName = field;
        this.aliasName = alias;
    }
    @Override
    public String getMessage() {
        return this.aliasName + " harus sesuai";
    }

    @Override
    public String getMessageTemplate() {
        return null;
    }

    @Override
    public T getRootBean() {
        return null;
    }

    @Override
    public Class<T> getRootBeanClass() {
        return null;
    }

    @Override
    public Object getLeafBean() {
        return null;
    }

    @Override
    public Object[] getExecutableParameters() {
        return new Object[0];
    }

    @Override
    public Object getExecutableReturnValue() {
        return null;
    }

    @Override
    public Path getPropertyPath() {
        return new Path() {
            @Override
            public Iterator<Node> iterator() {
                return null;
            }

            @Override
            public String toString() {
                return MatchViolation.this.fieldName;
            }
        };
    }

    @Override
    public Object getInvalidValue() {
        return null;
    }

    @Override
    public ConstraintDescriptor<?> getConstraintDescriptor() {
        return null;
    }

    @Override
    public <U> U unwrap(Class<U> type) {
        return null;
    }
}
