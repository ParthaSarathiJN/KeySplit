package io.github.ParthaSarathiJN;

import io.github.ParthaSarathiJN.pdu.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class DirectPacketClient {

    private static final Logger logger = LoggerFactory.getLogger(DirectPacketClient.class);

    public static void main(String[] args) {

        try (Socket socket = new Socket("localhost", 8080)) {
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            GetRequest getReq = new GetRequest("firstTest".getBytes());
            PDU fullPdu = getReq.getPDU();

            // Send the PDU
            ByteBuffer buffer = fullPdu.getData();
            byte[] pduBytes = buffer.array();
            logger.info("Client sending data: {}", Arrays.toString(buffer.array()));
            out.write(pduBytes);
            out.flush();

            // Receive and deserialize the response PDU
            byte[] responseBytes = new byte[1024];  // Adjust size accordingly
            in.read(responseBytes);
            ByteBuffer responseBuffer = ByteBuffer.wrap(responseBytes);

            PDU reqPdu = new PDU();

            // Deserialize the PDU based on expected packet types
            PDU receivedReq = reqPdu.createPdu(responseBuffer);

            // Verify the response
            logger.error("Received PDU:");
            logger.info("Header Length: {}", receivedReq.getLength());
            String value = null;
            if (receivedReq.getOperation() == -1) {
                GetResponse getResponse = (GetResponse) receivedReq.getImplPacket();
                value = new String(getResponse.getValueBytes());
            }
            logger.info("Response Key: {}", value);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
