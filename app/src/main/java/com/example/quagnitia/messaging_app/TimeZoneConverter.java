package com.example.quagnitia.messaging_app;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Niki on 06-08-2018.
 */

public class TimeZoneConverter {

    public static String localToGMTDate(String time) {
        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String currentDate = formatter1.format(calendar1.getTime());
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm aa", Locale.ENGLISH);
            Date dbDateTime = formatter.parse(currentDate + " " + time);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            String gmt = sdf.format(dbDateTime);
            return gmt.toString();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static String oldGMTToNewGMTDate(String time) {
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.ENGLISH);
        Calendar calendar1 = Calendar.getInstance();
        try {
            Date date = sdfs.parse(time);
            calendar1.setTime(date);

            Calendar c = Calendar.getInstance();

            if (c.getTimeInMillis() < calendar1.getTimeInMillis()) {
                return time;
            } else {
                Calendar calendar2 = Calendar.getInstance();
                calendar2.add(Calendar.DAY_OF_YEAR, 1);
                SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String currentDate = formatter1.format(calendar2.getTime());

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm aa", Locale.ENGLISH);
                String tt = "";
                if (calendar1.get(Calendar.AM_PM) == 0)
                    tt = "AM";
                else
                    tt = "PM";

                Date dbDateTime = formatter.parse(currentDate + " " + calendar1.get(Calendar.HOUR_OF_DAY) + ":" + calendar1.get(Calendar.MINUTE) + " " + tt);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.ENGLISH);
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                String gmt = sdf.format(dbDateTime);
                return gmt.toString();
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }


    @NonNull
    public static String gmttoLocalDate(String time) {
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        try {
            Date date = sdfs.parse(time);
            System.out.println(date);
            String timeZone = Calendar.getInstance().getTimeZone().getID();
            Date local = new Date(date.getTime() + TimeZone.getTimeZone(timeZone).getOffset(date.getTime()));
            String locl = sdfs.format(local);
            return locl.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @NonNull
    public static String gmttoLocalDateNoLocale(String time) {
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = sdfs.parse(time);
            System.out.println(date);
            String timeZone = Calendar.getInstance().getTimeZone().getID();
            Date local = new Date(date.getTime() + TimeZone.getTimeZone(timeZone).getOffset(date.getTime()));
            String locl = sdfs.format(local);
            return locl.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String gmtDateToLocalTime(String dateS) {
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        try {
            Date date = sdfs.parse(dateS);
            System.out.println(date);

            String timeZone = Calendar.getInstance().getTimeZone().getID();
            Date local = new Date(date.getTime() + TimeZone.getTimeZone(timeZone).getOffset(date.getTime()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            String time = sdf.format(local);
            return time;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String gmtDateToLocalTimeNOlocale(String dateS) {
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        try {
            Date date = sdfs.parse(dateS);
            System.out.println(date);

            String timeZone = Calendar.getInstance().getTimeZone().getID();
            Date local = new Date(date.getTime() + TimeZone.getTimeZone(timeZone).getOffset(date.getTime()));
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            String time = sdf.format(local);
            return time;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


    public static String localDateToGMTTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gmt = new Date(sdf.format(date));
        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        String time = sdf2.format(gmt);
        return time;
    }


}
