package niklasu.speedtester.events;

import java.util.Date;

public class ResultEvent {
    private Date date;
    private double speed;

    public ResultEvent(Date date, double speed) {
        this.date = date;
        this.speed = speed;
    }

    @Override
    public String toString() {
        return String.format("%s %s Mbit/s", date, String.format("%.2f", speed));
    }

    public String getSpeedText() {
        return String.format("%.2f MBit/s", speed);
    }
}
