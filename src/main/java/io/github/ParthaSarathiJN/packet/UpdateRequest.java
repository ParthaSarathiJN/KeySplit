package io.github.ParthaSarathiJN.packet;

import java.nio.ByteBuffer;
import java.util.Objects;

import static io.github.ParthaSarathiJN.common.Constants.UPDATE_REQ;

public class UpdateRequest extends RequestPacket {

    private int valueLength;
    private byte[] valueBytes;

    public UpdateRequest(byte[] keyBytes, byte[] valueBytes) {
        super(UPDATE_REQ, keyBytes);
        this.valueLength = Objects.requireNonNull(valueBytes).length;
        this.valueBytes = valueBytes;
        calculateLength();
    }

    @Override
    protected void fromByteArray(byte[] data) {
        super.fromByteArray(data);
        setValueLength(buffer.getInt());
        setValueBytes(buffer);
    }

    public int getValueLength() {
        return valueLength;
    }

    public void setValueLength(int valueLength) {
        this.valueLength = valueLength;
    }

    public byte[] getValueBytes() {
        return valueBytes;
    }

    public void setValueBytes(ByteBuffer buffer) {
        byte[] valueBytes = new byte[valueLength];
        buffer.get(valueBytes);
        this.valueBytes = valueBytes;
    }

    @Override
    protected int calculateLength() {
        return super.calculateLength() + 4 + valueLength;
    }
}
