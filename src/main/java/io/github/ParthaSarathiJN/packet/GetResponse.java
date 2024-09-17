package io.github.ParthaSarathiJN.packet;

import java.nio.ByteBuffer;
import java.util.Objects;

import static io.github.ParthaSarathiJN.common.Constants.GET_RESP;

public class GetResponse extends ResponsePacket {

    private int valueLength;
    private byte[] valueBytes;

    public GetResponse(byte[] valueBytes, int status) {
        super(GET_RESP, status);
        this.valueLength = Objects.requireNonNull(valueBytes).length;
        this.valueBytes = valueBytes;
        this.length = calculateLength();
    }

    @Override
    public void fromByteArray(byte[] data) {
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
        valueBytes = new byte[valueLength];
        buffer.get(valueBytes);
    }

    @Override
    protected int calculateLength() {
        return super.calculateLength() + 4 + valueLength;
    }
}
