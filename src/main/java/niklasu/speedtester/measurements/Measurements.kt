package niklasu.speedtester.measurements

import java.util.*

class Measurements {
    val measurements = LinkedList<Double>()

    fun add(speed: Double) {
        measurements.add(speed)
    }

    fun get() {
        measurements
    }
}