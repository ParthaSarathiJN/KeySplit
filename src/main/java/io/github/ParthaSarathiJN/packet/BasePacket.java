package io.github.ParthaSarathiJN.packet;

import java.nio.ByteBuffer;
import java.util.UUID;

public abstract class BasePacket {

    private int length;
    private byte operation;
    private byte[] uuidByteArr;

    public BasePacket(byte operation, String key) {
        this.operation = operation;
        this.uuidByteArr = generateUUID();
        this.length = calculateLength();
    }

    public void fromByetAray(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        this.length = buffer.getInt();
        this.operation = buffer.get();
        this.uuidByteArr = buffer.array();
    }

    public int getLength() {
        return length;
    }

    public byte getOperation() {
        return operation;
    }

    public UUID getUuid() {
        ByteBuffer buffer = ByteBuffer.wrap(uuidByteArr);
        return new UUID(buffer.getLong(), buffer.getLong());
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setOperation(byte operation) {
        this.operation = operation;
    }

    public void setUuidByteArr(byte[] uuidByteArr) {
        this.uuidByteArr = uuidByteArr;
    }

    private int calculateLength() {
        return 4 + 1 + 16;
    }

    private byte[] generateUUID() {
        UUID uuid = UUID.randomUUID();
        ByteBuffer uuidByteBuffer = ByteBuffer.wrap(new byte[16]);
        uuidByteBuffer.putLong(uuid.getMostSignificantBits());
        uuidByteBuffer.putLong(uuid.getLeastSignificantBits());
        return uuidByteBuffer.array();
    }

}
