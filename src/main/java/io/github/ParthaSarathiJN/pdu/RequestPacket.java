package io.github.ParthaSarathiJN.pdu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class RequestPacket implements PDUBase {

    private static final Logger logger = LoggerFactory.getLogger(RequestPacket.class);

    private long timestamp;
    private int keyLength;
    private byte[] keyBytes;

    public RequestPacket() {}

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
        logger.info("Serialized keyLength:{}, keyBytes:{} in RequestPacket", keyLength, new String(keyBytes));
        buffer.flip();
        return buffer;
    }

    @Override
    public void setData(ByteBuffer buffer) {
        this.timestamp = buffer.getLong();
        this.keyLength = buffer.getInt();
        if (keyLength > 0) {
            this.keyBytes = new byte[keyLength];
            buffer.get(this.keyBytes);
            logger.info("Deserialized keyLength:{}, keyBytes{} in RequestPacket", keyLength, new String(this.keyBytes));
        } else {
            keyBytes = new byte[0];
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
