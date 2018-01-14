package niklasu.speedtester.ui;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import niklasu.speedtester.events.ResultEvent;
import niklasu.speedtester.events.StartEvent;
import niklasu.speedtester.events.StopEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.ItemEvent;

public class TrayContextMenu {
    private static final Logger logger = LoggerFactory.getLogger(TrayContextMenu.class);
    private Menu results;
    private EventBus eventBus;
    private OptionsFrame optionsFrame;

    @Inject
    public TrayContextMenu(EventBus eventBus, OptionsFrame optionsFrame) throws AWTException {
        this.eventBus = eventBus;
        this.optionsFrame = optionsFrame;
        init();
    }

    private void init() throws AWTException {
        eventBus.register(this);
        SystemTray tray = SystemTray.getSystemTray();
        TrayIcon trayIcon = new TrayIcon(getTrayIcon(), "SpeedTester");

        // Create a pop-up menu components
        PopupMenu popup = new PopupMenu();
        buildPopupMenu(popup);
        trayIcon.setPopupMenu(popup);
        tray.add(trayIcon);
    }

    private Image getTrayIcon() {
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
        results = new Menu("10 last measurements");

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
                if (e.getStateChange() == ItemEvent.SELECTED) eventBus.post(new StopEvent());
                if (e.getStateChange() == ItemEvent.DESELECTED) eventBus.post(new StartEvent());
        });
        optionsItem.addActionListener(e -> {
            eventBus.post(new StopEvent());
            optionsFrame.appear();
        });
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

