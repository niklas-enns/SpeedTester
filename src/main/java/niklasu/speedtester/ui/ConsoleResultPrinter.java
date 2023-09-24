package niklasu.speedtester.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleResultPrinter {
    private final Logger logger = LoggerFactory.getLogger(ConsoleResultPrinter.class);

    public void show(double resultSpeed) {
        logger.info(String.format("%.2f MBit/s", resultSpeed));
    }

    public void showProgress(long current, long total) {
        int amount = (int) (((double) current / (double) total) * 100);
        char[] animationChars = { '|', '/', '-', '\\' };
        System.out.print("Processing: " + amount + "% " + animationChars[amount % 4] + "\r");
    }
}