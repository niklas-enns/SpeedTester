package downloadTestService;

import downloadTestService.interfaces.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by enzo on 06.11.2014.
 */
public class DownloadThread extends Thread implements Constants {
    private static final Logger log = Logger.getLogger(DownloadFileSizeChecker.class.getName());
    URL targetFile;
    long downloadSize;
    DownloadService host;
    Date startOfDownload = new Date();

    public DownloadThread(long size, DownloadService h, URL url) {
        targetFile = url;
        downloadSize = size;
        host = h;
    }

    public void run() {
        long startTime = startOfDownload.getTime();
        download();
        long runningTime = new Date().getTime() - startTime;
        double resultSpeed = ((double) downloadSize / ((double) runningTime / 1000.)) / MB * 8.;
        log.info("Download of " + downloadSize / MB + "MB completed, calculated Speed: " + String.format("%.2f", resultSpeed) + " MBit/s");
        sendResultToHost(startOfDownload, resultSpeed);
        appendToLogFile(startOfDownload, resultSpeed);

    }

    private void download() {
        try {
            log.info("Starting download...");
            ReadableByteChannel rbc = Channels.newChannel(targetFile.openStream());
            FileOutputStream fos = new FileOutputStream("temp.dat");
            fos.getChannel().transferFrom(rbc, 0, downloadSize);

        } catch (Exception e) {
            log.severe("Could not download file");
        }
    }

    private void sendResultToHost(Date dateOfDownload, double resultSpeed) {
        SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm");
        String time = sdftime.format(dateOfDownload);

        host.setResult(time + " - " + String.format("%.2f", resultSpeed) + " Mbit/s");
    }

    private void appendToLogFile(Date dateOfDownload, double resultSpeed) {
        File file = new File("results.txt");
        FileWriter writer;
        try {
            //init writer with append
            writer = new FileWriter(file, true);
            String date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(dateOfDownload);
            writer.write("" + date + " " + String.format("%.2f", resultSpeed) + " Mbit/s");
            writer.write(System.getProperty("line.separator"));

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
