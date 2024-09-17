package io.github.ParthaSarathiJN.packet;

import java.nio.ByteBuffer;
import java.util.UUID;

public abstract class BasePacket {

    protected int length;
    private byte operation;
    private byte[] uuidByteArr;
    protected ByteBuffer buffer;

    public BasePacket(byte operation) {
        this.operation = operation;
        this.uuidByteArr = generateUUID();
    }

    protected void fromByteArray(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        setLength(buffer.getInt());
        setOperation(buffer.get());
        setUuidByteArr(buffer);
    }

    public int getLength() {
        return length;
    }

    private void setLength(int length) {
        this.length = length;
    }

    public byte getOperation() {
        return operation;
    }

    private void setOperation(byte operation) {
        this.operation = operation;
    }

    public UUID getUuid() {
        ByteBuffer buffer = ByteBuffer.wrap(uuidByteArr);
        return new UUID(buffer.getLong(), buffer.getLong());
    }

    private void setUuidByteArr(ByteBuffer buffer) {
        byte[] uuidByteArr = new byte[16];
        buffer.get(uuidByteArr);
        this.uuidByteArr = uuidByteArr;
    }

    protected int calculateLength() {
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
