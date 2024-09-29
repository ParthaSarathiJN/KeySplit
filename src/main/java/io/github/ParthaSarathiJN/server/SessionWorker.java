package io.github.ParthaSarathiJN.server;

import io.github.ParthaSarathiJN.pdu.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import static io.github.ParthaSarathiJN.common.Constants.GET_REQ;
import static io.github.ParthaSarathiJN.common.Constants.INSERT_REQ;

public class SessionWorker implements Runnable {

    private final Socket clientSocket;
    private final StoreKeyValue storeKeyValue;

    public SessionWorker(Socket clientSocket, StoreKeyValue storeKeyValue) {
        this.clientSocket = clientSocket;
        this.storeKeyValue = storeKeyValue;
    }

    @Override
    public void run() {
        try {
            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();

            // Read incoming PDU, deserialize, and process it
            byte[] pduBytes = new byte[1024]; // Buffer size for incoming data
            in.read(pduBytes);
            ByteBuffer buffer = ByteBuffer.wrap(pduBytes);

            // Deserialize and create PDU based on packet type
            PDU receivedPdu = PDU.createPdu(buffer);

            // Process based on the operation
            switch (receivedPdu.getOperation()) {
                case GET_REQ:
                    handleGetRequest(out, receivedPdu);
                    break;
                case INSERT_REQ:
                    handleInsertRequest(out, receivedPdu);
                    break;
                // Add more case blocks for other operations (UPDATE_REQ, DELETE_REQ)
                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleGetRequest(OutputStream out, PDU receivedPdu) throws IOException {
        GetRequest getRequest = (GetRequest) receivedPdu.getImplPacket();
        ByteBuffer key = getRequest.getKeyBuffer();

        byte[] value = storeKeyValue.get(key);

        // Build and send response
        GetResponse getResponse = new GetResponse(value, 0);
        PDU responsePdu = getResponse.getPDU();
        out.write(responsePdu.getData().array());
        out.flush();
    }

    private void handleInsertRequest(OutputStream out, PDU receivedPdu) throws IOException {
        InsertRequest insertRequest = (InsertRequest) receivedPdu.getImplPacket();
        ByteBuffer key = insertRequest.getKeyBuffer();
        byte[] value = insertRequest.getValueBytes();

        storeKeyValue.put(key, value);

        // Send acknowledgment or response (depends on how you design it)
        InsertResponse insertResponse = new InsertResponse(0);
        PDU responsePdu = insertResponse.getPDU();
        out.write(responsePdu.getData().array());
        out.flush();
    }
}
