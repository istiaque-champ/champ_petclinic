package com.team2.prescriptionservice.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class date {
    public static boolean isValid(String str) {
        DateFormat correctFormat = new SimpleDateFormat("yyyy-MM-dd");
        correctFormat.setLenient(false);
        try {
            correctFormat.parse(str);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
