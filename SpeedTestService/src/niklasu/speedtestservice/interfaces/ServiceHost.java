package niklasu.speedtestservice.interfaces;

/**
 * Created by enzo on 15.11.2014.
 */
public interface ServiceHost {
    public void setResult(String result);
    public void startDownloadQueue();
    public void cancelDownloadQueue();
}
