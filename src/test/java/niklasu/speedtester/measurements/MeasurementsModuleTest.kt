package niklasu.speedtester.measurements

import com.google.inject.Guice
import org.junit.jupiter.api.Test

internal class MeasurementsModuleTest {
    @Test
    fun test() {
        val measurements = Guice.createInjector(MeasurementsModule()).getInstance(Measurements::class.java)
        measurements.add(2.0)
        measurements.add(3.0)
//        assertEquals("[{\"date\":\"${LocalDateTime.now()}\",\"speed\":2.0}]", measurements.toString())
    }
}