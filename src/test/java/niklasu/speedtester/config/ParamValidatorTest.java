package niklasu.speedtester.config;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ParamValidatorTest {

    @Test
    void IOException() throws IOException {
        final FileSizeChecker fileSizeChecker = mock(FileSizeChecker.class);
        when(fileSizeChecker.getFileSize(any())).thenThrow(new IOException("kapuu"));
        ParamValidator paramValidator = new ParamValidator(fileSizeChecker);

        Config config = new Config(100, 2, "http://fred.de");

        assertThrows(ValidationException.class, () -> paramValidator.validate(config)).printStackTrace();
    }

    @Test
    void malformedURL() throws IOException {
        final FileSizeChecker fileSizeChecker = mock(FileSizeChecker.class);
        when(fileSizeChecker.getFileSize(any())).thenThrow(new MalformedURLException("url ist doof"));
        ParamValidator paramValidator = new ParamValidator(fileSizeChecker);

        Config config = new Config(100, 2, "http://fred.de");

        assertThrows(ValidationException.class, () -> paramValidator.validate(config)).printStackTrace();
    }

    @Test
    void fileIsTooSmall() throws IOException {
        final FileSizeChecker fileSizeChecker = mock(FileSizeChecker.class);
        when(fileSizeChecker.getFileSize(any())).thenReturn(1L);
        ParamValidator paramValidator = new ParamValidator(fileSizeChecker);

        Config config = new Config(100, 2, "http://fred.de");

        assertThrows(ValidationException.class, () -> paramValidator.validate(config)).printStackTrace();
    }

    @Test
    void intervalLessThanOne() throws IOException {
        final FileSizeChecker fileSizeChecker = mock(FileSizeChecker.class);
        when(fileSizeChecker.getFileSize(any())).thenReturn(99999999999L);
        ParamValidator paramValidator = new ParamValidator(fileSizeChecker);

        Config config = new Config(100, 0, "http://fred.de");

        assertThrows(ValidationException.class, () -> paramValidator.validate(config)).printStackTrace();
    }
}