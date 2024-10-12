package io.github.ParthaSarathiJN.server;

import io.github.ParthaSarathiJN.utility.ChannelResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KeyValueStoreServer {

    private static final Logger logger = LoggerFactory.getLogger(KeyValueStoreServer.class);

    private final int applicationPort;
    private final ExecutorService threadPool;
    private final StoreKeyValue storeKeyValue;
    private Selector selector;
    private ServerSocketChannel serverChannel;
    static Queue<ChannelResponse> messageQueue = new ArrayBlockingQueue<>(1000);

    public KeyValueStoreServer(int applicationPort, int threadPoolSize) {
        this.threadPool = Executors.newFixedThreadPool(threadPoolSize);
        this.storeKeyValue = new StoreKeyValue();
        this.applicationPort = applicationPort;
    }

    public void startServer() {

        try {

            selector = Selector.open();

            serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(applicationPort));
            serverChannel.configureBlocking(false);

            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            logger.info("Starting server socket on port: {}", applicationPort);

            while (true) {
                selector.select(1000);
                processReadyKeys(selector.selectedKeys().iterator());
                processPendingResponses();
            }

        } catch (IOException ioServerSocketException) {
            logger.error("IoServerSocketException in ServerSocket!");
        }
    }

    private void processReadyKeys(Iterator<SelectionKey> selectionKeyIterator) {

        while (selectionKeyIterator.hasNext()) {

            SelectionKey selectedKey = selectionKeyIterator.next();
            selectionKeyIterator.remove();

            if (selectedKey.isAcceptable()) {
                acceptConnection(selectedKey);
            } else if (selectedKey.isReadable()) {
                readFromChannel(selectedKey);
            } else if (selectedKey.isWritable()) {
                writeToChannel(selectedKey);
            }
        }
    }

    private void acceptConnection(SelectionKey selectedKey) {

        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectedKey.channel();
            SocketChannel clientChannel = serverSocketChannel.accept();

            clientChannel.configureBlocking(false);
            clientChannel.register(selector, SelectionKey.OP_READ);

            logger.info("New client connected: {}", clientChannel.getRemoteAddress());

        } catch (IOException ioException) {
            logger.error("IOException in accepting client channel!");
        }
    }

    private void readFromChannel(SelectionKey selectedKey) {

        SocketChannel clientChannel = (SocketChannel) selectedKey.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        int PduLengthSize = 4;

        try {

            int bytesRead = clientChannel.read(buffer);

            if (bytesRead == -1) {
                clientChannel.close();
                logger.info("Client disconnected: {}", clientChannel.getRemoteAddress());
                return;
            } else if (bytesRead == 0) {
                return;
            }

        } catch (IOException ioException) {
            logger.error("IOException in reading from client channel! {} {}", ioException.toString(), ioException.getMessage());
        }

        buffer.flip();

        while (buffer.remaining() >= PduLengthSize) {

            buffer.mark();

            int messageLength = buffer.getInt();

            if (messageLength <= 0) {
                logger.error("Invalid message length received from client: {}", messageLength);
            }

            buffer.reset();

            // TODO Need to check for partial messages not fully present in channel, extra long message not able to be
            //  written to buffer in one go
            if (buffer.remaining() >= messageLength) {
                ByteBuffer messageBuffer = ByteBuffer.allocate(messageLength);
                buffer.get(messageBuffer.array(), 0, messageLength);

                threadPool.execute(new AsyncSessionWorker(messageBuffer, storeKeyValue, clientChannel));
            } else {
                // Partial message; reset buffer position and break out to wait for more data
                buffer.reset(); // Rewind to the mark position
                break;
            }
        }
    }

    private void writeToChannel(SelectionKey selectedKey) {
        SocketChannel clientChannel = (SocketChannel) selectedKey.channel();
        ByteBuffer responseBuffer = (ByteBuffer) selectedKey.attachment();

        try {
            clientChannel.write(responseBuffer);
        } catch (IOException ioException) {
            logger.error("IOException in writing to client channel!");
        }

        if (!responseBuffer.hasRemaining()) {
            selectedKey.interestOps(SelectionKey.OP_READ);
            selectedKey.attach(null);
        }
    }

    private void processPendingResponses() {

        synchronized (messageQueue) {

//            logger.error("messageQueue size: {}", messageQueue.size());
            while (!messageQueue.isEmpty()) {

                ChannelResponse channelResponse = messageQueue.remove();

                if (channelResponse != null) {

                    SocketChannel clientChannel = channelResponse.getSocketChannel();
                    ByteBuffer responseBuffer = channelResponse.getResponseBuffer();
                    SelectionKey selectedKey = clientChannel.keyFor(selector);

                    if (selectedKey != null && selectedKey.isValid()) {
                        selectedKey.interestOps(SelectionKey.OP_WRITE);
                        selectedKey.attach(responseBuffer);
                    }
                }
            }
        }
    }


    public void stopServer() {

    }
}
