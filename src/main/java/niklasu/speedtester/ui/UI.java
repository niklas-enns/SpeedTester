package niklasu.speedtester.ui;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import niklasu.speedtester.config.ConfigStore;
import niklasu.speedtester.events.ResultEvent;
import niklasu.speedtester.events.StartEvent;
import niklasu.speedtester.events.StopEvent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.util.logging.Logger;

@org.springframework.stereotype.Component
public class UI {
    private static final Logger log = Logger.getLogger(UI.class.getName());
    Menu results;
    @Autowired
    ConfigStore configStore;
    @Autowired
    EventBus eventBus;

    @PostConstruct
    public void init() throws Exception {
        eventBus.register(this);
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
                if (e.getStateChange() == 1) eventBus.post(new StopEvent());
                if (e.getStateChange() == 2) eventBus.post(new StartEvent());
            }
        });
        //optionsItem.addActionListener(e -> new OptionsFrame());
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

    @Subscribe
    public void setResult(ResultEvent resultEvent) {
        if (results.getItemCount() == 10) results.remove(0);
        results.add(new MenuItem(String.format("%.2f MBit/s", resultEvent.getSpeed())));
    }
}

