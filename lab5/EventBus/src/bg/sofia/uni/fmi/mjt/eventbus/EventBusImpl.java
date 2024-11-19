package bg.sofia.uni.fmi.mjt.eventbus;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import bg.sofia.uni.fmi.mjt.eventbus.exception.MissingSubscriptionException;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.Subscriber;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventBusImpl implements EventBus {
    private final Map<Class<? extends Event<?>>, Set<Subscriber<?>>> eventSubscribers;
    private final Map<Class<? extends Event<?>>, List<Event<?>>> eventLogs;

    public EventBusImpl() {
        this.eventSubscribers = new HashMap<>();
        this.eventLogs = new HashMap<>();
    }

    @Override
    public <T extends Event<?>> void subscribe(Class<T> eventType, Subscriber<? super T> subscriber) {
        if (eventType == null) {
            throw new IllegalArgumentException("Event type cannot be null");
        }

        if (subscriber == null) {
            throw new IllegalArgumentException("Subscriber cannot be null");
        }

        eventSubscribers.putIfAbsent(eventType, new HashSet<>());
        eventSubscribers.get(eventType).add(subscriber);
    }

    @Override
    public <T extends Event<?>> void unsubscribe(Class<T> eventType, Subscriber<? super T> subscriber)
            throws MissingSubscriptionException {

        if (eventType == null) {
            throw new IllegalArgumentException("Event type cannot be null");
        }

        if (subscriber == null) {
            throw new IllegalArgumentException("Subscriber cannot be null");
        }

        Set<Subscriber<?>> subscribers = eventSubscribers.get(eventType);
        if (subscribers == null || !subscribers.remove(subscriber)) {
            throw new MissingSubscriptionException("Subscriber not found for event type");
        }

    }

    @Override
    public <T extends Event<?>> void publish(T event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }

        Class<?> eventType = event.getClass();
        Set<Subscriber<?>> subscribers = eventSubscribers.getOrDefault(eventType, Collections.emptySet());

        for (Subscriber<?> subscriber : subscribers) {
            ((Subscriber<T>) subscriber).onEvent(event);
        }

        eventLogs.putIfAbsent((Class<? extends Event<?>>) event.getClass(), new ArrayList<>());
        eventLogs.get(event.getClass()).add(event);
    }

    @Override
    public void clear() {
        eventSubscribers.clear();
        eventLogs.clear();
    }

    @Override
    public Collection<? extends Event<?>> getEventLogs(Class<? extends Event<?>> eventType, Instant from,
                                                       Instant to) {
        if (eventType == null) {
            throw new IllegalArgumentException("Event type cannot be null");
        }

        if (from == null) {
            throw new IllegalArgumentException("Start timestamp cannot be null");
        }

        if (to == null) {
            throw new IllegalArgumentException("End timestamp cannot be null");
        }

        List<Event<?>> logs = eventLogs.getOrDefault(eventType, Collections.emptyList());
        List<Event<?>> result = new ArrayList<>();

        for (Event<?> event : logs) {
            if (!event.getTimestamp().isBefore(from) && event.getTimestamp().isBefore(to)) {
                result.add(event);
            }
        }

        result.sort(new TimeStampComparator());
        return Collections.unmodifiableList(result);
    }

    @Override
    public <T extends Event<?>> Collection<Subscriber<?>> getSubscribersForEvent(Class<T> eventType) {
        if (eventType == null) {
            throw new IllegalArgumentException("Event type cannot be null");
        }

        return Collections.unmodifiableSet(eventSubscribers.getOrDefault(eventType, Collections.emptySet()));
    }
}
