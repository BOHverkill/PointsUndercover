package org.bohverkill.pointsundercover.model;

import java.text.DecimalFormat;

public class Countdown {

    public final static DecimalFormat COUNTDOWN_FORMATTER = new DecimalFormat("00");
    private int minutes;
    private int seconds;

    public Countdown() {
        this(0, 0);
    }

    public Countdown(int minutes, int seconds) {
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public Countdown(Countdown countdown) {
        this(countdown.getMinutes(), countdown.getSeconds());
    }

    public int getMinutes() {
        return this.minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return this.seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    @Override
    public String toString() {
        return Countdown.COUNTDOWN_FORMATTER.format(this.getMinutes()) + ":" + Countdown.COUNTDOWN_FORMATTER.format(this.getSeconds());
    }

    public long toMillis() {
        return (this.getMinutes() * 60 + this.getSeconds()) * 1000;
    }
}
