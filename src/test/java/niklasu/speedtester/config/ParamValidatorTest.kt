package niklasu.speedtester.config

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.io.IOException
import java.net.URL

internal class ParamValidatorTest {

    @Test
    fun NetworkError() {
        val url = URL("http://fred.de")
        val fileSizeChecker = mockk<FileSizeChecker> { every { getFileSize(any()) } throws IOException("kaputt") }
        val paramValidator = ParamValidator(fileSizeChecker)

        val config = Config(100, 2, url.toString())

        assertThrows(ValidationException::class.java) { paramValidator.validate(config) }.printStackTrace()
    }

    @Test
    @Throws(java.io.IOException::class)
    fun malformedURL() {
        val fileSizeChecker = mockk<FileSizeChecker> {}
        val paramValidator = ParamValidator(fileSizeChecker)

        val config = Config(100, 2, "httasdddasdp://fred.de")

        assertThrows(ValidationException::class.java) { paramValidator.validate(config) }.printStackTrace()
    }

    @Test
    @Throws(java.io.IOException::class)
    fun fileIsTooSmall() {
        val url = URL("http://fred.de")
        val fileSizeChecker = mockk<FileSizeChecker> { every { getFileSize(url) } returns (1) }
        val paramValidator = ParamValidator(fileSizeChecker)

        val config = Config(100, 2, "http://fred.de")

        assertThrows(ValidationException::class.java) { paramValidator.validate(config) }.printStackTrace()
    }

    @Test
    @Throws(java.io.IOException::class)
    fun intervalLessThanOne() {
        val url = URL("http://fred.de")
        val fileSizeChecker = mockk<FileSizeChecker> { every { getFileSize(url) } returns (99999999999999) }
        val paramValidator = ParamValidator(fileSizeChecker)

        val config = Config(100, 0, "http://fred.de")

        assertThrows(ValidationException::class.java) { paramValidator.validate(config) }.printStackTrace()
    }
}