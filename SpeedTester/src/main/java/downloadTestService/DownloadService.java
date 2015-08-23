package downloadTestService;

import com.beust.jcommander.JCommander;
import downloadTestService.interfaces.ServiceHost;
import downloadTestService.listeners.ExitButtonListener;
import downloadTestService.listeners.OpenButtonListener;
import downloadTestService.listeners.OptionsButtonListener;
import downloadTestService.listeners.PauseButtonListener;

import java.awt.*;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;

import static java.util.concurrent.TimeUnit.MINUTES;

public class DownloadService implements ServiceHost {
    private static final Logger log = Logger.getLogger(DownloadFileSizeChecker.class.getName());
    static DownloadService dls = new DownloadService();
    static ScheduledFuture<?> beeperHandle;
    static ScheduledExecutorService scheduler;
    static Menu results;
    static DownloadServiceArguments arguments;

    static int dlsize;
    static int downloadInterval;
    static String url;
    static Boolean enableTrayIcon;

    final Runnable downloadStarter = new Runnable() {
        public void run() {
            log.info("Starting Download of " + dlsize + " MB");
            try {
                DownloadThread dl = new DownloadThread(dlsize, dls, new URL(url));
                dl.start();
            } catch (Exception e) {
                log.severe("Bad URL");
            }
        }
    };

    //TODO Wait4DL
    //Download should not start when there is already one running

    public static void main(String[] args) {
        arguments = new DownloadServiceArguments();
        new JCommander(arguments, args);

        dlsize = arguments.getSize();
        if (!(dlsize <= 200 && dlsize >= 1))
            throw new java.lang.IllegalArgumentException("File size has to be between 1 and 200 MB");
        downloadInterval = arguments.getInterval();
        if (!(downloadInterval >= 1))
            throw new java.lang.IllegalArgumentException("Download interval has to be greator or equal 1");
        url = arguments.getUrl();
        enableTrayIcon = arguments.getTray();

        scheduler = Executors.newScheduledThreadPool(1);
        dls.startDownloadQueue();

        if (SystemTray.isSupported() && enableTrayIcon) {
            initSysTray();
        } else {
            log.info("TrayIcon could not be added.");
        }
    }

    private static boolean initSysTray() {
        SystemTray tray = SystemTray.getSystemTray();
        TrayIcon trayIcon = new TrayIcon(getImage(), "tray icon");

        // Create a pop-up menu components
        PopupMenu popup = new PopupMenu();
        buildPopupMenu(popup);
        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
            return true;
        } catch (AWTException e) {
            return false;
        }
    }

    private static Image getImage() {
        Image image = Toolkit.getDefaultToolkit().getImage(dls.getClass().getResource("/Download_Icon.png"));
        image = image.getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        return image;
    }

    private static boolean buildPopupMenu(PopupMenu popup) {
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
        pauseMenu.addItemListener(new PauseButtonListener(dls));
        optionsItem.addActionListener(new OptionsButtonListener(dls));
        openItem.addActionListener(new OpenButtonListener());
        exitItem.addActionListener(new ExitButtonListener());

        return true;
    }

    @Override
    public void setResult(String result) {
        if (enableTrayIcon) {
            if (results.getItemCount() == 10) results.remove(0);
            results.add(new MenuItem(result));
        }
    }

    @Override
    public void startDownloadQueue() {
        log.finest("beeperHandle.start");

        beeperHandle = scheduler.scheduleAtFixedRate(downloadStarter, 0, downloadInterval, MINUTES);
    }

    @Override
    public void cancelDownloadQueue() {
        log.finest("beeperHandle.cancel");
        beeperHandle.cancel(true);
    }

    @Override
    public boolean setDownloadSize(int size) {
        if (size > 0 && size <= 210) {
            log.info("DL size was set to " + size);
            dlsize = size;
            return true;
        }
        return false;
    }

    @Override
    public int getDefaultDownloadSize() {
        return arguments.getSize();
    }

    @Override
    public int getDownloadSize() {
        return dlsize;
    }

    @Override
    public boolean setDownloadLink(String url) {
        DownloadService.url = url;
        return true;
    }

    @Override
    public String getDwonloadLink() {
        return url;
    }

    @Override
    public String getDefaultDownloadLink() {
        return arguments.getUrl();
    }

    @Override
    public boolean setDownloadInterval(int interval) {
        downloadInterval = interval;
        return true;
    }

    @Override
    public int getDownloadInterval() {
        return downloadInterval;
    }

    @Override
    public int getDefaultDownloadInterval() {
        return arguments.getInterval();
    }

}
