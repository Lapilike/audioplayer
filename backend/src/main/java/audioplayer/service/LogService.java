package audioplayer.service;

import audioplayer.model.TaskStatus;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface LogService {
    List<String> getLogsByDate(String date, String from, String to);

    String generateLogFile(String from, String to) throws InterruptedException;

    TaskStatus getStatus(String taskId);
}