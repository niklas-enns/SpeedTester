package niklasu.speedtester.ui;

import niklasu.speedtester.config.ConfigStore;

import java.awt.*;

/**
 * Created by enzo on 25.02.2017.
 */
@org.springframework.stereotype.Component
public class UI {
    Menu results;
    ConfigStore configStore;

    public UI(ConfigStore configStore) throws Exception {
        this.configStore = configStore;
        SystemTray tray = SystemTray.getSystemTray();
        TrayIcon trayIcon = new TrayIcon(getImage(), "tray icon");

        // Create a pop-up menu components
        PopupMenu popup = new PopupMenu();
        buildPopupMenu(popup);
        trayIcon.setPopupMenu(popup);
        tray.add(trayIcon);

    }

    private Image getImage() {
        Image image = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/Download_Icon.png"));
        image = image.getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        return image;
    }

    private boolean buildPopupMenu(PopupMenu popup) {
        MenuItem aboutItem = new MenuItem("About");
        CheckboxMenuItem pauseMenu = new CheckboxMenuItem("Pause");
        MenuItem openItem = new MenuItem("Open working directory");
        MenuItem optionsItem = new MenuItem("Options");
        MenuItem exitItem = new MenuItem("Exit");
        //Pseudo-menu:shows last results
        results = new Menu("Last 10 measurements");

        //Add components to pop-up menu
        popup.add(aboutItem);

        popup.addSeparator();
        popup.add(pauseMenu);
        popup.add(optionsItem);
        popup.addSeparator();
        popup.add(openItem);
        popup.add(results);

        popup.addSeparator();
        popup.add(exitItem);

        //Set Listeners
        pauseMenu.addItemListener(e -> {
            {
                if (e.getStateChange() == 1) configStore.setRunning(false);
                if (e.getStateChange() == 2) configStore.setRunning(true);
            }
        });
        optionsItem.addActionListener(e -> new OptionsFrame(configStore));
        openItem.addActionListener(e -> {
            final String dir = System.getProperty("user.dir");
            try {
                Runtime.getRuntime().exec("Explorer.exe " + dir);
            } catch (Exception u) {
                u.printStackTrace();
            }
        });
        exitItem.addActionListener(e -> System.exit(0));

        return true;
    }

    public void setResult(String result) {
        //TODO
        //if (results.getItemCount() == 10) results.remove(0);
        //results.add(new MenuItem(result));
    }
}

