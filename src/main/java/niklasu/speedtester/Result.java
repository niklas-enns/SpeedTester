package niklasu.speedtester;

import java.util.Date;

/**
 * Created by enzo on 25.02.2017.
 */
public class Result {
    public Date date;
    public double speed;
    public Result(Date date, double speed){
        this.date = date;
        this.speed = speed;
    }
}
