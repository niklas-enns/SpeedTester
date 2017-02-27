package niklasu.speedtester.events;

import java.util.Date;

/**
 * Created by enzo on 25.02.2017.
 */
public class ResultEvent {
    private Date date;
    private double speed;

    public ResultEvent(Date date, double speed) {
        this.date = date;
        this.speed = speed;
    }

    public Date getDate() {
        return date;
    }

    public double getSpeed() {
        return speed;
    }
}
