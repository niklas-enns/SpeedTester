package niklasu.speedtester.config

import com.nhaarman.mockito_kotlin.doThrow
import com.nhaarman.mockito_kotlin.mock
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

internal class KotlinParamValidatorTest {

    @Test
    fun NetworkError() {
        val url = URL("http://fred.de")
        val fileSizeChecker = mock<FileSizeChecker>{on {getFileSize(url)} doThrow(IOException("Network Error"))}
        val paramValidator = KotlinParamValidator(fileSizeChecker)

        val config = Config(100, 2, url.toString())

        assertThrows(ValidationException::class.java) { paramValidator.validate(config) }.printStackTrace()
    }

    @Test
    @Throws(java.io.IOException::class)
    fun malformedURL() {
        val fileSizeChecker = mock<FileSizeChecker>(FileSizeChecker::class.java)
        `when`<Long>(fileSizeChecker.getFileSize(any<URL>())).thenThrow(MalformedURLException("url ist doof"))
        val paramValidator = JavaParamValidator(fileSizeChecker)

        val config = Config(100, 2, "http://fred.de")

        assertThrows(ValidationException::class.java) { paramValidator.validate(config) }.printStackTrace()
    }

    @Test
    @Throws(java.io.IOException::class)
    fun fileIsTooSmall() {
        val fileSizeChecker = mock<FileSizeChecker>(FileSizeChecker::class.java)
        `when`<Long>(fileSizeChecker.getFileSize(any<URL>())).thenReturn(1L)
        val paramValidator = JavaParamValidator(fileSizeChecker)

        val config = Config(100, 2, "http://fred.de")

        assertThrows(ValidationException::class.java) { paramValidator.validate(config) }.printStackTrace()
    }

    @Test
    @Throws(java.io.IOException::class)
    fun intervalLessThanOne() {
        val fileSizeChecker = mock<FileSizeChecker>(FileSizeChecker::class.java)
        `when`<Long>(fileSizeChecker.getFileSize(any<URL>())).thenReturn(99999999999L)
        val paramValidator = JavaParamValidator(fileSizeChecker)

        val config = Config(100, 0, "http://fred.de")

        assertThrows(ValidationException::class.java) { paramValidator.validate(config) }.printStackTrace()
    }
}