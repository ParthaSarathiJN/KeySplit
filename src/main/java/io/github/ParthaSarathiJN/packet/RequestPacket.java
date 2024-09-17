package io.github.ParthaSarathiJN.packet;

import java.nio.ByteBuffer;
import java.util.Objects;

public abstract class RequestPacket extends BasePacket {

    private long timestamp;
    private int keyLength;
    private byte[] keyBytes;

    public RequestPacket(byte operation, byte[] keyBytes) {
        super(operation);
        this.timestamp = getCurrentTimestamp();
        this.keyLength = Objects.requireNonNull(keyBytes).length;
        this.keyBytes = keyBytes;
    }

    @Override
    protected void fromByteArray(byte[] data) {
        super.fromByteArray(data);
        setTimestamp(buffer.getLong());
        setKeyLength(buffer.getInt());
        setKeyBytes(buffer);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getKeyLength() {
        return keyLength;
    }

    public void setKeyLength(int keyLength) {
        this.keyLength = keyLength;
    }

    public byte[] getKeyBytes() {
        return keyBytes;
    }

    private void setKeyBytes(ByteBuffer buffer) {
        byte[] keyBytes = new byte[keyLength];
        buffer.get(keyBytes);
        this.keyBytes = keyBytes;
    }

    private long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    @Override
    protected int calculateLength() {
        return super.calculateLength() + 8 + 4 + keyLength;
    }
}
