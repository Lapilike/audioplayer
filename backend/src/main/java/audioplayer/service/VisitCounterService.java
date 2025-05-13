package audioplayer.service;

import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public interface VisitCounterService {
    void increment(String url);

    int getCount(String url);

    Map<String, Integer> getAllCounts();

    void resetAllCounts();
}
