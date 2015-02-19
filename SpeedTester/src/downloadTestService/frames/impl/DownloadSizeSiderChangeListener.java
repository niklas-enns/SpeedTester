package downloadTestService.frames.impl;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Created by enzo on 19.02.2015.
 */
public class DownloadSizeSiderChangeListener implements ChangeListener {
    JSlider slider;

    public DownloadSizeSiderChangeListener(JSlider s){
        slider=s;
    }
    @Override
    public void stateChanged(ChangeEvent e) {
        if (!slider.getValueIsAdjusting()){
            }
            if (slider.getValue()>175){
                slider.setValue(200);
                return;
            }
            if (slider.getValue()>125){
                slider.setValue(150);
                return;
            }
            if (slider.getValue()>75){
                slider.setValue(100);
                return;
            }
            if (slider.getValue()>25){
                slider.setValue(50);
                return;
            }
        if (slider.getValue() <= 25) {
            slider.setValue(1);
            return;
        }
    }
}
