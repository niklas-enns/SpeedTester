package niklasu.speedtester.config;

public class ValidationException extends Exception {
    ValidationException(String s) {
        super(s);
    }
    ValidationException(String message, Exception e) {
        super(message, e);
    }
}
