package niklasu.speedtester.interfaces;

/**
 * Created by enzo on 19.02.2015.
 */
public interface ParamChanger {
    String getDownloadLink();

    boolean resetDownloadLink();

    int getDownloadSize();

    boolean resetDownloadSize();

    int getDownloadInterval();

    boolean resetDownloadInterval();
}
