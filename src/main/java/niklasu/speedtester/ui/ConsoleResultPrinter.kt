package niklasu.speedtester.ui

import org.slf4j.LoggerFactory

class ConsoleResultPrinter {
    private val logger = LoggerFactory.getLogger(ConsoleResultPrinter::class.java)
    fun show(resultSpeed: Double) {
        logger.info(String.format("%.2f MBit/s", resultSpeed))
    }
}