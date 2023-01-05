package com.rs;

import com.rs.game.item.Item;

import java.util.Calendar;

public class WorldSettings {

    public static boolean donationBoostActive = false, donationBoostItemActive = false;
    public static double donationBoost = 1.0;
    public static Item donationBoostItem = null;

    private static final Calendar cal = Calendar.getInstance();

    public static int getMonth(){
        return cal.get(Calendar.MONTH);
    }

    public static int getDayOfMonth(){
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int getWeek(){
        return cal.get(Calendar.WEEK_OF_MONTH);
    }

    public static int getDayOfWeek(){
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * Weekend Boots
     */

    public static boolean isSlayerWeekend(){
        return getDayOfWeek() >= 5 && getDayOfWeek() < 7;
    }

    public static boolean isDungeonWeekend(){
        return getDayOfWeek() >= 5 && getDayOfWeek() < 7;
    }

    public static boolean isDoubleCoins(){
        return getDayOfMonth() >= 15 && getDayOfMonth() < 18;
    }

    public static boolean isPrestigeBoosted(){
        return getDayOfMonth() >= 7 && getDayOfMonth() < 13;
    }

}
