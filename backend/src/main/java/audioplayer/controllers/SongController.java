package audioplayer.controllers;

import audioplayer.model.dto.CreateSongDto;
import audioplayer.model.dto.SongDto;
import audioplayer.model.entity.Song;
import audioplayer.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/music")
@AllArgsConstructor
@Slf4j
@Tag(name = "Песни", description = "Управление песнями")
public class SongController {
    SongService songService;
    private final Path rootLocation = Paths.get("backend/files");

    @GetMapping("/file/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path file = rootLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = "audio/mpeg"; // или определять динамически по расширению

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Прочитать файлы песен и обновить БД")
    @GetMapping("/parse")
    public void parseAll() {
        songService.parseAll();
    }

    @GetMapping("/change")
    public void attachAll() {
        songService.changeDir();
    }

    @Operation(summary = "Получить список всех песен")
    @GetMapping
    public List<SongDto> getAll() {
        List<Song> songList = songService.findAll();
        return songList.stream().map(SongDto::new).toList();
    }

    @Operation(summary = "Получить песню по ID")
    @GetMapping("/{id}")
    public SongDto getById(@PathVariable Long id) {
        return new SongDto(songService.findById(id));
    }

    @Operation(summary = "Получить песню по ID")
    @GetMapping("/all")
    public List<SongDto> getAllById(@RequestParam List<Long> ids) {
        return songService.findAllById(ids).stream().map(SongDto::new).toList();
    }

    @Operation(summary = "Поиск песен по названию и/или исполнителю")
    @GetMapping("/search")
    public List<SongDto> getByName(@RequestParam(required = false) String title,
                                   @RequestParam(required = false) String artist) {
        return songService.findByCriteria(title, artist);
    }

    @Operation(summary = "Сохранить новую песню")
    @PostMapping
    public ResponseEntity<SongDto> save(@RequestBody @Valid CreateSongDto songDto) {
        log.info("Logging test: {}", songDto);
        return ResponseEntity.ok(songService.create(songDto));
    }

    @Operation(summary = "Обновить песню по ID (полностью)")
    @PutMapping("/{id}")
    public SongDto update(@PathVariable Long id, @RequestBody @Valid CreateSongDto createSongDto) {
        return songService.update(id, createSongDto);
    }

    @Operation(summary = "Частично обновить песню по ID")
    @PatchMapping("/{id}")
    public SongDto patch(@PathVariable Long id, @RequestBody CreateSongDto createSongDto) {
        return songService.patch(id, createSongDto);
    }

    @Operation(summary = "Удалить песню по ID")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        songService.deleteById(id);
    }
}
