package bg.sofia.uni.fmi.mjt.eventbus.events;

import java.time.Instant;

public class BasicEvent<T extends Payload<?>> implements Event<T> {
    private final Instant timestamp;
    private final int priority;
    private final String source;
    private final T payload;

    public BasicEvent(Instant timestamp, int priority, String source, T payload) {
        this.timestamp = timestamp;
        this.priority = priority;
        this.source = source;
        this.payload = payload;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public T getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "Event{" +
                "timestamp=" + timestamp +
                ", priority=" + priority +
                ", source='" + source + '\'' +
                ", payload=" + payload +
                '}';
    }
}