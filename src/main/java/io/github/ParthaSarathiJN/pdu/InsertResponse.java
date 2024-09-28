package io.github.ParthaSarathiJN.pdu;

import java.nio.ByteBuffer;

import static io.github.ParthaSarathiJN.common.Constants.INSERT_RESP;

public class InsertResponse implements PDUBase {

    public InsertResponse() {
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

}
