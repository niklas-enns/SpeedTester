package niklasu.speedtester.exceptions;

/**
 * Created by enzo on 24.08.2015.
 */
public class TooSmallFileException extends Exception {
    private long realSize;
    private long requiredSize;

    public TooSmallFileException(String s, long realSize, long requiredSize) {
        super(s);
        this.realSize = realSize;
        this.requiredSize = requiredSize;
    }

    public long getRealSize() {
        return realSize;
    }

    public long getRequiredSize() {
        return requiredSize;
    }
}
