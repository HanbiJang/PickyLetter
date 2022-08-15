package com.makeus.pineapple;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalStringDate {

    //일정한 형식의 string 에서 년월일, 요일 뽑기
    public static String calDate(String today) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(today); //2018-06-10T15:00:00Z
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Integer year = Integer.valueOf(new SimpleDateFormat("yyyy").format(date)); // 년
        Integer month = Integer.valueOf(new SimpleDateFormat("MM").format(date)); // 월
        Integer days = Integer.valueOf(new SimpleDateFormat("dd").format(date)); // 일

        String newDate = month + "/" + days + "/" + year;
        return newDate;
    }
}
