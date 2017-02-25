package niklasu.speedtester;

import niklasu.speedtester.config.ConfigReader;
import niklasu.speedtester.config.ConfigStore;
import niklasu.speedtester.downloader.DownloadFileSizeChecker;
import niklasu.speedtester.downloader.DownloadMgr;
import niklasu.speedtester.interfaces.Constants;
import niklasu.speedtester.ui.UI;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DownloadService implements Constants {
    private static final Logger log = Logger.getLogger(DownloadFileSizeChecker.class.getName());


    ConfigStore configStore;

    public DownloadService(String[] args) throws Exception {
        log.setLevel(Level.INFO);
        configStore = new ConfigStore();
        new ConfigReader(args, configStore);

        if (SystemTray.isSupported() && configStore.isTray()) {
            new UI(configStore);
        } else {
            log.info("SystemTray not available");
        }

        new DownloadMgr(configStore);
        registerForEvents();
    }

    private void registerForEvents() {
        //TODO receive results for commandline
    }

    private String convertResultToString(Date dateOfDownload, double resultSpeed){
        SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm");
        String time = sdftime.format(dateOfDownload);
        return (time + " - " + String.format("%.2f", resultSpeed) + " Mbit/s");
    }



    public static void main(String[] args) throws Exception {
        new DownloadService(args);
    }
}
