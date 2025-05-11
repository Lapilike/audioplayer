package by.lapil.audioplayer.service.impl;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import by.lapil.audioplayer.model.Status;
import by.lapil.audioplayer.model.TaskStatus;
import by.lapil.audioplayer.service.LogService;
import by.lapil.audioplayer.utils.LogFileCreator;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {
    private final ConcurrentHashMap<String, TaskStatus> taskStatusMap = new ConcurrentHashMap<>();
    private final LogFileCreator logFileCreator;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public List<String> getLogsByDate(String date, String from, String to) {
        try {
            LocalDate logDate = LocalDate.parse(date, DATE_FORMATTER);

            String filename = String.format("logs/app-%s.log", DATE_FORMATTER.format(logDate));
            Path filePath = Paths.get(filename);

            if (!Files.exists(filePath)) {
                throw new ResponseStatusException(NOT_FOUND,
                        "Лог-файл за дату " + date + " не найден");
            }

            try (Stream<String> lines = Files.lines(filePath)) {
                if (from != null && to != null) {
                    LocalTime fromTime = LocalTime.parse(from, TIME_FORMATTER);
                    LocalTime toTime = LocalTime.parse(to, TIME_FORMATTER);

                    return lines
                            .filter(line -> {
                                try {
                                    String timePart = line.substring(11, 16);
                                    LocalTime logTime = LocalTime.parse(timePart, TIME_FORMATTER);
                                    return !logTime.isBefore(fromTime) && !logTime.isAfter(toTime);
                                } catch (DateTimeParseException e) {
                                    return false;
                                }
                            })
                            .toList();
                } else {
                    return lines.toList();
                }
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(BAD_REQUEST, "Неверный формат запроса: " + e.getMessage());
        }
    }

    @Override
    public String generateLogFile(String from, String to) throws InterruptedException {
        String taskId = UUID.randomUUID().toString();
        TaskStatus logTask = new TaskStatus(Status.PENDING, null);
        taskStatusMap.put(taskId, logTask);
        logFileCreator.createFile(taskId, from, to, taskStatusMap);
        return taskId;
    }

    @Override
    public TaskStatus getStatus(String taskId) {
        return taskStatusMap.getOrDefault(taskId, new TaskStatus(Status.NOT_FOUND, null));
    }
}
