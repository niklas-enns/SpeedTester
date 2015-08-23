package downloadTestService.frames.impl;

import downloadTestService.Constants;
import downloadTestService.DownloadFileSizeChecker;
import downloadTestService.DownloadService;
import downloadTestService.frames.OptionsFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by enzo on 19.02.2015.
 */
public class AcceptButtonListener implements ActionListener, Constants {
    DownloadService dls;
    OptionsFrame host;

    public AcceptButtonListener(DownloadService d, OptionsFrame f) {
        dls = d;
        host = f;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            long realFileSize = new DownloadFileSizeChecker().getFileSize(new URL(host.getDownloadLink()));
            if (realFileSize < MB) {
                if (continueAnyway() != 0) {
                    return;
                }
            }
            if (realFileSize / MB < host.getDownloadSize()) {
                informAboutTargetFileIsSmallerThanRequired(realFileSize);
                return;
            }

        } catch (MalformedURLException e1) {
            System.out.println("Bad URL");
            informAboutBadURL();
            return;
        } catch (Exception e1) {
            informAboutBadTargetFile();
            return;
        }
        dls.setDownloadLink(host.getDownloadLink());
        dls.setDownloadInterval(host.getDownloadInterval());
        dls.setDownloadSize(host.getDownloadSize());
        dls.startDownloadQueue();
        host.dispose();
    }

    private void informAboutTargetFileIsSmallerThanRequired(long realFileSize) {
        JOptionPane.showMessageDialog(host,
                "The target file, you selected has a size of " + realFileSize / MB + "MB,\n which is less than your required download size of " + host.getDownloadSize() + "MB.\nLower the download size or choose a bigger target file",
                "Target file is too small",
                JOptionPane.ERROR_MESSAGE);
    }

    private void informAboutBadTargetFile() {
        JOptionPane.showMessageDialog(host,
                "The HTTP response did not include an Content-Length field. Please pick another file",
                "Target file is mysterious",
                JOptionPane.ERROR_MESSAGE);
    }

    private int continueAnyway() {
        return JOptionPane.showConfirmDialog(
                host,
                "The file is smaller than 1 MB. Proceed anyway?",
                "Small target file",
                JOptionPane.YES_NO_OPTION);

    }

    private void informAboutBadURL() {
        JOptionPane.showMessageDialog(host,
                "Check your URL. It has to start with\n" +
                        "http://www....",
                "Bad URL",
                JOptionPane.ERROR_MESSAGE);
    }
}
