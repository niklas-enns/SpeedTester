package downloadTestService.frames;

import downloadTestService.Constants;
import downloadTestService.DownloadService;
import downloadTestService.frames.impl.*;
import downloadTestService.interfaces.OptionsChanger;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by enzo on 19.02.2015.
 */
public class OptionsFrame extends Frame implements OptionsChanger, Constants {
    static final int SIZE_MIN = 0;
    static final int SIZE_MAX = 200;
    private static int DOWNLOAD_INTERVAL_INIT;
    long DOWNLOAD_SIZE_INIT;
    DownloadService dls;
    JSlider downloadSizeSlider;
    JSlider intervalSlider;
    TextField linkField;

    public OptionsFrame(DownloadService d) {
        dls = d;
        dls.cancelDownloadQueue();
        DOWNLOAD_SIZE_INIT = dls.getDownloadSize();

        DOWNLOAD_INTERVAL_INIT = dls.getDownloadInterval();
        setTitle("SpeedTester options");
        setSize(400, 300);

        setLayout(new GridLayout(4, 1));

        //filesize slider
        JPanel p = new JPanel();
        TitledBorder border = new TitledBorder(new EtchedBorder(), "Download size [MB]");
        p.setBorder(border);
        downloadSizeSlider = new JSlider(JSlider.HORIZONTAL,
                SIZE_MIN, SIZE_MAX, (int) DOWNLOAD_SIZE_INIT / MB);


        Font font = new Font("Serif", Font.ITALIC, 15);
        downloadSizeSlider.setFont(font);

        //Turn on labels at major tick marks.
        downloadSizeSlider.setMajorTickSpacing(100);
        downloadSizeSlider.setMinorTickSpacing(50);
        downloadSizeSlider.setPaintTicks(true);
        downloadSizeSlider.setPaintLabels(true);
        //downloadSizeSlider.setSnapToTicks(true);
        downloadSizeSlider.addChangeListener(new DownloadSizeSiderChangeListener(downloadSizeSlider));
        p.add(downloadSizeSlider);
        add(p);

        //intervalslider
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

        JPanel p2 = new JPanel();
        p2.setBorder(new TitledBorder(new EtchedBorder(), "Download file"));
        linkField = new TextField();
        linkField.setColumns(50);
        linkField.setText(dls.getDwonloadLink());
        p2.add(linkField);
        add(p2);

        Panel buttons = new Panel();
        buttons.setLayout(new GridLayout(1, 2));

        Button reset = new Button("Reset");
        reset.addActionListener(new ResetButtonListener(this));
        Button decline = new Button("Decline");
        decline.addActionListener(new DeclineButtonListener(dls, this));
        Button accept = new Button("Accept");
        accept.addActionListener(new AcceptButtonListener(dls, this));

        buttons.add(reset);
        buttons.add(decline);
        buttons.add(accept);
        add(buttons);
        addWindowListener(new OptionsWindowListener());

        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public String getDownloadLink() {
        return linkField.getText();
    }

    @Override
    public boolean resetDownloadLink() {
        linkField.setText(dls.getDefaultDownloadLink());
        return true;
    }

    @Override
    public int getDownloadSize() {
        return downloadSizeSlider.getValue() * MB;
    }

    @Override
    public boolean resetDownloadSize() {
        downloadSizeSlider.setValue((int) dls.getDefaultDownloadSize() / MB);
        return true;
    }

    @Override
    public int getDownloadInterval() {
        return intervalSlider.getValue() * MB;
    }

    @Override
    public boolean resetDownloadInterval() {
        intervalSlider.setValue(dls.getDefaultDownloadInterval());
        return true;
    }


    class OptionsWindowListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            dls.startDownloadQueue();
            e.getWindow().dispose();
        }
    }
}
