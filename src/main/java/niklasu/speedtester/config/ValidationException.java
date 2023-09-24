package niklasu.speedtester.config;

class ValidationException extends Exception {
    ValidationException(String s) {
        super(s);
    }
    ValidationException(String message, Exception e) {
        super(message, e);
    }
}
