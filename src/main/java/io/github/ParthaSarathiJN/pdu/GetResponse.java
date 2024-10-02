package io.github.ParthaSarathiJN.pdu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.ByteBuffer;

import static io.github.ParthaSarathiJN.common.Constants.GET_RESP;

public class GetResponse implements PDUPacket {

    private static final Logger logger = LoggerFactory.getLogger(GetResponse.class);

    private PDU pdu;

    private int valueLength;
    private byte[] valueBytes;

    public GetResponse() {}

    public GetResponse(byte[] valueBytes, int status) {
        this.valueLength = valueBytes.length;
        this.valueBytes = valueBytes;

        PDUHeader pduHeader = new PDUHeader(GET_RESP);
        PDUBase pduBase = new ResponsePacket(status);
        pduHeader.setLength(pduHeader.calculateLength() + pduBase.calculateLength() + this.calculateLength());
        this.pdu = new PDU(pduHeader, pduBase, this);
    }

    @Override
    public ByteBuffer getData() {
        ByteBuffer buffer = ByteBuffer.allocate(4 + valueLength);
        buffer.putInt(valueLength);
        buffer.put(valueBytes);
        buffer.flip();
        return buffer;
    }

    @Override
    public void setData(ByteBuffer buffer) {
        this.valueLength = buffer.getInt();
        if (valueLength > 0) {
            valueBytes = new byte[valueLength];
            buffer.get(valueBytes);
        } else {
            valueBytes = new byte[0]; // Handle empty case
        }
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

    public int calculateLength() {
        return 4 + valueLength;
    }

    public PDU getPdu() {
        return this.pdu;
    }
}
