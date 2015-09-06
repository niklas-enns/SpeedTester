package downloadTestService;

import com.beust.jcommander.JCommander;
import downloadTestService.exceptions.BadFileException;
import downloadTestService.exceptions.TooSmallFileException;
import downloadTestService.interfaces.Constants;
import downloadTestService.interfaces.ServiceHost;
import downloadTestService.listeners.ExitButtonListener;
import downloadTestService.listeners.OpenButtonListener;
import downloadTestService.listeners.OptionsButtonListener;
import downloadTestService.listeners.PauseButtonListener;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;

import static java.util.concurrent.TimeUnit.MINUTES;

public class DownloadService implements ServiceHost, Constants {
    private static final Logger log = Logger.getLogger(DownloadFileSizeChecker.class.getName());
    static DownloadService downloadService = new DownloadService();
    static ScheduledFuture<?> beeperHandle;
    static ScheduledExecutorService scheduler;
    static Menu results;
    static DownloadServiceArguments arguments;

    static long downloadSize;
    static int downloadInterval;
    static String url;
    static Boolean enableTrayIcon;

    final Runnable downloadStarter = new Runnable() {
        public void run() {
            try {
                DownloadThread dl = new DownloadThread(downloadSize, downloadService, new URL(url));
                dl.start();
            } catch (Exception e) {
                log.severe("Bad URL");
            }
        }
    };


    public static void main(String[] args) {
        arguments = new DownloadServiceArguments();
        new JCommander(arguments, args);
        downloadSize = arguments.getSize();
        downloadInterval = arguments.getInterval();
        url = arguments.getUrl();
        enableTrayIcon = arguments.getTray();
        try {
            ParamValidator.validateParams(downloadSize, downloadInterval, url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        } catch (TooSmallFileException e) {
            e.printStackTrace();
            return;
        } catch (BadFileException e) {
            e.printStackTrace();
            return;
        }

        scheduler = Executors.newScheduledThreadPool(1);
        downloadService.startDownloadQueue();

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
        Image image = Toolkit.getDefaultToolkit().getImage(downloadService.getClass().getResource("/Download_Icon.png"));
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
        pauseMenu.addItemListener(new PauseButtonListener(downloadService));
        optionsItem.addActionListener(new OptionsButtonListener(downloadService));
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
    public boolean setDownloadSize(long size) {
        if (size > MB && size <= 200 * MB) {
            log.info("DL size was set to " + size);
            downloadSize = size;
            return true;
        }
        return false;
    }

    @Override
    public long getDefaultDownloadSize() {
        return arguments.getSize();
    }

    @Override
    public long getDownloadSize() {
        return downloadSize;
    }

    @Override
    public boolean setDownloadLink(String url) {
        DownloadService.url = url;
        return true;
    }

    @Override
    public String getDownloadURL() {
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
