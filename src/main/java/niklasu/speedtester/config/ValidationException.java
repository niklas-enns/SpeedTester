package niklasu.speedtester.config;

public class ValidationException extends Exception {

    public ValidationException(String s) {
        super(s);
    }

    public ValidationException(String message, Exception e){
        super(message, e);
    }
}
