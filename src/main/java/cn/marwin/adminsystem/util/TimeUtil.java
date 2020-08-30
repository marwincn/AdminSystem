package cn.marwin.adminsystem.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    public static String getCurrentTime() {
        Date dateTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(dateTime);
    }

    public static String getDate(String dateTime) {
        if (dateTime.length() > 10) {
            return dateTime.substring(0, 10);
        } else {
            return dateTime;
        }
    }

    public static String getTime(String dateTime) {
        if (dateTime.length() > 19) {
            return dateTime.substring(11, 19);
        } else {
            return dateTime;
        }
    }

    public static String getDateTime(String dateTime) {
        if (dateTime.length() > 19) {
            return dateTime.substring(0, 19);
        } else {
            return dateTime;
        }
    }
}
