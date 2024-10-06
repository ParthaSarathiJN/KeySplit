package io.github.ParthaSarathiJN.server;

import io.github.ParthaSarathiJN.pdu.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import static io.github.ParthaSarathiJN.common.Constants.*;

public class SessionWorker implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SessionWorker.class);

    private final Socket clientSocket;
    private final StoreKeyValue storeKeyValue;

    public SessionWorker(Socket clientSocket, StoreKeyValue storeKeyValue) {
        this.clientSocket = clientSocket;
        this.storeKeyValue = storeKeyValue;
        logger.info("New Client Connected!");
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();

            // Read incoming PDU, deserialize, and process it
            byte[] pduBytes = new byte[1024]; // Buffer size for incoming data

            // Loop until client disconnects or there's an error
            while (true) {
                int bytesRead = inputStream.read(pduBytes);

                // If bytesRead is -1, it means the client has disconnected
                if (bytesRead == -1) {
                    logger.info("Client disconnected.");
                    break; // Exit the loop and close the connection
                }

                ByteBuffer buffer = ByteBuffer.wrap(pduBytes, 0, bytesRead); // Only process the actual bytes read

                PDU pdu = new PDU();

                // Deserialize and create PDU based on packet type
                PDU receivedPdu = pdu.createPdu(buffer);

                // Process based on the operation
                switch (receivedPdu.getOperation()) {
                    case GET_REQ:
                        handleGetRequest(outputStream, receivedPdu);
                        break;
                    case INSERT_REQ:
                        handleInsertRequest(outputStream, receivedPdu);
                        break;
                    case UPDATE_REQ:
                        handleUpdateRequest(outputStream, receivedPdu);
                        break;
                    case DELETE_REQ:
                        handleDeleteRequest(outputStream, receivedPdu);
                        break;
                    default:
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleGetRequest(OutputStream outputStream, PDU receivedPdu) throws IOException {

        RequestPacket requestPacket = (RequestPacket) receivedPdu.getPduBase();
        ByteBuffer key = ByteBuffer.wrap(requestPacket.getKeyBytes());

        // TODO: Check if it does reference check for fetching kv or key bytes match
        byte[] value = new byte[0];
        ByteBuffer bufferValue = storeKeyValue.get(key);

        logger.info("Received GetRequest from Client for Key: {}", new String(requestPacket.getKeyBytes()));

        if (bufferValue != null) {
            value = bufferValue.array();
            logger.info("Fetched Value in Map: {}", new String(value));
        } else {
            logger.info("NO Key in Map to GET: {}", new String(requestPacket.getKeyBytes()));
        }

        GetResponse getResponse = new GetResponse(value, 0);
        PDU responsePdu = getResponse.getPdu();

        logger.info("Sent GetResponse to Client");

        outputStream.write(responsePdu.getData().array());
        outputStream.flush();
    }

    private void handleInsertRequest(OutputStream outputStream, PDU receivedPdu) throws IOException {

        RequestPacket requestPacket = (RequestPacket) receivedPdu.getPduBase();
        ByteBuffer key = ByteBuffer.wrap(requestPacket.getKeyBytes());

        InsertRequest insertRequest = (InsertRequest) receivedPdu.getImplPacket();
        ByteBuffer value = ByteBuffer.wrap(insertRequest.getValueBytes());

        logger.info("Received InsertRequest from Client for Key: {} & Value: {}", new String(requestPacket.getKeyBytes()), new String(insertRequest.getValueBytes()));
        logger.info("Inserted Key:Value in Map");

        ByteBuffer prevValue = storeKeyValue.put(key, value);

        InsertResponse insertResponse = new InsertResponse(0);
        PDU responsePdu = insertResponse.getPdu();

        logger.info("Sent InsertResponse to Client");

        outputStream.write(responsePdu.getData().array());
        outputStream.flush();
    }

    private void handleUpdateRequest(OutputStream outputStream, PDU receivedPdu) throws IOException {

        RequestPacket requestPacket = (RequestPacket) receivedPdu.getPduBase();
        ByteBuffer key = ByteBuffer.wrap(requestPacket.getKeyBytes());

        UpdateRequest updateRequest = (UpdateRequest) receivedPdu.getImplPacket();
        ByteBuffer value = ByteBuffer.wrap(updateRequest.getValueBytes());

        logger.info("Received UpdateRequest from Client for Key: {} & Value: {}", new String(requestPacket.getKeyBytes()), new String(updateRequest.getValueBytes()));
        logger.info("Updated Key:Value in Map");

        ByteBuffer prevValue = storeKeyValue.put(key, value);

        UpdateResponse updateResponse = new UpdateResponse(0);
        PDU responsePdu = updateResponse.getPdu();

        logger.info("Sent UpdateResponse to Client");

        outputStream.write(responsePdu.getData().array());
        outputStream.flush();
    }

    private void handleDeleteRequest(OutputStream outputStream, PDU receivedPdu) throws IOException {

        RequestPacket requestPacket = (RequestPacket) receivedPdu.getPduBase();
        ByteBuffer bufferKey = ByteBuffer.wrap(requestPacket.getKeyBytes());

        ByteBuffer bufferValue = storeKeyValue.remove(bufferKey);

        byte[] value = new byte[0];

        logger.info("Received DeleteRequest from Client for Key: {}", new String(requestPacket.getKeyBytes()));

        if (bufferValue != null) {
            value = bufferValue.array();
            logger.info("Found & Deleted Value in Map: {}", new String(value));
        } else {
            logger.info("NO Key in Map to DELETE: {}", new String(requestPacket.getKeyBytes()));
        }

        DeleteResponse deleteResponse = new DeleteResponse(value, 0);
        PDU responsePdu = deleteResponse.getPdu();

        logger.info("Sent DeleteResponse to Client");

        outputStream.write(responsePdu.getData().array());
        outputStream.flush();
    }
}
