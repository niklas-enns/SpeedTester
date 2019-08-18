package niklasu.speedtester.measurements

import java.time.LocalDateTime
import java.util.*

class Measurements {
    val measurements = LinkedList<Measurement>()

    fun add(speed: Double) {
        measurements.add(Measurement(LocalDateTime.now(), speed))
    }

    fun get() {
        measurements
    }

    override fun toString(): String {
        return measurements.toString()
    }
}