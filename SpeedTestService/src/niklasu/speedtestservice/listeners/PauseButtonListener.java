package niklasu.speedtestservice.listeners;

import niklasu.speedtestservice.DownloadService;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by enzo on 19.02.2015.
 */
public class PauseButtonListener implements ItemListener{
    DownloadService dls;

    public PauseButtonListener(DownloadService d){
        dls=d;
    }
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange()==1) dls.cancelDownloadQueue();
        if (e.getStateChange()==2) dls.startDownloadQueue();
    }
}
