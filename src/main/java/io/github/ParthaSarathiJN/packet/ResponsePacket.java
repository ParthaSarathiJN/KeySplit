//package io.github.ParthaSarathiJN.packet;
//
//public abstract class ResponsePacket extends BasePacket {
//
//    private int status;
//
//    public ResponsePacket(byte operation, int status) {
//        super(operation);
//        this.status = status;
//    }
//
//    @Override
//    public void fromByteArray(byte[] data) {
//        super.fromByteArray(data);
//        setStatus(buffer.get());
//    }
//
//    public int getStatus() {
//        return status;
//    }
//
//    public void setStatus(int status) {
//        this.status = status;
//    }
//
//    @Override
//    protected int calculateLength() {
//        return super.calculateLength() + 4;
//    }
//}
