package bg.sofia.uni.fmi.mjt.eventbus.events;

public class StringPayload implements Payload<String> {
    private final String data;

    public StringPayload(String data) {
        this.data = data;
    }

    @Override
    public int getSize() {
        return data.length();
    }

    @Override
    public String getPayload() {
        return data;
    }
}
