package downloadTestService.interfaces;

/**
 * Created by enzo on 15.11.2014.
 */
public interface ServiceHost {
    public void setResult(String result);
    public void startDownloadQueue();
    public void cancelDownloadQueue();
    public boolean setDownloadSize(int size);
    public int getDefaultDownloadSize();
    public int getDownloadSize();
    public boolean setDownloadLink(String url);
    public String getDwonloadLink();
    public String getDefaultDownloadLink();

    public boolean setDownloadInterval(int interval);

    public int getDownloadInterval();

    public int getDefaultDownloadInterval();
}
