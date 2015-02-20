package downloadTestService.frames.impl;

import downloadTestService.DownloadFileSizeChecker;
import downloadTestService.DownloadService;
import downloadTestService.frames.OptionsFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
        try {
            boolean newFileIsBigEnough = (new DownloadFileSizeChecker().targetIsBiggerThan(new URL(host.getDownloadLink()),1));
            if (!newFileIsBigEnough){
                if (continueAnyway()==0){
                    dls.setDownloadLink(host.getDownloadLink());
                    dls.setDownloadSize(host.getDownloadSize());
                    dls.startDownloadQueue();
                    host.dispose();
                }
            }
        }catch (Exception b){
            System.out.println("Bad URL");
            informAboutBadURL();
        }

    }
    private int continueAnyway(){
        return JOptionPane.showConfirmDialog(
                host,
                "The file is smaller than 1 MB. Proceed anyway?",
                "Small target file",
                JOptionPane.YES_NO_OPTION);

    }

    private void informAboutBadURL(){
        JOptionPane.showMessageDialog(host,
                "Check your URL. It has to start with\n"+
                "http://www....",
                "Bad URL",
                JOptionPane.ERROR_MESSAGE);
    }
}
