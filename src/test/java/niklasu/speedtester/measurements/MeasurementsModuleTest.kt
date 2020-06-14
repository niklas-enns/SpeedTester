package niklasu.speedtester.measurements

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.inject.Guice
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals


internal class MeasurementsModuleTest {
    @Test
    fun test() {
        val measurements = Guice.createInjector(MeasurementsModule()).getInstance(Measurements::class.java)
        measurements.add(2.0)
        measurements.add(3.0)
//        assertEquals("[{\"date\":\"${LocalDateTime.now()}\",\"speed\":2.0}]", measurements.toString())
    }

    @Test
    fun serialization() {
        val measurementAsJson = jacksonObjectMapper().registerModule(JavaTimeModule()).writeValueAsString(Measurement(LocalDateTime.of(2020, 3, 3, 3, 3), 332.0))
        assertEquals("{\"timeStamp\":\"2020-03-03 03:03\",\"speed\":332.0}", measurementAsJson)

    }
}