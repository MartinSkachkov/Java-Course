import bg.sofia.uni.fmi.mjt.eventbus.events.BasicEvent;
import bg.sofia.uni.fmi.mjt.eventbus.events.StringPayload;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.DeferredEventSubscriber;

import java.time.Instant;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        // Създаване на subscriber за отложени събития
        DeferredEventSubscriber<BasicEvent<StringPayload>> subscriber = new DeferredEventSubscriber<>();

        // Създаване на няколко събития с различни приоритети и таймстампове
        BasicEvent<StringPayload> event1 = new BasicEvent<>(Instant.now().minusSeconds(100), 1, "Source1", new StringPayload("Event 1"));
        BasicEvent<StringPayload> event2 = new BasicEvent<>(Instant.now().minusSeconds(200), 2, "Source2", new StringPayload("Event 2"));
        BasicEvent<StringPayload> event3 = new BasicEvent<>(Instant.now().minusSeconds(50), 3, "Source3", new StringPayload("Event 3"));
        BasicEvent<StringPayload> event4 = new BasicEvent<>(Instant.now().minusSeconds(150), 1, "Source4", new StringPayload("Event 4"));

        // Добавяне на събития към subscriber-а
        subscriber.onEvent(event1);
        subscriber.onEvent(event2);
        subscriber.onEvent(event3);
        subscriber.onEvent(event4);

        // Принтиране на всички събития в сортиран ред (по приоритет и timestamp)
        System.out.println("Sorted events by priority and timestamp:");
        Iterator<BasicEvent<StringPayload>> eventIterator = subscriber.iterator();
        while (eventIterator.hasNext()) {
            BasicEvent<StringPayload> event = eventIterator.next();
            System.out.println(event);
        }
    }
}