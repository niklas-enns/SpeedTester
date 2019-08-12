package niklasu.speedtester.ui

import org.slf4j.LoggerFactory

class ConsoleResultPrinter {
    private val logger = LoggerFactory.getLogger(ConsoleResultPrinter::class.java)
    fun show(resultSpeed: Double) {
        logger.info(String.format("%.2f MBit/s", resultSpeed))
    }

    fun showProgress(current: Long, total: Long) {
        val amount = ((current.toDouble() / (total.toDouble())) * 100).toInt()
        val animationChars = charArrayOf('|', '/', '-', '\\')
        System.out.print("Processing: " + amount + "% " + animationChars[amount % 4] + "\r")
    }
}