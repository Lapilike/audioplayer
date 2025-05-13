package audioplayer.model;

import java.nio.file.Path;
import lombok.Data;

@Data
public class TaskStatus {
    private Status status;
    private Path filePath;

    public TaskStatus(Status status, Path filePath) {
        this.status = status;
        this.filePath = filePath;
    }
}
