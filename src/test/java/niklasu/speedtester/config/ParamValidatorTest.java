package niklasu.speedtester.config;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.Test;

class ParamValidatorTest {

    @Test
    void NetworkError() throws IOException, FileSizeChecker.NoFileSizeException, ValidationException {
        URL url = new URL("http://fred.de");
        FileSizeChecker fileSizeChecker = mock(FileSizeChecker.class);
        when(fileSizeChecker.getFileSize(any())).thenThrow(new IOException("kaputt"));

        ParamValidator paramValidator = new ParamValidator(fileSizeChecker);

        assertThrows(ValidationException.class, () -> paramValidator.validate(100L, url.toString()));
    }

    @Test
    void malformedURL() {
        FileSizeChecker fileSizeChecker = mock(FileSizeChecker.class);
        ParamValidator paramValidator = new ParamValidator(fileSizeChecker);

        assertThrows(ValidationException.class, () -> paramValidator.validate(100L, "httasdddasdp://fred.de"));

    }

    @Test
    void fileIsTooSmall() throws IOException, FileSizeChecker.NoFileSizeException, ValidationException {
        FileSizeChecker fileSizeChecker = mock(FileSizeChecker.class);
        when(fileSizeChecker.getFileSize(any())).thenReturn(1L);
        ParamValidator paramValidator = new ParamValidator(fileSizeChecker);
        assertThrows(ValidationException.class, () -> paramValidator.validate(100L, "http://fred.de"));
    }

}