package niklasu.speedtester.config

import com.google.inject.Guice
import com.google.inject.ProvisionException
import niklasu.speedtester.MB
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

internal class ConfigModuleTest {
    val mockWebServer = MockWebServer()
    @Test
    fun networkError() {
        val args = arrayOf("-url", "http://x", "-interval", "1", "-size", "10")
        val injector = Guice.createInjector(ConfigModule(args))
        assertThrows<ProvisionException> { injector.getInstance(ConfigProvider::class.java) }.printStackTrace()
    }

    @Test
    @Throws(java.io.IOException::class)
    fun malformedURL() {
        val args = arrayOf("-url", "httpppp://x", "-interval", "1", "-size", "10")
        val injector = Guice.createInjector(ConfigModule(args))
        assertThrows<ProvisionException> { injector.getInstance(ConfigProvider::class.java) }.printStackTrace()
    }


    @Test
    @Throws(java.io.IOException::class)
    fun fileIsTooSmall() {
        mockWebServer.enqueue(MockResponse().setBody("small content"))

        val url = mockWebServer.url("").toString()
        val args = arrayOf("-url", url, "-interval", "1", "-size", "10")
        val injector = Guice.createInjector(ConfigModule(args))

        try {
            injector.getInstance(ConfigProvider::class.java)
        } catch (e: Exception) {
            assertEquals("The size of ${url} was 13 and is < $MB", e.cause?.message)
        }
    }

    @Test
    @Throws(java.io.IOException::class)
    fun fileIsSmallerThanRequired() {
        mockWebServer.enqueue(MockResponse().setHeader("Content-Length", "9000000"))

        val url = mockWebServer.url("").toString()
        val args = arrayOf("-url", url, "-interval", "1", "-size", "10")
        val injector = Guice.createInjector(ConfigModule(args))

        try {
            injector.getInstance(ConfigProvider::class.java)
        } catch (e: Exception) {
            assertEquals("${url} has a size of 13 while ${10 * MB} is required", e.cause?.message)
        }
    }

    @Test
    @Throws(java.io.IOException::class)
    fun intervalSmaller1() {
        mockWebServer.enqueue(MockResponse().setHeader("Content-Length", "999999999999999"))
        val args = arrayOf("-url", mockWebServer.url("").toString(), "-interval", "-1", "-size", "10")
        val injector = Guice.createInjector(ConfigModule(args))
        try {
            injector.getInstance(ConfigProvider::class.java)
        } catch (e: ProvisionException) {
            assertEquals("Interval must be >= 1. Your input was -1", e.cause?.message)
        }
    }

    /*
    Comparison to ParamValidatorTest:
    + Test shows that when I start the application with these parameters, correct exceptions will be thrown out
    of the application. ParamValidatorTest shows only that the ParamValidator works correctly. Not, that the class has been
    integrated in the application correctly
    + Refactoring everything covered by the ConfigurationModule is way easier! No tight coupling to the implementation anymore
    + When Constructor of ConfigProvider grows, we don't care here

    - I
     */

}