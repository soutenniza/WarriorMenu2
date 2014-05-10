package com.warriormenu;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lnguyen on 5/9/14.
 */
public class Day {
    public int open;
    public int close;

    public boolean is24Hour() {
        return this.open == 0000;
    }

    public boolean isClosed() {
        return this.open == 9999;
    }

    public Date openDate() {
        try {
            return new SimpleDateFormat("hhmm").parse(String.format("%04d", this.open));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Date closeDate() {
        try {
            return new SimpleDateFormat("hhmm").parse(String.format("%04d", this.close));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String openTimeString() {
        return DateFormat.getTimeInstance(DateFormat.SHORT).format(this.openDate());
    }

    public String closeTimeString() {
        return DateFormat.getTimeInstance(DateFormat.SHORT).format(this.closeDate());
    }
}
