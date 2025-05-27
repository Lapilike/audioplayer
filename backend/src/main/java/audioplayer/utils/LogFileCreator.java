package audioplayer.utils;

import audioplayer.exception.NotFoundException;
import audioplayer.model.Status;
import audioplayer.model.TaskStatus;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LogFileCreator {
    @Async
    public void createFile(String taskId, String from, String to,
                           ConcurrentMap<String, TaskStatus> taskStatusMap) throws InterruptedException {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            Path secureDir = Paths.get(System.getProperty("java.io.tmpdir"));
            Path tempLogFile = Files.createTempFile(secureDir, "log-temp-" + taskId, ".log");
            Files.writeString(tempLogFile, "Logs...");
            LocalDate fromDate = LocalDate.parse(from, dateFormatter);
            LocalDate toDate = LocalDate.parse(to, dateFormatter);

            if (fromDate.isAfter(toDate)) throw new IllegalArgumentException("Incorrect date range");

            while (!fromDate.isAfter(toDate)) {
                String filename = String.format("backend/logs/app-%s.log", dateFormatter.format(fromDate));
                Path logFile = Paths.get(filename);
                if (!Files.exists(logFile)) {
                    fromDate = fromDate.plusDays(1);
                    continue;
                }
                String line = String.join("\n", Files.readAllLines(logFile));
                Files.writeString(tempLogFile, "\nLog date: " + fromDate + "\n", StandardOpenOption.APPEND);
                Files.writeString(tempLogFile, line, StandardOpenOption.APPEND);
                fromDate = fromDate.plusDays(1);
            }

            if (Files.size(tempLogFile) == 7) {
                throw new NotFoundException("No logs found");
            }

            Thread.sleep(10000);

            taskStatusMap.put(taskId, new TaskStatus(Status.DONE, tempLogFile));
        } catch (NotFoundException e) {
            taskStatusMap.put(taskId, new TaskStatus(Status.NOT_FOUND, null));
        } catch (InterruptedException e) {
            throw e;
        } catch (Exception e) {
            taskStatusMap.put(taskId, new TaskStatus(Status.ERROR, null));
        }
    }
}
