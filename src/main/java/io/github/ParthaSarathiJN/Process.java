package io.github.ParthaSarathiJN;

import io.github.ParthaSarathiJN.packet.BasePacket;
import io.github.ParthaSarathiJN.packet.GetRequest;
import io.github.ParthaSarathiJN.packet.GetResponse;

import java.nio.charset.StandardCharsets;

import static io.github.ParthaSarathiJN.common.Constants.NO_ERROR;

public class Process {

    public BasePacket processPacket(GetRequest getRequest) {
        System.out.println("Operation:" + getRequest.getOperation() + ", Key:" + new String(getRequest.getKeyBytes(), StandardCharsets.US_ASCII));
        return new GetResponse("processResponse".getBytes(StandardCharsets.US_ASCII), NO_ERROR);
    }

}
