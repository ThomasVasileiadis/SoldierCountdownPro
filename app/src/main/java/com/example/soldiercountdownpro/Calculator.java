package com.example.soldiercountdownpro;

import android.os.Build;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import androidx.annotation.RequiresApi;

public class Calculator {

    private String startDate;
    private String endDate;
    DateTimeFormatter formatter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Calculator(String startDate, String endDate) {
        formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long calculateDifference() {
        LocalDate date1 = LocalDate.parse(this.startDate, formatter);
        LocalDate date2 = LocalDate.parse(this.endDate, formatter);
        long daysBetween = Duration.between(date1.atStartOfDay(), date2.atStartOfDay()).toDays();
        return daysBetween;
    }
}
