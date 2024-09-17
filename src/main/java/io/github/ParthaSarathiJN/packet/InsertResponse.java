package io.github.ParthaSarathiJN.packet;

import static io.github.ParthaSarathiJN.common.Constants.INSERT_RESP;

public class InsertResponse extends ResponsePacket {

    public InsertResponse(int status) {
        super(INSERT_RESP, status);
        this.length = calculateLength();
    }

    @Override
    protected int calculateLength() {
        return super.calculateLength();
    }

}
