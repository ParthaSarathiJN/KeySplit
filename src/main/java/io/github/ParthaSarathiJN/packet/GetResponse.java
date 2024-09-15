package io.github.ParthaSarathiJN.packet;

import static io.github.ParthaSarathiJN.common.Constants.GET_RESP;

public class GetResponse extends ResponsePacket {

    private String value;

    public GetResponse(String value, int status) {
        super(GET_RESP, status);
        this.value = getValue();
    }

    public b

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
