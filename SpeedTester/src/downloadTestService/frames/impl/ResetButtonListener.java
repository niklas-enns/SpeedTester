package downloadTestService.frames.impl;

import downloadTestService.frames.OptionsFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by enzo on 19.02.2015.
 */
public class ResetButtonListener implements ActionListener{
    OptionsFrame host;
    public ResetButtonListener(OptionsFrame h){
        host = h;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        host.resetDownloadLink();
        host.resetDownloadSize();
        host.resetDownloadInterval();
    }
}
