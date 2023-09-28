package niklasu.speedtester.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.ProvisionException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;

class ConfigModuleTest {
    private MockWebServer mockWebServer = new MockWebServer();
    @Test
    void networkError() {
        String[] args = { "-url", "http://x", "-interval", "1", "-size", "10" };
        Injector injector = Guice.createInjector(new ConfigModule(args));
        org.junit.jupiter.api.Assertions.assertThrows(ProvisionException.class,
                () -> injector.getInstance(ConfigProvider.class));
    }

    @Test
    void malformedURL() {
        String[] args = { "-url", "httpppp://x", "-interval", "1", "-size", "10" };
        Injector injector = Guice.createInjector(new ConfigModule(args));
        org.junit.jupiter.api.Assertions.assertThrows(ProvisionException.class,
                () -> injector.getInstance(ConfigProvider.class));
    }


    @Test
    void fileIsTooSmall() {
        mockWebServer.enqueue(new MockResponse().setBody("small content"));

        String url = mockWebServer.url("").toString();
        String[] args = { "-url", url, "-interval", "1", "-size", "10" };
        Injector injector = Guice.createInjector(new ConfigModule(args));

        try {
            injector.getInstance(ConfigProvider.class);
        } catch (Exception e) {
            assertEquals("The size of " + url +" was 13 and is < 1000000 byte", e.getCause().getMessage());
        }
    }

    @Test
    void fileIsSmallerThanRequired() {
        mockWebServer.enqueue(new MockResponse().setHeader("Content-Length", "9000000"));

        String url = mockWebServer.url("").toString();
        String[] args = { "-url", url, "-interval", "1", "-size", "10" };
        Injector injector = Guice.createInjector(new ConfigModule(args));

        try {
            injector.getInstance(ConfigProvider.class);
        } catch (Exception e) {
            assertEquals("${url} has a size of 13 while ${10 * MB} is required", e.getMessage());
        }
    }

    @Test
    void intervalSmaller1() {
        mockWebServer.enqueue(new MockResponse().setHeader("Content-Length", "999999999999999"));
        String[] args = { "-url", mockWebServer.url("").toString(), "-interval", "-1", "-size", "10" };
        Injector injector = Guice.createInjector(new ConfigModule(args));
        try {
            injector.getInstance(ConfigProvider.class);
        } catch (ProvisionException e) {
            assertEquals("Interval must be >= 1. Your input was -1", e.getCause().getMessage());
        }
    }

    /*
    Comparison to ParamValidatorTest:
    + Test shows that when I start the application with these parameters, correct exceptions will be thrown out
    of the application. ParamValidatorTest shows only that the ParamValidator works correctly. Not, that the class has been
    integrated in the application correctly
    + Refactoring everything covered by the ConfigurationModule is way easier! No tight coupling to the implementation anymore
    + When Constructor of ConfigProvider grows, we don't care here

    - When sth fails, we see only the test case and the affected module. We don't see the exact class / method where the
    bug is
     */

}