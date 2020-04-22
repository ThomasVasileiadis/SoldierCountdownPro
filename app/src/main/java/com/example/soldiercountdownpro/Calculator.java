package com.example.soldiercountdownpro;

import android.os.Build;
import androidx.annotation.RequiresApi;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Calculator  {

    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private SimpleDateFormat formatter;
    List<String> dateFormatStrings = Arrays.asList(
            "yyyy-MM-dd HH:mm",
            "yyyy-M-dd HH:mm",
            "yyyy-MM-dd H:mm",
            "yyyy-M-dd H:mm");

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Calculator(String startDate, String startTime,String endDate, String endTime) {
        try {
            this.startDate = startDate;
            this.startTime = startTime;
            this.endDate = endDate;
            this.endTime = endTime;
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    //Calculate elapsed time in milliseconds from start end date
    public long calculateElapsed() throws ParseException {
        Date date1 = this.parseDate(this.startDate + " " + this.startTime  ); // Start
        Date date2 = this.parseDate(this.endDate + " " + this.endTime ); // End

        long endTime = date1.getTime();
        Calendar calendar = Calendar.getInstance();
        long currtime = calendar.getTimeInMillis();

        return endTime - currtime;
    }

    //Calculate elapsed time in days until end date
    public long calculateElapsedInDays() throws ParseException {
        Date date1 = this.parseDate(this.startDate + " " + this.startTime  ); // Start
        Date date2 = this.parseDate(this.endDate + " " + this.endTime ); // End
        long endTime = date1.getTime();
        long diff = date2.getTime() - date1.getTime();
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        long currentTime= System.currentTimeMillis();
        long elapsedTime =  currentTime - endTime;
        long elapsedTimeInDays = elapsedTime / 86400000;

        return elapsedTimeInDays;
    }

    //Calculate elapsed time in weeks until end date
    public long calculateElapsedInWeeks() throws ParseException {
        Date date1 = this.parseDate(this.startDate + " " + this.startTime  ); // Start
        Date date2 = this.parseDate(this.endDate + " " + this.endTime ); // End
        long endTime = date1.getTime();
        long diff = date2.getTime() - date1.getTime();
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        long currentTime= System.currentTimeMillis();
        long elapsedTime =  currentTime - endTime;
        long elapsedTimeInWeeks = elapsedTime / 604800000;

        return elapsedTimeInWeeks;
    }

    //Calculate difference between two dates in milliseconds
    public long calculateDifference() throws ParseException {
        Date date1 = this.parseDate(this.startDate + " " + this.startTime  ); // Start
        Date date2 = this.parseDate(this.endDate + " " + this.endTime ); // End
        long diff = date1.getTime() - date2.getTime() ;

        return diff;
    }

    //Method that is used to "combine" date and time
    private Date parseDate(String date) {
        for (String pattern:dateFormatStrings) {
            try{
                return new SimpleDateFormat(pattern).parse(date);
            } catch(Exception ignored){
            }
        }
        return null;
    }
}
