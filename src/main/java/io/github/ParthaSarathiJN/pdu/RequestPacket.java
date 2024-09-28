package io.github.ParthaSarathiJN.pdu;

import java.nio.ByteBuffer;

public class RequestPacket implements PDUBase {

    private long timestamp;
    private int keyLength;
    private byte[] keyBytes;

    public RequestPacket() {
    }

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
        System.out.println("Serialized keyLength in RequestPacket: " + keyLength);
        System.out.println("Serialized keyBytes in RequestPacket: " + new String(keyBytes));
        buffer.flip();
        return buffer;
    }

    @Override
    public void setData(ByteBuffer buffer) {
        this.timestamp = buffer.getLong();
        this.keyLength = buffer.getInt();
        System.out.println("Deserialized keyLength in RequestPacket: " + this.keyLength);
        if (keyLength > 0) {
            this.keyBytes = new byte[keyLength];
            buffer.get(this.keyBytes);      // Deserialize key bytes
            System.out.println("Deserialized keyBytes in RequestPacket: " + new String(this.keyBytes));
        } else {
            keyBytes = new byte[0];  // Handle empty case
        }
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

    public int calculateLength() {
        return 12 + keyLength;
    }
}
