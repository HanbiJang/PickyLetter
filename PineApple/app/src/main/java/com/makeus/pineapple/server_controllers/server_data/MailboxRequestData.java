package com.makeus.pineapple.server_controllers.server_data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MailboxRequestData {
    Integer userId, page;
    String endDate, startDate;

    public MailboxRequestData(){}

    // 최근 레터 찾기
    public MailboxRequestData(Integer userId, Integer page, Integer days) {
        this.userId = userId;
        this.endDate = calToday();
        this.page = page;
        this.startDate = calStart(days);
    }

    //지난 레터 찾기 (최근 일주일 빼기)
    public MailboxRequestData(Integer userId, Integer page) {
        this.userId = userId;
        this.endDate = calToday();
        this.page = page;
        this.startDate = "1000-01-01";
    }

    //지정 날짜 찾기 (필터)
    public MailboxRequestData(Integer userId, Integer page, String endDate, String startDate) {
        this.userId = userId;
        this.endDate = endDate;
        this.startDate = startDate;
        this.page = page;
    }

    //날짜 관련 함수
    //날짜를 대입하면 요일을 구하는 함수
    public String calDay(String date){
        String day = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date nDate = null;
        try {
            nDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);

        switch (dayNum) {
            case 1:
                day = "일";
                break;
            case 2:
                day = "월";
                break;
            case 3:
                day = "화";
                break;
            case 4:
                day = "수";
                break;
            case 5:
                day = "목";
                break;
            case 6:
                day = "금";
                break;
            case 7:
                day = "토";
                break;

        }

        return day+"요일";

    }


    public String calToday(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("current: " + dateFormat.format(cal.getTime()));
        endDate = dateFormat.format(cal.getTime()); //오늘
        return endDate;
    }

    private String calStart(Integer days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        cal.add(Calendar.DATE, days);
        System.out.println("after: " + dateFormat.format(cal.getTime()));
        startDate = dateFormat.format(cal.getTime()); //한주
        return  startDate;
    }

    //


    public Integer getUserId() {
        return userId;
    }

    public String getEndDate() {
        return endDate;
    }

    public Integer getPage() {
        return page;
    }

    public String getStartDate() {
        return startDate;
    }

    //


    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
