package com.lllllllhp.model.game;

public class Time {
    private int hour;
    private int minute;
    private int second;

    public Time(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public Time(int minute, int second) {
        this(0, minute, second);
    }

    public Time(int second) {
        this(0, 0, second);
    }

    static public Time copyTime(Time time) {
        return new Time(time.hour, time.minute, time.second);
    }

    public void addSeconds(int add) {
        second += add;
        while (true) {
            if (second >= 60) {
                minute++;
                second -= 60;
            } else break;
        }
        while (true) {
            if (minute >= 60) {
                hour++;
                minute -= 60;
            } else break;
        }
    }

    public void minusSeconds(int minus) {
        second -= minus;
        while (true) {
            if (second < 0) {
                minute--;
                second += 60;
            } else break;
        }
        while (true) {
            if (minute < 0) {
                hour--;
                minute += 60;
            } else break;
        }
    }

    //------------------------------------------------------
    public int getTotal() {
        return getHour() * 3600 + getMinute() * 60 + getSecond();
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d", getHour(), getMinute(), getSecond());
    }
}
