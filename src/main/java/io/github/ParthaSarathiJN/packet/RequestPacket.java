package io.github.ParthaSarathiJN.packet;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

public class RequestPacket extends BasePacket {

    private String key;
    private long timestamp;

    public RequestPacket(byte operation, String key) {
        super(operation);
        this.timestamp = System.currentTimeMillis();
        this.key = Objects.requireNonNull(key);
    }

    @Override
    public void fromByteArray(byte[] data) {
        super.fromByteArray(data);
        ByteBuffer buffer = ByteBuffer.wrap(data);
        this.timestamp = buffer.getLong();
        this.key = Arrays.toString(buffer.array());
    }

    public String getKey() {
        return key;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int calculateLength() {
        return super.calculateLength() + 16 + (key.length() * 2);
    }
}
