package io.github.ParthaSarathiJN.server;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

public class StoreKeyValue {

    private final ConcurrentHashMap<ByteBuffer, ByteBuffer> storeKVMap;

    public StoreKeyValue() {
        storeKVMap = new ConcurrentHashMap<>();
    }

    // Get value for a given key
    public ByteBuffer get(ByteBuffer key) {
        return storeKVMap.get(key);
    }

    // Add new key-value pair (insert)
    public ByteBuffer put(ByteBuffer key, ByteBuffer value) {
        return storeKVMap.put(key, value);
    }

    // Update value for a given key (same as put)
//    public ByteBuffer update(ByteBuffer key, ByteBuffer value) {
//        return storeKVMap.put(key, value);
//    }

    // Remove a key-value pair
    public ByteBuffer remove(ByteBuffer key) {
        return storeKVMap.remove(key);
    }
}

