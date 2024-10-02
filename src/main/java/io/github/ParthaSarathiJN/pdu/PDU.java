package io.github.ParthaSarathiJN.pdu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.github.ParthaSarathiJN.common.Constants.*;

public class PDU {

    private static final Logger logger = LoggerFactory.getLogger(PDU.class);

    private PDUHeader pduHeader;
    private PDUBase pduBase;
    private PDUPacket implPacket;

    public PDU() {}

    public PDU(PDUHeader pduHeader, PDUBase pduBase, PDUPacket implPacket) {
        this.pduHeader = pduHeader;
        this.pduBase = pduBase;
        this.implPacket = implPacket;
    }

    public ByteBuffer getData() {
        int totalLength = pduHeader.calculateLength() + pduBase.calculateLength() + implPacket.calculateLength();
        ByteBuffer buffer = ByteBuffer.allocate(totalLength);
        buffer.put(pduHeader.getData());
        buffer.put(pduBase.getData());
        buffer.put(implPacket.getData());
        buffer.flip();
        return buffer;
    }

    public PDU createPdu(ByteBuffer buffer) {
        try {

            pduHeader = new PDUHeader();
            pduHeader.setData(buffer);

            if (pduHeader.getOperation() > 0) {
                pduBase = new RequestPacket();
            } else {
                pduBase = new ResponsePacket();
            }

            pduBase.setData(buffer);

            implPacket = createImplPacket(pduHeader.getOperation());
            implPacket.setData(buffer);

            return this;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PDUBase getPduBase() {
        return pduBase;
    }

    public PDUPacket getImplPacket() {
        return implPacket;
    }

    public int getLength() {
        return pduHeader.getLength();
    }

    public byte getOperation() {
        return pduHeader.getOperation();
    }

    public byte[] getUuidByteArr() {
        return pduHeader.getUuidByteArr();
    }

    public UUID getUuid() {
        return pduHeader.getUuid();
    }

    public PDUPacket createImplPacket(byte operation) {
        try {
            return (PDUPacket) pduList.get(operation).getDeclaredConstructor().newInstance();
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Map<Byte, Class> pduList = new HashMap(10);
    static {
        pduList.put(GET_REQ, GetRequest.class);
        pduList.put(INSERT_REQ, InsertRequest.class);
        pduList.put(GET_RESP, GetResponse.class);
        pduList.put(INSERT_RESP, InsertResponse.class);
        pduList.put(UPDATE_REQ, UpdateRequest.class);
        pduList.put(UPDATE_RESP, UpdateResponse.class);
        pduList.put(DELETE_REQ, DeleteRequest.class);
        pduList.put(DELETE_RESP, DeleteResponse.class);
    }

}
