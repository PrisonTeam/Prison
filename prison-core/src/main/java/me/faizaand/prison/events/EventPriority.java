package me.faizaand.prison.events;

/**
 * Event priorities dictate the order in which an event subscription is alerted.
 * High priority events get the final say in what happens in the end. Lower priority events
 * "hear" about the event first.
 *
 * @since 4.0
 */
public enum EventPriority {

    HIGH(3), NORMAL(2), LOW(1), MONITOR(0);

    private int level;

    EventPriority(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

}
