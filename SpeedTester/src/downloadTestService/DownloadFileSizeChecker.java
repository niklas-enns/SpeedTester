package downloadTestService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by enzo on 19.02.2015.
 */
public class DownloadFileSizeChecker {

    public boolean targetIsBiggerThan(URL targetFile, int size){
        try{
            System.out.println("Starting test targetIsBiggerThan");
            ReadableByteChannel rbc = Channels.newChannel(targetFile.openStream());
            FileOutputStream fos = new FileOutputStream("targetIsBiggerThan.data");
            fos.getChannel().transferFrom(rbc, 0, 1000 * 1000 * size);
            File f = new File("targetIsBiggerThan.data");
            System.out.println("Test download size: "+f.length());
            return (f.length() == (size*1000*1000));
        }catch (Exception e){
            System.out.println("Could not download file");
            return false;
        }
    }
}
