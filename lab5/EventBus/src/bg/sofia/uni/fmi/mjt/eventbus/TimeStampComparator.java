package bg.sofia.uni.fmi.mjt.eventbus;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;

import java.util.Comparator;

public class TimeStampComparator implements Comparator<Event<?>> {

    @Override
    public int compare(Event<?> event1, Event<?> event2) {
        return event1.getTimestamp().compareTo(event2.getTimestamp());
    }
}
