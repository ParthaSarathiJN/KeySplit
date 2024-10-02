package io.github.ParthaSarathiJN.pdu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.ByteBuffer;

import static io.github.ParthaSarathiJN.common.Constants.UPDATE_REQ;

public class UpdateRequest extends RequestPacket {

    private static final Logger logger = LoggerFactory.getLogger(UpdateRequest.class);

    private PDU pdu;

    private int valueLength;
    private byte[] valueBytes;

    public UpdateRequest() {}

    public UpdateRequest(byte[] keyBytes, byte[] valueBytes) {
        this.valueLength = valueBytes.length;
        this.valueBytes = valueBytes;

        PDUHeader pduHeader = new PDUHeader(UPDATE_REQ);
        PDUBase pduBase = new RequestPacket(keyBytes);
        pduHeader.setLength(pduHeader.calculateLength() + pduBase.calculateLength() + this.calculateLength());
        this.pdu = new PDU(pduHeader, pduBase, this);
    }

    @Override
    public ByteBuffer getData() {
        ByteBuffer buffer = ByteBuffer.allocate(4 + valueLength);
        buffer.putInt(valueLength);
        buffer.put(valueBytes);
        logger.info("Serialized valueLength UpdateRequest: {}", valueLength);
        logger.info("Serialized valueBytes in UpdateRequest: {}", new String(valueBytes));
        buffer.flip();
        return buffer;
    }

    @Override
    public void setData(ByteBuffer buffer) {
        this.valueLength = buffer.getInt();
        logger.info("Deserialized valueLength in UpdateRequest: {}", this.valueLength);
        if (valueLength > 0) {
            this.valueBytes = new byte[valueLength];
            buffer.get(this.valueBytes);      // Deserialize key bytes
            logger.info("Deserialized valueBytes in UpdateRequest: {}", new String(this.valueBytes));
        } else {
            valueBytes = new byte[0];  // Handle empty case
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
        byte[] valueBytes = new byte[valueLength];
        buffer.get(valueBytes);
        this.valueBytes = valueBytes;
    }

    @Override
    public int calculateLength() {
        return  4 + valueLength;
    }

    public PDU getPdu() {
        return this.pdu;
    }
}
