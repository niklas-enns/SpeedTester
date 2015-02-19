package downloadTestService.frames.impl;

import downloadTestService.DownloadService;
import downloadTestService.frames.OptionsFrame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by enzo on 19.02.2015.
 */
public class AcceptButtonListener implements ActionListener {
    DownloadService dls;
    OptionsFrame host;
    public AcceptButtonListener(DownloadService d, OptionsFrame f){
        dls=d;
        host = f;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        dls.setDownloadLink(host.getDownloadLink());
        dls.setDownloadSize(host.getDownloadSize());
        dls.startDownloadQueue();
        host.dispose();
    }
}
