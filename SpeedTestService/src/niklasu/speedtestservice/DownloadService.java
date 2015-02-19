package niklasu.speedtestservice;

import niklasu.speedtestservice.interfaces.ServiceHost;
import niklasu.speedtestservice.listeners.ExitButtonListener;
import niklasu.speedtestservice.listeners.OpenButtonListener;
import niklasu.speedtestservice.listeners.PauseButtonListener;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.MINUTES;

public class DownloadService implements ServiceHost {
    static State state;
    static DownloadService dls = new DownloadService();
    static ScheduledFuture<?> beeperHandle;
    static ScheduledExecutorService scheduler;
    static int dlsize = 50;
    static Menu results;

    public static void main(String[] args) {
        state = State.IDLE;
        scheduler = Executors.newScheduledThreadPool(1);
        dls.startDownloadQueue();

        if (SystemTray.isSupported()) {
            initSysTray();
        }else{
            System.out.println("TrayIcon could not be added.");
        }
    }

    @Override
    public void setResult(String result) {
        if(results.getItemCount()==10) results.remove(0);
        results.add(new MenuItem(result));
    }

    @Override
    public void startDownloadQueue() {
        beeperHandle=scheduler.scheduleAtFixedRate(downloadStarter, 0, 1, MINUTES);
    }

    @Override
    public void cancelDownloadQueue() {
        beeperHandle.cancel(true);
    }

    final Runnable downloadStarter = new Runnable() {
        public void run() {
            System.out.println("Starting Download of "+dlsize+" MB");
            DownloadThread dl = new DownloadThread(dlsize,dls);
            dl.start();
        }
    };
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

    private static Image getImage(){
        Image image = Toolkit.getDefaultToolkit().getImage(dls.getClass().getResource("images/Download_Icon.png"));
        image = image.getScaledInstance(15,15,Image.SCALE_SMOOTH);
    return image;
    }

    private static boolean buildPopupMenu(PopupMenu popup){
        MenuItem aboutItem = new MenuItem("About");
        CheckboxMenuItem pauseMenu = new CheckboxMenuItem("Pause");
        MenuItem openItem = new MenuItem("Open working directory");
        MenuItem exitItem = new MenuItem("Exit");
        //Pseudo-menu:shows last results
        results = new Menu("Last 10 minutes");

        //Add components to pop-up menu
        popup.add(aboutItem);

        popup.addSeparator();
        popup.add(pauseMenu);
        popup.add(openItem);
        popup.add(results);

        popup.addSeparator();
        popup.add(exitItem);

        //Set Listeners
        pauseMenu.addItemListener(new PauseButtonListener(dls));
        openItem.addActionListener(new OpenButtonListener());
        exitItem.addActionListener(new ExitButtonListener());

    return true;
    }

}
