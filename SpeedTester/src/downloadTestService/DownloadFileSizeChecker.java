package downloadTestService;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Logger;

/**
 * Created by enzo on 19.02.2015.
 */
public class DownloadFileSizeChecker {
    private static final Logger log = Logger.getLogger(DownloadFileSizeChecker.class.getName());
    public boolean targetIsBiggerThan(URL targetFile, int size){
        try{
            log.info("Checking if the target file is bigger than "+size+" MB");
            ReadableByteChannel rbc = Channels.newChannel(targetFile.openStream());
            FileOutputStream fos = new FileOutputStream("targetIsBiggerThan.data");
            fos.getChannel().transferFrom(rbc, 0, 1000 * 1000 * size);
            File f = new File("targetIsBiggerThan.data");
            log.info("Test download size: " + f.length());
            return (f.length() == (size*1000*1000));
        }catch (Exception e){
            log.info("Could not download file");
            return false;
        }
    }
}
