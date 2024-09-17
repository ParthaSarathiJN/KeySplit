package io.github.ParthaSarathiJN.packet;

import static io.github.ParthaSarathiJN.common.Constants.GET_REQ;

public class GetRequest extends RequestPacket {

    public GetRequest(byte[] keyBytes) {
        super(GET_REQ, keyBytes);
        this.length = calculateLength();
    }

}
