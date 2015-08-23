package downloadTestService.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by enzo on 19.02.2015.
 */
public class OpenButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        final String dir = System.getProperty("user.dir");
        try {
            Runtime.getRuntime().exec("Explorer.exe " + dir);
        } catch (Exception u) {
            u.printStackTrace();
        }
    }
}
