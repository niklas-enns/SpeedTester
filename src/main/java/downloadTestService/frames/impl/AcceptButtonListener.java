package downloadTestService.frames.impl;

import downloadTestService.DownloadService;
import downloadTestService.ParamValidator;
import downloadTestService.exceptions.BadFileException;
import downloadTestService.exceptions.TooSmallFileException;
import downloadTestService.frames.OptionsFrame;
import downloadTestService.interfaces.Constants;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;

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
            if (ParamValidator.validateParams(host.getDownloadSize(), host.getDownloadInterval(), host.getDownloadLink()))
                return;
        } catch (TooSmallFileException e1) {
            e1.printStackTrace();
            informAboutTargetFileIsSmallerThanRequired(e1.getRealSize(), e1.getRequiredSize());
            return;
        } catch (MalformedURLException e1) {
            informAboutBadURL();
            e1.printStackTrace();
            return;
        } catch (BadFileException e1) {
            e1.printStackTrace();
            informAboutBadTargetFile();
            return;
        }
        dls.setDownloadLink(host.getDownloadLink());
        dls.setDownloadInterval(host.getDownloadInterval());
        dls.setDownloadSize(host.getDownloadSize());
        dls.startDownloadQueue();
        host.dispose();
    }

    private void informAboutTargetFileIsSmallerThanRequired(long realFileSize, long requiredDownloadsize) {
        JOptionPane.showMessageDialog(host,
                "The target file, you selected has a size of " + realFileSize / MB + "MB,\n which is less than your required download size of " + requiredDownloadsize / MB + "MB.\nLower the download size or choose a bigger target file",
                "Target file is too small",
                JOptionPane.ERROR_MESSAGE);
    }


    private void informAboutBadTargetFile() {
        JOptionPane.showMessageDialog(host,
                "The HTTP response did not include an Content-Length field. Please pick another file",
                "Target file is mysterious",
                JOptionPane.ERROR_MESSAGE);
    }


    private void informAboutBadURL() {
        JOptionPane.showMessageDialog(host,
                "Check your URL. It has to start with\n" +
                        "http://www....",
                "Bad URL",
                JOptionPane.ERROR_MESSAGE);
    }
}
