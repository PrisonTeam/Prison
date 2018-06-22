package me.faizaand.prison.events;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Responsible for firing all {@link Subscription}s when a certain event occurs.
 *
 * @since 4.0
 */
public abstract class EventHandler {

    private List<Subscription> subscriptions = new ArrayList<>();

    public void addSubscription(Subscription subscription) {
        this.subscriptions.add(subscription);
    }

    public List<Subscription> getSubscriptions() {
        subscriptions.sort(Comparator.comparingInt(o -> o.getPriority().getLevel()));
        return subscriptions;
    }

}
