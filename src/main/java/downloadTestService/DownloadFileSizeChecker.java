package downloadTestService;


import downloadTestService.exceptions.BadFileException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Created by enzo on 19.02.2015.
 */
public class DownloadFileSizeChecker {
    private static final Logger log = Logger.getLogger(DownloadFileSizeChecker.class.getName());

    public long getFileSize(URL targetFile) throws BadFileException {
        HttpClient client = new DefaultHttpClient();
        HttpHead head = new HttpHead(String.valueOf(targetFile));
        HttpResponse response = null;
        try {
            response = client.execute(head);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Header[] size = response.getHeaders("Content-length");
        if (size.length == 0) throw new BadFileException("The HTTP response has no Content-Length field");
        log.finest("" + size[0].getValue());
        String contentLength = size[0].getValue().replaceAll("[^\\d.]", ""); //delete all non-numbers
        long contentLengthAsInt = Long.parseLong(contentLength);
        return contentLengthAsInt;
    }
}
