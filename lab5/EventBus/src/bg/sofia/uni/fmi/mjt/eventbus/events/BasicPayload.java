package bg.sofia.uni.fmi.mjt.eventbus.events;

public class BasicPayload<T> implements Payload<T> {
    private final int size;
    private final T content;

    public BasicPayload(int size, T content) {
        this.size = size;
        this.content = content;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public T getPayload() {
        return content;
    }
}
