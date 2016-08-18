package time;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by manabu on 2016/08/03.
 */
public class TimeStamp {
    public static String getTimeStamp() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String timestamp = sdf.format(date);
        return timestamp;
    }
}
