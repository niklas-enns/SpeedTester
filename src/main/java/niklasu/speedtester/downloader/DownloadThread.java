package niklasu.speedtester.downloader;

import static niklasu.speedtester.Constants.MB;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Date;

import niklasu.speedtester.influx.InfluxModule;
import niklasu.speedtester.ui.ConsoleResultPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DownloadThread extends Thread {
    private final URL targetFile;
    private final long downloadsizeInMB;
    private final ConsoleResultPrinter consoleResultPrinter;
    private final InfluxModule.InfluxWriter influxWriter;
    Logger logger = LoggerFactory.getLogger("DownloadThread");

    DownloadThread(URL targetFile, long downloadsizeInMB, ConsoleResultPrinter consoleResultPrinter,
            InfluxModule.InfluxWriter influxWriter) {
        this.targetFile = targetFile;
        this.downloadsizeInMB = downloadsizeInMB;
        this.consoleResultPrinter = consoleResultPrinter;
        this.influxWriter = influxWriter;
    }

    //for testing purposes
    long downloadedBytes = 0;

    @Override
    public void run() {
        downloadedBytes = 0;
        try {
            long startTime = new Date().getTime();
            download();
            long runtime = new Date().getTime() - startTime;
            double resultSpeed = ((double) downloadedBytes / MB) / ((double) runtime / 1000.0) * 8.0;
            consoleResultPrinter.show(resultSpeed);
            influxWriter.write(resultSpeed);
        } catch (DownloadException e) {
            logger.error("Download failed, because", e);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private void download() throws DownloadException {
        int bufferSize = 1 * MB;
        byte[] byteBuffer = new byte[bufferSize];
        try (InputStream stream = targetFile.openStream()){

            while (downloadedBytes < downloadsizeInMB * MB) {
                int read = stream.read(byteBuffer, 0, bufferSize);
                downloadedBytes += read;
                consoleResultPrinter.showProgress(downloadedBytes, downloadsizeInMB * MB);
            }
            logger.debug("Downloaded " + downloadedBytes + " Bytes ~ " + downloadedBytes / MB + " MB");
        } catch (IOException e) {
            throw new DownloadException("", e);
        }

    }
}
