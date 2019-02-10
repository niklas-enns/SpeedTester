package niklasu.speedtester.config;

public interface ParamValidator {
    void validate(Config config) throws ValidationException;
}
