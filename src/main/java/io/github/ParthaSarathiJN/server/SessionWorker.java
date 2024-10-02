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
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();

            // Read incoming PDU, deserialize, and process it
            byte[] pduBytes = new byte[1024]; // Buffer size for incoming data
            inputStream.read(pduBytes);
            ByteBuffer buffer = ByteBuffer.wrap(pduBytes);

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleGetRequest(OutputStream outputStream, PDU receivedPdu) throws IOException {

        RequestPacket requestPacket = (RequestPacket) receivedPdu.getPduBase();
        ByteBuffer key = ByteBuffer.wrap(requestPacket.getKeyBytes());

        // TODO: Check if it does reference check for fetching kv or key bytes match
        byte[] value = storeKeyValue.get(key).array();

        logger.info("Received GetRequest from Client for Key: {}", new String(requestPacket.getKeyBytes()));
        logger.info("Found Value in Map: {}", new String(value));

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

        ByteBuffer prevValue = storeKeyValue.update(key, value);

        UpdateResponse updateResponse = new UpdateResponse(0);
        PDU responsePdu = updateResponse.getPdu();

        logger.info("Sent UpdateResponse to Client");

        outputStream.write(responsePdu.getData().array());
        outputStream.flush();
    }

    private void handleDeleteRequest(OutputStream outputStream, PDU receivedPdu) throws IOException {

        RequestPacket requestPacket = (RequestPacket) receivedPdu.getPduBase();
        ByteBuffer key = ByteBuffer.wrap(requestPacket.getKeyBytes());

        byte[] value = storeKeyValue.remove(key).array();

        logger.info("Received DeleteRequest from Client for Key: {}", new String(requestPacket.getKeyBytes()));
        logger.info("Deleted Key in Map");

        DeleteResponse deleteResponse = new DeleteResponse(value, 0);
        PDU responsePdu = deleteResponse.getPdu();

        logger.info("Sent DeleteResponse to Client");

        outputStream.write(responsePdu.getData().array());
        outputStream.flush();
    }
}
