package siplaundry.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        System.out.println("Failed validation: " + message);
    }
}
