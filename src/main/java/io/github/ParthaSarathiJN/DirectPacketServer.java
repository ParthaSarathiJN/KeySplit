package io.github.ParthaSarathiJN;

import io.github.ParthaSarathiJN.pdu.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class DirectPacketServer {

    private static final Logger logger = LoggerFactory.getLogger(DirectPacketServer.class);

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            logger.trace("Server listening on port 8080...");

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

                // Verify the received PDU
                logger.info("Received PDU in Server");
                logger.info("Header Length: {}", receivedReq.getLength());
                logger.info("Request Key: {}", new String(((RequestPacket) receivedReq.getPduBase()).getKeyBytes()));

                GetResponse getResp = new GetResponse("firstResp".getBytes(), 0);
                PDU sendingResp = getResp.getPDU();

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
