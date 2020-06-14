package niklasu.speedtester.measurements

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class Measurement(
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        val timeStamp: LocalDateTime,
        val speed: Double
)