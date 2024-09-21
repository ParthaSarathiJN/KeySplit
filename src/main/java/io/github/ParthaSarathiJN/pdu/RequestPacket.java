package io.github.ParthaSarathiJN.pdu;

import java.nio.ByteBuffer;

public class RequestPacket extends PDU {

    private long timestamp;
    private int keyLength;
    private byte[] keyBytes;

    public RequestPacket(byte[] keyBytes) {
        this.timestamp = getCurrentTimestamp();
        this.keyLength = keyBytes.length;
        this.keyBytes = keyBytes;
    }

    @Override
    public ByteBuffer getData() {
        ByteBuffer buffer = ByteBuffer.allocate(12 + keyLength);
        buffer.putLong(getTimestamp());
        buffer.putInt(getKeyLength());
        buffer.put(keyBytes);
        return buffer;
    }

    @Override
    public void setData(ByteBuffer buffer) {
        this.timestamp = buffer.getLong();
        this.keyLength = buffer.getInt();
        keyBytes = new byte[keyLength];
        buffer.get(keyBytes);
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
}
