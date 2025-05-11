package by.lapil.audioplayer.service.impl;

import by.lapil.audioplayer.service.VisitCounterService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class VisitCounterServiceImpl implements VisitCounterService {
    private final Map<String, AtomicInteger> counters = new ConcurrentHashMap<>();

    @Override
    public void increment(String url) {
        counters.computeIfAbsent(url, k -> new AtomicInteger(0)).incrementAndGet();
    }

    @Override
    public int getCount(String url) {
        return counters.getOrDefault(url, new AtomicInteger(0)).get();
    }

    @Override
    public Map<String, Integer> getAllCounts() {
        return counters.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
    }

    @Override
    public void resetAllCounts() {
        counters.clear();
    }
}
