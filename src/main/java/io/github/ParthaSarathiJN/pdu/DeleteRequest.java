package io.github.ParthaSarathiJN.pdu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.ByteBuffer;

import static io.github.ParthaSarathiJN.common.Constants.DELETE_REQ;

public class DeleteRequest extends RequestPacket {

    private static final Logger logger = LoggerFactory.getLogger(DeleteRequest.class);

    private PDU pdu;

    public DeleteRequest() {}

    public DeleteRequest(byte[] keyBytes) {
        PDUHeader pduHeader = new PDUHeader(DELETE_REQ);
        PDUBase pduBase = new RequestPacket(keyBytes);
        pduHeader.setLength(pduHeader.calculateLength() + pduBase.calculateLength() + this.calculateLength());
        this.pdu = new PDU(pduHeader, pduBase, this);
    }

    @Override
    public ByteBuffer getData() {
        return ByteBuffer.allocate(0);
    }

    @Override
    public void setData(ByteBuffer buffer) {
        return;
    }

    @Override
    public int calculateLength() {
        return 0;
    }

    public PDU getPdu() {
        return this.pdu;
    }
}
