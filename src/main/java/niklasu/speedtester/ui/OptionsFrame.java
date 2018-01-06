package niklasu.speedtester.ui;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import niklasu.speedtester.config.ConfigStore;
import niklasu.speedtester.events.StartEvent;
import niklasu.speedtester.exceptions.ValidationException;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class OptionsFrame extends Frame {
    private static final int SIZE_MIN = 0;
    private static final int SIZE_MAX = 200;
    private static final Font font = new Font("Serif", Font.ITALIC, 15);
    private static int DOWNLOAD_INTERVAL_INIT;
    private EventBus eventBus;
    private ConfigStore configStore;
    private long DOWNLOAD_SIZE_INIT;
    private JSlider downloadSizeSlider;
    private TextField intervalField;
    private TextField linkField;

    @Inject
    public OptionsFrame(EventBus eventBus, ConfigStore configStore) throws HeadlessException {
        this.eventBus = eventBus;
        this.configStore = configStore;
        init();
    }

    private void init() {
        DOWNLOAD_SIZE_INIT = configStore.getSize();
        DOWNLOAD_INTERVAL_INIT = configStore.getInterval();
        setTitle("Options");
        setSize(400, 300);
        setLayout(new GridLayout(4, 1));
        setResizable(false);

        createAndAddSizeSlider();
        createAndAddIntervalField();
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
            intervalField.setText(""+configStore.getInterval());
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

    private void createAndAddIntervalField() {
        JPanel p3 = new JPanel();
        p3.setBorder(new TitledBorder(new EtchedBorder(), "Download interval [Minute]"));
        intervalField = new TextField(Integer.toString(configStore.getInterval()), 4);
        p3.add(intervalField);
        add(p3);
    }

    private void createAndAddSizeSlider() {
        JPanel sizeSlider = new JPanel();
        TitledBorder border = new TitledBorder(new EtchedBorder(), "Download size [MB]");
        sizeSlider.setBorder(border);
        downloadSizeSlider = new JSlider(JSlider.HORIZONTAL,
                SIZE_MIN, SIZE_MAX, (int) DOWNLOAD_SIZE_INIT);
        downloadSizeSlider.setFont(font);

        //Turn on labels at major tick marks.
        downloadSizeSlider.setMajorTickSpacing(100);
        downloadSizeSlider.setMinorTickSpacing(50);
        downloadSizeSlider.setPaintTicks(true);
        downloadSizeSlider.setPaintLabels(true);
        downloadSizeSlider.addChangeListener(new DownloadSizeSiderChangeListener(downloadSizeSlider));
        sizeSlider.add(downloadSizeSlider);
        add(sizeSlider);
    }

    public void appear() {
        setVisible(true);
    }

    private String getDownloadLink() {
        return linkField.getText();
    }

    private int getDownloadSize() {
        return downloadSizeSlider.getValue();
    }

    private int getDownloadInterval() throws ValidationException {
        try {
            return Integer.parseInt(intervalField.getText());
        }catch (NumberFormatException e){
            throw new ValidationException(String.format("%s is not a valid number", intervalField.getText()));
        }
    }

    class OptionsWindowListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            eventBus.post(new StartEvent());
            e.getWindow().dispose();
        }
    }

    class AcceptButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                configStore.setParams(getDownloadSize(), getDownloadInterval(), getDownloadLink());
            } catch (ValidationException e1) {
                informAboutTargetFileIsSmallerThanRequired(e1.getMessage());
                return;
            }
            eventBus.post(new StartEvent());
            dispose();
        }

        private void informAboutTargetFileIsSmallerThanRequired(String message) {
            JOptionPane.showMessageDialog(null,
                    message,
                    "Configuration Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}
