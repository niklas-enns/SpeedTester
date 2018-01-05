package niklasu.speedtester.exceptions;

/**
 * Created by enzo on 24.08.2015.
 */
public class ValidationException extends Exception {

    public ValidationException(String s) {
        super(s);
    }

    public ValidationException(String message, Exception e){
        super(message, e);
    }
}
