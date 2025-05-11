package by.lapil.audioplayer.service;

import static java.nio.file.StandardOpenOption.CREATE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import by.lapil.audioplayer.model.Status;
import by.lapil.audioplayer.model.TaskStatus;
import by.lapil.audioplayer.service.impl.LogServiceImpl;
import by.lapil.audioplayer.utils.LogFileCreator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

class LogServiceTest {
    @Mock
    private LogFileCreator logFileCreator;

    @InjectMocks
    private LogServiceImpl logService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getLogsByDate_shouldReturnFilteredLines_whenTimeRangeProvided() throws IOException {
        // Arrange
        String date = "2024-05-10";
        Path logPath = Paths.get("logs/app-2024-05-10.log");
        Files.createDirectories(logPath.getParent());

        Files.write(logPath, List.of(
                "2024-05-10 10:15 INFO Something happened",
                "2024-05-10 11:30 WARN Warning issued",
                "2024-05-10 13:45 ERROR Failure"
        ), CREATE);

        // Act
        List<String> result = logService.getLogsByDate(date, "10:00", "12:00");

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).contains("10:15");
        assertThat(result.get(1)).contains("11:30");

        Files.deleteIfExists(logPath);
    }


    @Test
    void getLogsByDate_shouldReturnAllLines_whenNoTimeRangeProvided() throws IOException {
        String date = "2024-05-10";
        Path logPath = Paths.get("logs/app-2024-05-10.log");
        Files.createDirectories(logPath.getParent());

        Files.write(logPath, List.of(
                "2024-05-10 09:00 INFO Startup",
                "2024-05-10 14:00 INFO Running"
        ), CREATE);

        List<String> result = logService.getLogsByDate(date, null, null);

        assertThat(result).hasSize(2);
        assertThat(result.get(1)).contains("14:00");

        Files.deleteIfExists(logPath);
    }

    @Test
    void getLogsByDate_shouldThrowNotFound_whenFileMissing() {
        String date = "2099-01-01"; // Future date to avoid file collision

        assertThatThrownBy(() -> logService.getLogsByDate(date, null, null))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void getLogsByDate_shouldThrowBadRequest_whenInvalidDateFormat() {
        assertThatThrownBy(() -> logService.getLogsByDate("2024_05_10", null, null))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Неверный формат запроса");
    }

    @Test
    void getLogsByDate_shouldThrowBadRequest_whenInvalidTimeFormat() throws IOException {
        String date = "2024-05-10";
        Path logPath = Paths.get("logs/app-2024-05-10.log");
        Files.createDirectories(logPath.getParent());
        Files.write(logPath, List.of("2024-05-10 10:15 INFO Something happened"), CREATE);

        assertThatThrownBy(() -> logService.getLogsByDate(date, "10-00", "11-00"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Неверный формат запроса");

        Files.deleteIfExists(logPath);
    }

    @Test
    void getLogsByDate_shouldIgnoreLineWithInvalidTimeFormat() throws IOException {
        String date = "2024-05-10";
        Path logPath = Paths.get("logs/app-2024-05-10.log");
        Files.createDirectories(logPath.getParent());

        Files.write(logPath, List.of(
                "2024-05-10 10:15 INFO Valid line",
                "Invalid line without time",
                "2024-05-10 XX:XX ERROR Bad time"
        ), CREATE);

        List<String> result = logService.getLogsByDate(date, "09:00", "11:00");

        // Ожидаем, что валидна только одна строка
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).contains("Valid line");

        Files.deleteIfExists(logPath);
    }

    @Test
    void getLogsByDate_shouldExcludeLineOutsideTimeRange() throws IOException {
        String date = "2024-05-10";
        Path logPath = Paths.get("logs/app-2024-05-10.log");
        Files.createDirectories(logPath.getParent());

        Files.write(logPath, List.of(
                "2024-05-10 08:00 INFO Too early",
                "2024-05-10 09:30 INFO In range"
        ), CREATE);

        List<String> result = logService.getLogsByDate(date, "09:00", "10:00");

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).contains("09:30");

        Files.deleteIfExists(logPath);
    }

    @Test
    void generateLogFile_shouldCreateTaskAndCallCreator() throws InterruptedException {
        String from = "09:00";
        String to = "10:00";

        String taskId = logService.generateLogFile(from, to);

        assertThat(taskId).isNotNull();

        TaskStatus status = logService.getStatus(taskId);
        assertThat(status).isNotNull();
        assertThat(status.getStatus()).isEqualTo(Status.PENDING);
        assertThat(status.getFilePath()).isNull();

        verify(logFileCreator).createFile(eq(taskId), eq(from), eq(to), any());
    }

    @Test
    void getStatus_shouldReturnNotFoundIfUnknownTask() {
        String unknownTaskId = UUID.randomUUID().toString();

        TaskStatus status = logService.getStatus(unknownTaskId);

        assertThat(status).isNotNull();
        assertThat(status.getStatus()).isEqualTo(Status.NOT_FOUND);
        assertThat(status.getFilePath()).isNull();
    }
}