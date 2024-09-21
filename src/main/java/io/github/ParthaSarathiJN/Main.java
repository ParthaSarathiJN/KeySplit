//package io.github.ParthaSarathiJN;
//
//import io.github.ParthaSarathiJN.pdu.GetRequest;
//import io.github.ParthaSarathiJN.pdu.GetResponse;
//import io.github.ParthaSarathiJN.pdu.InsertRequest;
//import io.github.ParthaSarathiJN.pdu.InsertResponse;
//
//import java.nio.charset.StandardCharsets;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class Main {
//
//    public static ConcurrentHashMap<byte[], byte[]> keySplit = new ConcurrentHashMap<>();
//
//    public static void main(String[] args) {
//
//        Main main = new Main();
//        main.start();
//
//    }
//
//    public void start() {
//        Process process = new Process();
//
//        String testReq = "processRequest", testResp = "processResponseggfdgfg";
//
//        InsertRequest insertRequest = new InsertRequest(testReq.getBytes(StandardCharsets.US_ASCII),
//                testResp.getBytes(StandardCharsets.US_ASCII));
//
//        InsertResponse insertResponse = (InsertResponse) process.processInsertPacket(insertRequest);
//        System.out.println("InsertResp ResponseCode:" + insertResponse.getStatus());
//
//        GetRequest getRequest = new GetRequest(testReq.getBytes(StandardCharsets.US_ASCII));
//
//        GetResponse getResponse = (GetResponse) process.processGetPacket(getRequest);
//        System.out.println("Value:" + new String(getResponse.getValueBytes(), StandardCharsets.US_ASCII) + ", ResponseCode:" + getResponse.getStatus());
//
//    }
//
//}