package bg.sofia.uni.fmi.mjt.eventbus.subscribers;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;

import java.util.Comparator;

public class EventPriorityComparator<T extends Event<?>> implements Comparator<T> {

    @Override
    public int compare(T event1, T event2) {
        int comparisonPriorityResult = Integer.compare(event1.getPriority(), event2.getPriority());

        if (comparisonPriorityResult != 0) {
            return comparisonPriorityResult;
        }

        return event1.getTimestamp().compareTo(event2.getTimestamp());
    }
}
