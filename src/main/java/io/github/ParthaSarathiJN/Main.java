package io.github.ParthaSarathiJN;

import io.github.ParthaSarathiJN.packet.GetRequest;
import io.github.ParthaSarathiJN.packet.GetResponse;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {

        ConcurrentHashMap<String, String> keySplit = new ConcurrentHashMap<>();

        String test = "processRequest";
        GetRequest getRequest = new GetRequest(test.getBytes(StandardCharsets.US_ASCII));

        Process process = new Process();
        GetResponse getResponse = (GetResponse) process.processPacket(getRequest);
        System.out.println("Value:" + new String(getResponse.getValueBytes(), StandardCharsets.US_ASCII) + ", ResponseCode:" + getResponse.getStatus());
    }
}