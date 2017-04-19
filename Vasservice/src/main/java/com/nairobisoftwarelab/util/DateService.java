package com.nairobisoftwarelab.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Martin on 24/01/2016.
 */
public class DateService {
    private SimpleDateFormat df;

    public String now() {
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    /**
     * Return current time
     *
     * @return
     */
    public String currentTime() {
        df = new SimpleDateFormat("HH:mm:ss");
        return df.format(new Date());
    }

    public String currentDateTime(Date date) {
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }

    public String formattedTime() {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new Date());
        calendar2.add(Calendar.HOUR_OF_DAY, +7);

        //Date date = calendar2.getTime();

        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(date);
    }
}
