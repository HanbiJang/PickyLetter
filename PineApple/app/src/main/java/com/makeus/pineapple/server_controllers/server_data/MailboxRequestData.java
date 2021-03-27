package com.makeus.pineapple.server_controllers.server_data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MailboxRequestData {
    Integer userId, page;
    String endDate, startDate;
    public MailboxRequestData(Integer userId, Integer page, Integer days) {
        this.userId = userId;
        this.endDate = calToday();
        this.page = page;
        this.startDate = calStart(days);
    }

    public MailboxRequestData(Integer userId, Integer page) {
        this.userId = userId;
        this.endDate = calToday();
        this.page = page;
        this.startDate = "1000-01-01";
    }
    //

    private String calToday(){
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
