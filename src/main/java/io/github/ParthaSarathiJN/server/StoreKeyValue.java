package io.github.ParthaSarathiJN.server;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

public class StoreKeyValue {

    private final ConcurrentHashMap<ByteBuffer, byte[]> storeKVMap;

    public StoreKeyValue() {
        storeKVMap = new ConcurrentHashMap<>();
    }

    // Add new key-value pair (insert)
    public byte[] put(ByteBuffer key, byte[] value) {
        return storeKVMap.put(key, value);
    }

    // Get value for a given key
    public byte[] get(ByteBuffer key) {
        return storeKVMap.get(key);
    }

    // Remove a key-value pair
    public byte[] remove(ByteBuffer key) {
        return storeKVMap.remove(key);
    }

    // Update value for a given key (same as put)
    public byte[] update(ByteBuffer key, byte[] value) {
        return storeKVMap.put(key, value);
    }
}

