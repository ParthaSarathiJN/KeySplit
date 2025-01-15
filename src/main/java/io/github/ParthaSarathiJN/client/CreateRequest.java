package io.github.ParthaSarathiJN.client;

import io.github.ParthaSarathiJN.pdu.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class CreateRequest {

    private static final Logger logger = LoggerFactory.getLogger(CreateRequest.class);

    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public CreateRequest(DataInputStream inputStream, DataOutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public void sendGetRequest(BufferedReader reader) throws IOException {

        logger.info("Enter GET Request Key: ");
        String key = reader.readLine();

        GetRequest getRequest = new GetRequest(key.getBytes());
        PDU requestPdu = getRequest.getPdu();
        outputStream.write(requestPdu.getData().array());
        outputStream.flush();
        logger.info("Sent GetRequest to Server with Key: {}", key);

        byte[] pduBytes = new byte[1024];
        inputStream.read(pduBytes);
        ByteBuffer buffer = ByteBuffer.wrap(pduBytes);

        PDU pdu = new PDU();

        PDU receivedResponsePdu = pdu.createPdu(buffer);
        GetResponse getResponse = (GetResponse) receivedResponsePdu.getImplPacket();

        logger.info("Received GetResponse from Server with Value: {}", new String(getResponse.getValueBytes()));
    }

    public void sendInsertRequest(BufferedReader reader) throws IOException {

        logger.info("Enter INSERT Request Key: ");
        String key = reader.readLine();
        logger.info("Enter INSERT Request Value: ");
        String value = reader.readLine();

        InsertRequest insertRequest = new InsertRequest(key.getBytes(), value.getBytes());
        PDU requestPdu = insertRequest.getPdu();
        outputStream.write(requestPdu.getData().array());
        outputStream.flush();
        logger.info("Sent InsertRequest to Server with Key: {} & Value: {}", key, value);

        byte[] pduBytes = new byte[1024];
        inputStream.read(pduBytes);
        ByteBuffer buffer = ByteBuffer.wrap(pduBytes);

        PDU pdu = new PDU();

        PDU receivedResponsePdu = pdu.createPdu(buffer);
        ResponsePacket responsePacket = (ResponsePacket) receivedResponsePdu.getPduBase();

        logger.info("Received InsertResponse from Server with Status: {}", responsePacket.getStatus());
    }

    public void sendUpdateRequest(BufferedReader reader) throws IOException {

        logger.info("Enter UPDATE Request Key: ");
        String key = reader.readLine();
        logger.info("Enter UPDATE Request Value: ");
        String value = reader.readLine();

        UpdateRequest updateRequest = new UpdateRequest(key.getBytes(), value.getBytes());
        PDU requestPdu = updateRequest.getPdu();
        outputStream.write(requestPdu.getData().array());
        outputStream.flush();
        logger.info("Sent UpdateRequest to Server with Key: {} & Value: {}", key, value);

        byte[] pduBytes = new byte[1024];
        inputStream.read(pduBytes);
        ByteBuffer buffer = ByteBuffer.wrap(pduBytes);

        PDU pdu = new PDU();

        PDU receivedResponsePdu = pdu.createPdu(buffer);
        ResponsePacket responsePacket = (ResponsePacket) receivedResponsePdu.getPduBase();

        logger.info("Received UpdateResponse from Server with Status: {}", responsePacket.getStatus());

    }

    public void sendDeleteRequest(BufferedReader reader) throws IOException {

        logger.info("Enter DELETE Request Key: ");
        String key = reader.readLine();

        DeleteRequest deleteRequest = new DeleteRequest(key.getBytes());
        PDU requestPdu = deleteRequest.getPdu();
        outputStream.write(requestPdu.getData().array());
        outputStream.flush();
        logger.info("Sent DeleteRequest to Server with Key: {}", key);

        byte[] pduBytes = new byte[1024];
        inputStream.read(pduBytes);
        ByteBuffer buffer = ByteBuffer.wrap(pduBytes);

        PDU pdu = new PDU();

        PDU receivedResponsePdu = pdu.createPdu(buffer);
        ResponsePacket responsePacket = (ResponsePacket) receivedResponsePdu.getPduBase();

        logger.info("Received DeleteResponse from Server with Status: {}", responsePacket.getStatus());
    }

}
