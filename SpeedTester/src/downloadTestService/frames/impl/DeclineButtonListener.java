package downloadTestService.frames.impl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by enzo on 19.02.2015.
 */
public class DeclineButtonListener implements ActionListener{
    JSlider slider;
    public DeclineButtonListener(JSlider s){
        slider=s;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        slider.setValue(10);
    }
}
