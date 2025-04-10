package by.lapil.audioplayer.controllers;

import by.lapil.audioplayer.model.dto.CreateSongDto;
import by.lapil.audioplayer.model.dto.SongDto;
import by.lapil.audioplayer.model.entity.Song;
import by.lapil.audioplayer.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public SongDto patch(@PathVariable Long id, @RequestBody @Valid CreateSongDto createSongDto) {
        return songService.patch(id, createSongDto);
    }

    @Operation(summary = "Удалить песню по ID")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        songService.deleteById(id);
    }
}
