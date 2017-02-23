package downloadTestService.interfaces;

/**
 * Created by enzo on 19.02.2015.
 */
public interface ParamChanger {
    public String getDownloadLink();

    public boolean resetDownloadLink();

    public int getDownloadSize();

    public boolean resetDownloadSize();

    public int getDownloadInterval();

    public boolean resetDownloadInterval();
}
