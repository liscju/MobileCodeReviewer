package pl.edu.agh.mobilecodereviewer.utilities;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    //2014-10-21 18:50:59.000000000
    private final static String GERRIT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.S";
    private final static String PRINTABLE_DATE_FORMAT = "yyyy-MM-dd HH:mm";

    public static String getPrettyDate(String gerritFormatDate){
        SimpleDateFormat sdf = new SimpleDateFormat(GERRIT_DATE_FORMAT);
        Date d;
        try {
            d = sdf.parse(gerritFormatDate);
        } catch (ParseException e) {
            Log.e("DATE FORMATTING", "BAD INPUT DATE");
            return gerritFormatDate;
        }

        return getDateInPrintableFormat(d);
    }

    public static String getPrettyDate(Date d){
        return getDateInPrintableFormat(d);
    }

    private static String getDateInPrintableFormat(Date d){
        SimpleDateFormat sdf = new SimpleDateFormat(PRINTABLE_DATE_FORMAT);
        return sdf.format(d);
    }

}
