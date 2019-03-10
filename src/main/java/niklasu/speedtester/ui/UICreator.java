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
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

public class UICreator {
    private static final Logger logger = LoggerFactory.getLogger(UICreator.class);
    private final Menu results = new Menu("10 last measurements");
    private final EventBus eventBus;
    private final OptionsFrame optionsFrame;

    @Inject
    public UICreator(EventBus eventBus, OptionsFrame optionsFrame) throws AWTException {
        this.eventBus = eventBus;
        this.optionsFrame = optionsFrame;
        create();
    }

    private void create() throws AWTException {
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

    private void buildPopupMenu(PopupMenu popup) {
        MenuItem aboutItem = new MenuItem("About");
        popup.add(aboutItem);
        popup.addSeparator();

        popup.add(getPauseMenuItem());

        popup.add(getMenuItem("Options", e -> handleOptionsItemClick()));
        popup.addSeparator();

        popup.add(getMenuItem("Open working directory", e -> handleExitButtonClick()));
        popup.add(results);
        popup.addSeparator();

        MenuItem exitItem = getMenuItem("Exit", e -> System.exit(0));
        popup.add(exitItem);
    }

    private void handleOptionsItemClick() {
        eventBus.post(new StopEvent());
        optionsFrame.appear();
    }

    private void handleExitButtonClick() {
        final String dir = System.getProperty("user.dir");
        try {
            Runtime.getRuntime().exec("Explorer.exe " + dir);
        } catch (Exception u) {
            u.printStackTrace();
        }
    }

    private MenuItem getMenuItem(String options, ActionListener actionListener) {
        MenuItem optionsItem = new MenuItem(options);
        optionsItem.addActionListener(actionListener);
        return optionsItem;
    }

    private CheckboxMenuItem getPauseMenuItem() {
        CheckboxMenuItem pauseMenu = new CheckboxMenuItem("Pause");
        pauseMenu.addItemListener(e -> {
            switch (e.getStateChange()) {
                case ItemEvent.SELECTED:
                    eventBus.post(new StopEvent());
                    break;
                case ItemEvent.DESELECTED:
                    eventBus.post(new StartEvent());
                    break;
            }
        });
        return pauseMenu;
    }

    @Subscribe
    public void setResult(ResultEvent resultEvent) {
        if (results.getItemCount() == 10) results.remove(0);
        results.add(new MenuItem(resultEvent.getSpeedText()));
    }
}

