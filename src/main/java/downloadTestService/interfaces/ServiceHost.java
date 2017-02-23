package downloadTestService.interfaces;

import java.net.MalformedURLException;

/**
 * Created by enzo on 15.11.2014.
 */
public interface ServiceHost {
    public void setResult(String result);

    public void startDownloadQueue() throws MalformedURLException;

    public void cancelDownloadQueue();

    public boolean setDownloadSize(long size);

    public long getDefaultDownloadSize();

    public long getDownloadSize();

    public boolean setDownloadLink(String url);

    public String getDownloadURL();

    public String getDefaultDownloadLink();

    public boolean setDownloadInterval(int interval);

    public int getDownloadInterval();

    public int getDefaultDownloadInterval();
}
