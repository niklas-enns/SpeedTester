package downloadTestService.frames.impl;

import downloadTestService.DownloadService;
import downloadTestService.frames.OptionsFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by enzo on 19.02.2015.
 */
public class DeclineButtonListener implements ActionListener {
    DownloadService dls;
    OptionsFrame host;

    public DeclineButtonListener(DownloadService d, OptionsFrame f) {
        dls = d;
        host = f;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dls.startDownloadQueue();
        host.dispose();
    }
}
