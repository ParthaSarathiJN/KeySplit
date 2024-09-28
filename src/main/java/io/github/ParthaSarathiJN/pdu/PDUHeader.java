package io.github.ParthaSarathiJN.pdu;

import java.nio.ByteBuffer;
import java.util.UUID;

public class PDUHeader implements PDUPacket {

    private int length;
    private byte operation;
    private byte[] uuidByteArr = new byte[16];

    public ByteBuffer getData() {
        ByteBuffer buffer = ByteBuffer.allocate(21);
        buffer.putInt(getLength());
        buffer.put(getOperation());
        buffer.put(getUuidByteArr());
        buffer.flip();
        return buffer;
    }

    public void setData(ByteBuffer buffer) {
        this.length = buffer.getInt();
        this.operation = buffer.get();
        buffer.get(uuidByteArr);
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte getOperation() {
        return operation;
    }

    public void setOperation(byte operation) {
        this.operation = operation;
    }

    public UUID getUuid() {
        ByteBuffer buffer = ByteBuffer.wrap(uuidByteArr);
        return new UUID(buffer.getLong(), buffer.getLong());
    }

    public byte[] getUuidByteArr() {
        return uuidByteArr;
    }

    public void setUuidByteArr(ByteBuffer buffer) {
        byte[] uuidByteArr = new byte[16];
        buffer.get(uuidByteArr);
        this.uuidByteArr = uuidByteArr;
    }

    public int calculateLength() {
        return 4 + 1 + 16;
    }

    public byte[] generateUUID() {
        UUID uuid = UUID.randomUUID();
        ByteBuffer uuidByteBuffer = ByteBuffer.wrap(new byte[16]);
        uuidByteBuffer.putLong(uuid.getMostSignificantBits());
        uuidByteBuffer.putLong(uuid.getLeastSignificantBits());
        return uuidByteBuffer.array();
    }

}
