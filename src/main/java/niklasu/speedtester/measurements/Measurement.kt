package niklasu.speedtester.measurements

import java.time.LocalDateTime

data class Measurement(val now: LocalDateTime, val speed: Double) {
    override fun toString(): String {
        return "{\"date\":\"$now\",\"speed\":$speed}"
    }
}