package io.github.ParthaSarathiJN.server;

import io.github.ParthaSarathiJN.pdu.*;
import io.github.ParthaSarathiJN.utility.ChannelResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static io.github.ParthaSarathiJN.common.Constants.*;
import static io.github.ParthaSarathiJN.server.KeyValueStoreServer.messageQueue;

public class AsyncSessionWorker implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(AsyncSessionWorker.class);

    private final ByteBuffer messageBuffer;
    private final StoreKeyValue storeKeyValue;
    private final SocketChannel socketChannel;

    public AsyncSessionWorker(ByteBuffer messageBuffer, StoreKeyValue storeKeyValue, SocketChannel socketChannel) {
        this.messageBuffer = messageBuffer;
        this.storeKeyValue = storeKeyValue;
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        try {

                PDU pdu = new PDU();

                // Deserialize and create PDU based on packet type
                PDU receivedPdu = pdu.createPdu(messageBuffer);

                // Process based on the operation
                switch (receivedPdu.getOperation()) {
                    case GET_REQ:
                        handleGetRequest(socketChannel, receivedPdu);
                        break;
                    case INSERT_REQ:
                        handleInsertRequest(socketChannel, receivedPdu);
                        break;
                    case UPDATE_REQ:
                        handleUpdateRequest(socketChannel, receivedPdu);
                        break;
                    case DELETE_REQ:
                        handleDeleteRequest(socketChannel, receivedPdu);
                        break;
                    default:
                        break;
                }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleGetRequest(SocketChannel socketChannel, PDU receivedPdu) throws IOException {

        RequestPacket requestPacket = (RequestPacket) receivedPdu.getPduBase();
        ByteBuffer key = ByteBuffer.wrap(requestPacket.getKeyBytes());

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

        insertResponseToQueue(socketChannel, responsePdu.getData());
    }

    private void handleInsertRequest(SocketChannel socketChannel, PDU receivedPdu) throws IOException {

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

        insertResponseToQueue(socketChannel, responsePdu.getData());
    }

    private void handleUpdateRequest(SocketChannel socketChannel, PDU receivedPdu) throws IOException {

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

        insertResponseToQueue(socketChannel, responsePdu.getData());
    }

    private void handleDeleteRequest(SocketChannel socketChannel, PDU receivedPdu) throws IOException {

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

        insertResponseToQueue(socketChannel, responsePdu.getData());
    }

    public void insertResponseToQueue(SocketChannel socketChannel, ByteBuffer responseBuffer) {
        ChannelResponse channelResponse = new ChannelResponse(socketChannel, responseBuffer);
        messageQueue.add(channelResponse);
    }
}
