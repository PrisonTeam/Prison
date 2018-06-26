package me.faizaand.prison.signs;

import java.util.ArrayList;
import java.util.List;

/**
 * Makes sure each sign of a specific type stays up to date.
 *
 * @since 4.0
 */
public class DisplaySignAdapter {

    private String identifier;
    private long refreshRate;
    private List<DisplaySign> signs; // signs that we're concerned about here

    public DisplaySignAdapter(String identifier, long refreshRate) {
        this.identifier = identifier;
        this.refreshRate = refreshRate;
        this.signs = new ArrayList<>();
    }

    public String getIdentifier() {
        return identifier;
    }

    public long getRefreshRate() {
        return refreshRate;
    }

    public void addSign(DisplaySign sign) {
        this.signs.add(sign);
    }

    public List<DisplaySign> getSigns() {
        return signs;
    }
}
