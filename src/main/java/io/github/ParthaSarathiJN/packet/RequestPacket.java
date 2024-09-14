package io.github.ParthaSarathiJN.packet;

import java.util.Objects;

public class RequestPacket extends BasePacket {

    private final String key;
    private final long timestamp;

    public RequestPacket(byte operation, String key) {

        this.key = Objects.requireNonNull(key);
        this.timestamp = System.currentTimeMillis();
    }


    public String getKey() {
        return key;
    }

    public long getTimestamp() {
        return timestamp;
    }

}
