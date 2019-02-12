package niklasu.speedtester.config

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.doThrow
import com.nhaarman.mockito_kotlin.mock
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.io.IOException
import java.net.URL

internal class ParamValidatorTest {

    @Test
    fun NetworkError() {
        val url = URL("http://fred.de")
        val fileSizeChecker = mock<FileSizeChecker>{on {getFileSize(url)} doThrow(IOException("Network Error"))}
        val paramValidator = ParamValidator(fileSizeChecker)

        val config = Config(100, 2, url.toString())

        assertThrows(ValidationException::class.java) { paramValidator.validate(config) }.printStackTrace()
    }

    @Test
    @Throws(java.io.IOException::class)
    fun malformedURL() {
        val fileSizeChecker = mock<FileSizeChecker>{}
        val paramValidator = ParamValidator(fileSizeChecker)

        val config = Config(100, 2, "httasdddasdp://fred.de")

        assertThrows(ValidationException::class.java) { paramValidator.validate(config) }.printStackTrace()
    }

    @Test
    @Throws(java.io.IOException::class)
    fun fileIsTooSmall() {
        val url = URL("http://fred.de")
        val fileSizeChecker = mock<FileSizeChecker>{on {getFileSize(url)} doReturn (1)}
        val paramValidator = ParamValidator(fileSizeChecker)

        val config = Config(100, 2, "http://fred.de")

        assertThrows(ValidationException::class.java) { paramValidator.validate(config) }.printStackTrace()
    }

    @Test
    @Throws(java.io.IOException::class)
    fun intervalLessThanOne() {
        val url = URL("http://fred.de")
        val fileSizeChecker = mock<FileSizeChecker>{on {getFileSize(url)} doReturn (99999999999999)}
        val paramValidator = ParamValidator(fileSizeChecker)

        val config = Config(100, 0, "http://fred.de")

        assertThrows(ValidationException::class.java) { paramValidator.validate(config) }.printStackTrace()
    }
}