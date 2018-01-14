package niklasu.speedtester.ui;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import niklasu.speedtester.config.ConfigStore;
import niklasu.speedtester.config.ValidationException;
import niklasu.speedtester.events.StartEvent;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class OptionsFrame extends Frame {
    private EventBus eventBus;
    private ConfigStore configStore;
    private TextField downloadSize;
    private TextField intervalField;
    private TextField linkField;

    @Inject
    public OptionsFrame(EventBus eventBus, ConfigStore configStore) throws HeadlessException {
        this.eventBus = eventBus;
        this.configStore = configStore;
        init();
    }

    private void init() {
        setTitle("Options");
        setSize(400, 200);
        setLayout(new GridLayout(3, 1));
        setResizable(false);

        Panel sliders = new Panel();
        sliders.setLayout(new GridLayout(1, 2));


        JPanel downloadSizePanel = assembleDownloadSizePanel();
        sliders.add(downloadSizePanel);

        JPanel intervalPanel = assembleDownloadIntervalPanel();
        sliders.add(intervalPanel);

        add(sliders);

        createAndAddURLTextField();
        createAndAddButtons();

        addWindowListener(new OptionsWindowListener());

        setLocationRelativeTo(null);
    }

    private JPanel assembleDownloadIntervalPanel() {
        JPanel intervalPanel = new JPanel();
        intervalPanel.setBorder(new TitledBorder(new EtchedBorder(), "Download interval [Minute]"));
        intervalField = new TextField(Integer.toString(configStore.getInterval()), 4);
        intervalPanel.add(intervalField);
        return intervalPanel;
    }

    private JPanel assembleDownloadSizePanel() {
        JPanel downloadSizePanel = new JPanel();
        downloadSizePanel.setBorder(new TitledBorder(new EtchedBorder(), "Download size [MB]"));
        downloadSize = new TextField(Integer.toString(configStore.getSize()), 4);
        downloadSizePanel.add(downloadSize);
        return downloadSizePanel;
    }

    private void createAndAddButtons() {
        Panel buttons = new Panel();
        buttons.setLayout(new GridLayout(1, 2));

        Button reset = new Button("Reset");
        reset.addActionListener(e -> {
            configStore.reset();
            linkField.setText(configStore.getUrl());
            downloadSize.setText("" + configStore.getSize());
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


    public void appear() {
        setVisible(true);
    }

    private String getDownloadLink() {
        return linkField.getText();
    }

    private int getDownloadSize() throws ValidationException {
        try {
            return Integer.parseInt(downloadSize.getText());
        } catch (NumberFormatException e) {
            throw new ValidationException(String.format("%s is not a valid number", intervalField.getText()));
        }
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
