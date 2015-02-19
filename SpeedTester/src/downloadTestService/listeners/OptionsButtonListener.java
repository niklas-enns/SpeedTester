package downloadTestService.listeners;

import downloadTestService.DownloadService;
import downloadTestService.frames.OptionsFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by enzo on 19.02.2015.
 */
public class OptionsButtonListener implements ActionListener {
    DownloadService dls;
    public OptionsButtonListener(DownloadService d){
        dls=d;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        new OptionsFrame(dls);
    }
}
