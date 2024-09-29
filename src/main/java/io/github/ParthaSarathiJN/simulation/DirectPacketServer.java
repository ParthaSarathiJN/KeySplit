package io.github.ParthaSarathiJN.simulation;

import io.github.ParthaSarathiJN.pdu.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static io.github.ParthaSarathiJN.common.Constants.*;

public class DirectPacketServer {

    private static final Logger logger = LoggerFactory.getLogger(DirectPacketServer.class);

    public static void main(String[] args) {

        Map<ByteBuffer, byte[]> keyValueMap = new ConcurrentHashMap<>();

        try (ServerSocket serverSocket = new ServerSocket(8080)) {

            logger.error("Server listening on port 8080...");

            while (true) {

                Socket socket = serverSocket.accept();
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();

                PDU pdu = new PDU();

                // Receive and deserialize PDU
                byte[] pduBytes = new byte[100];  // Adjust size accordingly
                int bytesRead = in.read(pduBytes);
                logger.debug("Bytes received: {}", bytesRead);

                ByteBuffer buffer = ByteBuffer.wrap(pduBytes);
                logger.info("Received raw data: {}", Arrays.toString(pduBytes));

                PDU receivedReq = pdu.createPdu(buffer);

                RequestPacket requestPacket = (RequestPacket) receivedReq.getPduBase();
                byte[] value = new byte[0];

                if (receivedReq.getOperation() == GET_REQ) {
                    logger.info("Received GetRequest PDU in Server");
                    GetRequest getRequest = (GetRequest) receivedReq.getImplPacket();
                    value = keyValueMap.get(ByteBuffer.wrap(requestPacket.getKeyBytes()));
                    logger.error("Value is: {}", keyValueMap.get(ByteBuffer.wrap(requestPacket.getKeyBytes())));
                } else if (receivedReq.getOperation() == INSERT_REQ) {
                    logger.info("Received InsertRequest PDU in Server");
                    InsertRequest insertRequest = (InsertRequest) receivedReq.getImplPacket();
                    keyValueMap.put(ByteBuffer.wrap(requestPacket.getKeyBytes()), insertRequest.getValueBytes());
                    logger.error("Added as size {}", keyValueMap.size());
                }

                logger.info("Header Length: {}", receivedReq.getLength());
                logger.info("Request Key: {}", new String((requestPacket.getKeyBytes())));

                PDU sendingResp = null;

                if (receivedReq.getOperation() == GET_REQ) {
                    GetResponse getResp = new GetResponse(value, 0);
                    sendingResp = getResp.getPDU();
                } else if (receivedReq.getOperation() == INSERT_REQ) {
                    InsertResponse insertResponse = new InsertResponse(0);
                    sendingResp = insertResponse.getPdu();
                }
                // Send the response
                ByteBuffer responseBuffer = sendingResp.getData();
                out.write(responseBuffer.array());
                out.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
