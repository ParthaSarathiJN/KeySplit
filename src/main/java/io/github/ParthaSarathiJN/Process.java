//package io.github.ParthaSarathiJN;
//
//import io.github.ParthaSarathiJN.pdu.*;
//
//import java.nio.charset.StandardCharsets;
//
//import static io.github.ParthaSarathiJN.Main.keySplit;
//import static io.github.ParthaSarathiJN.common.Constants.NO_ERROR;
//
//public class Process {
//
//    public BasePacket processGetPacket(GetRequest getRequest) {
//        System.out.println("InsertRequest Process: Operation:" + getRequest.getOperation() + ", Key:" + new String(getRequest.getKeyBytes(), StandardCharsets.US_ASCII));
//        return new GetResponse(keySplit.get(getRequest.getKeyBytes()), NO_ERROR);
//    }

//    public BasePacket processInsertPacket(InsertRequest insertRequest) {
//        keySplit.put(insertRequest.getKeyBytes(), insertRequest.getValueBytes());
//        System.out.println(keySplit.get(insertRequest.getKeyBytes()));
//        return new InsertResponse(0);
//    }
//
//}
//