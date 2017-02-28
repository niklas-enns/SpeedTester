package niklasu.speedtester.ui;

import com.google.common.eventbus.EventBus;
import niklasu.speedtester.config.ConfigStore;
import niklasu.speedtester.config.ParamValidator;
import niklasu.speedtester.events.ConfigChangedEvent;
import niklasu.speedtester.events.StartEvent;
import niklasu.speedtester.events.StopEvent;
import niklasu.speedtester.exceptions.BadFileException;
import niklasu.speedtester.exceptions.TooSmallFileException;
import niklasu.speedtester.interfaces.Constants;
import niklasu.speedtester.interfaces.ParamChanger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;

/**
 * Created by enzo on 19.02.2015.
 */
@org.springframework.stereotype.Component
public class OptionsFrame extends Frame implements ParamChanger, Constants {
    static final int SIZE_MIN = 0;
    static final int SIZE_MAX = 200;
    private static final Font font = new Font("Serif", Font.ITALIC, 15);
    private static int DOWNLOAD_INTERVAL_INIT;
    @Autowired
    EventBus eventBus;
    @Autowired
    ConfigStore configStore;
    @Autowired
    ParamValidator paramValidator;
    long DOWNLOAD_SIZE_INIT;
    JSlider downloadSizeSlider;
    JSlider intervalSlider;
    TextField linkField;

    @PostConstruct
    public void init() {
        DOWNLOAD_SIZE_INIT = configStore.getSize();
        DOWNLOAD_INTERVAL_INIT = configStore.getInterval();
        setTitle("Options");
        setSize(400, 300);
        setLayout(new GridLayout(4, 1));
        setResizable(false);

        createAndAddSizeSlider();
        createAndAddIntervalSlider();
        createAndAddURLTextField();
        createAndAddButtons();

        addWindowListener(new OptionsWindowListener());

        setLocationRelativeTo(null);
    }

    private void createAndAddButtons() {
        Panel buttons = new Panel();
        buttons.setLayout(new GridLayout(1, 2));

        Button reset = new Button("Reset");
        reset.addActionListener(e -> {
            configStore.reset();
            linkField.setText(configStore.getUrl());
            downloadSizeSlider.setValue(configStore.getSize());
            intervalSlider.setValue(configStore.getInterval());
            eventBus.post(new ConfigChangedEvent());
        });

        Button decline = new Button("Decline");
        decline.addActionListener(e -> eventBus.post(new StartEvent()));

        Button accept = new Button("Accept");
        accept.addActionListener(new AcceptButtonListener());

        buttons.add(reset);
        buttons.add(decline);
        buttons.add(accept);
        add(buttons);
    }

    private void createAndAddURLTextField() {
        JPanel p2 = new JPanel();
        p2.setBorder(new TitledBorder(new EtchedBorder(), "Download file"));
        linkField = new TextField();
        linkField.setColumns(50);
        linkField.setText(configStore.getUrl());
        p2.add(linkField);
        add(p2);
    }

    private void createAndAddIntervalSlider() {
        JPanel p3 = new JPanel();
        p3.setBorder(new TitledBorder(new EtchedBorder(), "Download interval [Minute]"));
        intervalSlider = new JSlider(JSlider.HORIZONTAL,
                SIZE_MIN, SIZE_MAX, DOWNLOAD_INTERVAL_INIT);

        intervalSlider.setFont(font);

        //Turn on labels at major tick marks.
        intervalSlider.setMajorTickSpacing(100);
        intervalSlider.setMinorTickSpacing(50);
        intervalSlider.setPaintTicks(true);
        intervalSlider.setPaintLabels(true);
        //downloadSizeSlider.setSnapToTicks(true);
        intervalSlider.addChangeListener(new DownloadIntervalSliderChangeListener(intervalSlider));
        p3.add(intervalSlider);
        add(p3);
    }

    private void createAndAddSizeSlider() {
        JPanel sizeSlider = new JPanel();
        TitledBorder border = new TitledBorder(new EtchedBorder(), "Download size [MB]");
        sizeSlider.setBorder(border);
        downloadSizeSlider = new JSlider(JSlider.HORIZONTAL,
                SIZE_MIN, SIZE_MAX, (int) DOWNLOAD_SIZE_INIT / MB);
        downloadSizeSlider.setFont(font);

        //Turn on labels at major tick marks.
        downloadSizeSlider.setMajorTickSpacing(100);
        downloadSizeSlider.setMinorTickSpacing(50);
        downloadSizeSlider.setPaintTicks(true);
        downloadSizeSlider.setPaintLabels(true);
        //downloadSizeSlider.setSnapToTicks(true);
        downloadSizeSlider.addChangeListener(new DownloadSizeSiderChangeListener(downloadSizeSlider));
        sizeSlider.add(downloadSizeSlider);
        add(sizeSlider);
    }

    public void appear() {
        eventBus.post(new StopEvent());
        setVisible(true);
    }

    public String getDownloadLink() {
        return linkField.getText();
    }

    public boolean resetDownloadLink() {

        return true;
    }

    public int getDownloadSize() {
        return downloadSizeSlider.getValue();
    }


    public boolean resetDownloadSize() {

        return true;
    }


    public int getDownloadInterval() {
        return intervalSlider.getValue() * MB;
    }


    public boolean resetDownloadInterval() {

        return true;
    }

    class OptionsWindowListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            eventBus.post(new StartEvent());
            e.getWindow().dispose();
        }
    }

    class AcceptButtonListener implements ActionListener, Constants {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                paramValidator.validateParams(getDownloadSize(), getDownloadInterval(), getDownloadLink());
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
            configStore.setUrl(getDownloadLink());
            configStore.setInterval(getDownloadInterval());
            configStore.setSize(getDownloadSize());
            eventBus.post(new ConfigChangedEvent());
            eventBus.post(new StartEvent());
            dispose();
        }

        private void informAboutTargetFileIsSmallerThanRequired(long realFileSize, long requiredDownloadsize) {
            JOptionPane.showMessageDialog(null,
                    "The target file, you selected has a size of " + realFileSize / MB + "MB,\n which is less than your required download size of " + requiredDownloadsize / MB + "MB.\nLower the download size or choose a bigger target file",
                    "Target file is too small",
                    JOptionPane.ERROR_MESSAGE);
        }


        private void informAboutBadTargetFile() {
            JOptionPane.showMessageDialog(null,
                    "The HTTP response did not include an Content-Length field. Please pick another file",
                    "Target file is mysterious",
                    JOptionPane.ERROR_MESSAGE);
        }


        private void informAboutBadURL() {
            JOptionPane.showMessageDialog(null,
                    "Check your URL. It has to start with\n" +
                            "http://www....",
                    "Bad URL",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}
