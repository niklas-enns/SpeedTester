/*
package niklasu.speedtester.ui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import niklasu.speedtester.config.ConfigStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.stereotype.Component;



*/
/**
 * Created by enzo on 25.02.2017.
 *//*

@org.springframework.stereotype.Component
public class UIFX extends Application {
    Menu results;
    @Autowired
    ConfigStore configStore;

    @Override
    public void start(Stage primaryStage) throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();
        TrayIcon trayIcon = new TrayIcon(getImage(), "tray icon");
        // Create a pop-up menu components
        PopupMenu popup = new PopupMenu();
        buildPopupMenu(popup);
        trayIcon.setPopupMenu(popup);
        tray.add(trayIcon);

        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
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

    public static void go() {
        launch();
    }
}
*/
