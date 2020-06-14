package niklasu.speedtester.measurements

import java.time.LocalDateTime

class Measurements {
    val measurements = mutableListOf<Measurement>()

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