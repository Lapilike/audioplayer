package audioplayer.controllers;

import audioplayer.exception.NotFoundException;
import audioplayer.model.Status;
import audioplayer.model.TaskStatus;
import audioplayer.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/log")
@AllArgsConstructor
@Tag(name = "Логирование", description = "Эндпоинты для получения лог-файлов")
public class LogController {
    private final LogService logService;

    @Operation(summary = "Получить лог по определённой дате")
    @GetMapping("/{date}")
    public ResponseEntity<List<String>> getLogsByDate(@PathVariable String date,
                                                  @RequestParam(required = false) String from,
                                                  @RequestParam(required = false) String to) {
        return ResponseEntity.ok(logService.getLogsByDate(date, from, to));
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateLog(@RequestParam(required = false) String from,
                                              @RequestParam(required = false) String to) {
        String taskId;
        taskId = logService.generateLogFile(from, to);
        return ResponseEntity.ok(taskId);
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<Map<String, Object>> getLogStatus(@PathVariable String id) {
        TaskStatus status = logService.getStatus(id);
        Map<String, Object> body = new HashMap<>();
        body.put("id", id);
        body.put("status", status.getStatus());
        if (status.getStatus() == Status.NOT_FOUND) throw new NotFoundException("Logs not found");
        return ResponseEntity.ok().body(body);
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable String id) {
        TaskStatus status = logService.getStatus(id);
        if (status.getStatus() != Status.DONE || status.getFilePath() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Path file = status.getFilePath();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + file.getFileName().toString())
                .contentType(MediaType.TEXT_PLAIN)
                .body(new FileSystemResource(file));
    }
}
