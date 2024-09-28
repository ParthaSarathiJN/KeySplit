package io.github.ParthaSarathiJN.pdu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.ByteBuffer;

import static io.github.ParthaSarathiJN.common.Constants.INSERT_RESP;

public class InsertResponse implements PDUBase {

    private static final Logger logger = LoggerFactory.getLogger(InsertResponse.class);

    private PDU pdu;

    public InsertResponse() {}

    public InsertResponse(int status) {
        PDUHeader pduHeader = new PDUHeader(INSERT_RESP);
        PDUBase pduBase = new ResponsePacket(status);
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
