package com.example.soldiercountdownpro;

import android.os.Build;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.RequiresApi;

public class Calculator {

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

    public long calculateDifference() throws ParseException {
        Date date1 = this.parseDate(this.startDate + " " + this.startTime  );
        Date date2 = this.parseDate(this.endDate + " " + this.endTime );
        long diff = date2.getTime() - date1.getTime();
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        return days;
    }

    private Date parseDate(String date) {
        for (String pattern:dateFormatStrings) {
            try{
                return new SimpleDateFormat(pattern).parse(date);
            } catch(Exception e){

            }
        }
        return null;
    }

}
