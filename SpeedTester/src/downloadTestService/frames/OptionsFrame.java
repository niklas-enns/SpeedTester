package downloadTestService.frames;

import downloadTestService.DownloadService;
import downloadTestService.frames.impl.AcceptButtonListener;
import downloadTestService.frames.impl.DeclineButtonListener;
import downloadTestService.frames.impl.DownloadSizeSiderChangeListener;
import downloadTestService.frames.impl.ResetButtonListener;
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
public class OptionsFrame extends Frame implements OptionsChanger{
    DownloadService dls;
    JSlider downloadSizeSlider;
    TextField linkField;
    static final int SIZE_MIN = 0;
    static final int SIZE_MAX = 200;
    int SIZE_INIT;

    public OptionsFrame(DownloadService d){
        dls=d;
        dls.cancelDownloadQueue();
        SIZE_INIT=dls.getDownloadSize();
        setTitle("SpeedTester options");
        setSize(400,300);

        setLayout(new GridLayout(3,1));

        JPanel p = new JPanel();
        p.setBorder(new TitledBorder(new EtchedBorder(), "Download size [MB]"));

        downloadSizeSlider = new JSlider(JSlider.HORIZONTAL,
                SIZE_MIN, SIZE_MAX, SIZE_INIT);


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

        JPanel p2 = new JPanel();
        p2.setBorder(new TitledBorder(new EtchedBorder(), "Download file"));
        linkField = new TextField();
        linkField.setColumns(50);
        linkField.setText(dls.getDwonloadLink());
        p2.add(linkField);
        add(p2);

        Panel buttons = new Panel();
        buttons.setLayout(new GridLayout(1,2));

        Button reset = new Button("Reset");
        reset.addActionListener(new ResetButtonListener(this));
        Button decline = new Button("Decline");
        decline.addActionListener(new DeclineButtonListener(dls,this));
        Button accept = new Button("Accept");
        accept.addActionListener(new AcceptButtonListener(dls,this));

        buttons.add(reset);
        buttons.add(decline);
        buttons.add(accept);
        add(buttons);
        addWindowListener(new OptionsWindowListener());

        setResizable(false);
        setVisible(true);
    }

    public String getDownloadLink(){
        return linkField.getText();
    }

    @Override
    public boolean resetDownloadLink() {
        linkField.setText(dls.getDefaultDownloadLink());
        return true;
    }

    @Override
    public int getDownloadSize() {
        return downloadSizeSlider.getValue();
    }

    @Override
    public boolean resetDownloadSize() {
        downloadSizeSlider.setValue(dls.getDefaultDownloadSize());
        return true;
    }

    class OptionsWindowListener extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
            dls.startDownloadQueue();
            e.getWindow().dispose();
        }
    }
}
