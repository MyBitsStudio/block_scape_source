package com.rs.game.player;

import java.io.Serializable;
import java.util.Calendar;
import java.util.TimeZone;

public class DailyLoginManager implements Serializable {

    private static final long serialVersionUID = 208038418624564836L;

    private transient Player player;

    public void setPlayer(Player player) {this.player = player; }

    private int dayOfMonth,dayOfWeek,month, week;

    public DailyLoginManager(){

    }

    public int getDayOfMonth(){ return this.dayOfMonth; }

    public int getDayOfWeek(){ return this.dayOfWeek; }

    public int getMonth(){ return this.month; }

    public int getWeek() { return this.week;}

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public void setDayOfWeek(int dayOfWeek){
        this.dayOfWeek = dayOfWeek;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setWeek(int week){
        this.week = week;
    }

    private int getCalDayOfMonth(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("EST"));
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    private int getCalDayOfWeek(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("EST"));
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
    private int getCalMonth() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("EST"));
        return calendar.get(Calendar.MONTH);
    }
    private int getCalWeek() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("EST"));
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    public void checkDaily(){
        if(this.dayOfMonth != getCalDayOfMonth()){
            runDailyCheck();
            setDayOfMonth(getCalDayOfMonth());
        }
        if(this.dayOfWeek != getCalDayOfWeek()){
            runDayOfWeekCheck();
            setDayOfWeek(getCalDayOfWeek());
        }
        if(this.week != getCalWeek()){
            runWeeklyCheck();
            setWeek(getCalWeek());
        }
        if(this.month != getCalMonth()){
            runMonthCheck();
            setMonth(getCalMonth());
        }
    }

    private void runMonthCheck() {
        //put monthly here
    }

    private void runDayOfWeekCheck() {
        //put day of week here

    }

    private void runWeeklyCheck(){

    }

    private void runDailyCheck() {
        //well place here for dailies
        player.getDonationManager().dailyLogin();
        player.getDailyTaskManager().getNewTask(false);
    }


}
