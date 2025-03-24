package by.lapil.audioplayer.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class Cache<T> extends LinkedHashMap<String, T> {
    private static final Integer CACHE_SIZE = 3;
    private final Map<Long, Set<String>> keyTracker = new HashMap<>();

    public Cache() {
        super(CACHE_SIZE, 0.75f, true);
    }

    public Set<String> getKeys(Long id) {
        return keyTracker.getOrDefault(id, null);
    }

    public void update(Long id, T value) {
        Set<String> cacheKeys = keyTracker.getOrDefault(id, null);
        if (cacheKeys != null) {
            for (String cacheKey : cacheKeys) {
                super.put(cacheKey, value);
            }
        }
    }

    public void trackKey(Long id, String key) {
        keyTracker.computeIfAbsent(id, k -> new HashSet<>()).add(key);
    }

    public void remove(Long id) {
        Set<String> keys = keyTracker.remove(id);
        for (String key : keys) {
            super.remove(key);
        }
    }

    @Override
    public void clear() {
        keyTracker.clear();
        super.clear();
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<String, T> eldest) {
        return size() > CACHE_SIZE; // Удалять самый старый элемент, если превышен лимит
    }
}
