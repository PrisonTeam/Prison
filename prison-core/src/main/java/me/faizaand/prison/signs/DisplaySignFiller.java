package me.faizaand.prison.signs;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * A display sign shows users information about a Prison module.
 * For example, a sign can show users how full a mine is.
 *
 * @since 4.0
 */
public class DisplaySignFiller {

    private String identifier;
    private Function<String[], String[]> callback;
    private long refreshRate;
    private TimeUnit unit;

    public DisplaySignFiller(String identifier, Function<String[], String[]> callback, long refreshRate, TimeUnit unit) {
        this.identifier = identifier;
        this.callback = callback;
        this.refreshRate = refreshRate;
        this.unit = unit;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Function<String[], String[]> getCallback() {
        return callback;
    }

    public long getRefreshRate() {
        return refreshRate;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public long getRefreshRateSeconds() {
        return unit.toSeconds(refreshRate);
    }

}
