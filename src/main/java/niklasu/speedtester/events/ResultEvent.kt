package niklasu.speedtester.events

import java.util.*

class ResultEvent(private val date: Date, private val speed: Double) {

    val speedText: String
        get() = String.format("%.2f MBit/s", speed)

    override fun toString(): String {
        return String.format("%s %s Mbit/s", date, String.format("%.2f", speed))
    }
}
