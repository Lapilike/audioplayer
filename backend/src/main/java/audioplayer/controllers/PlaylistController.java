package audioplayer.controllers;

import audioplayer.model.dto.CreatePlaylistDto;
import audioplayer.model.dto.PlaylistDto;
import audioplayer.service.PlaylistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/api/v1/playlist")
@AllArgsConstructor
@Slf4j
@Tag(name = "Плейлист", description = "Управление альбомами")
public class PlaylistController {
    private final PlaylistService playlistService;

    @Operation(summary = "Получить список всех альбомов")
    @GetMapping
    public List<PlaylistDto> getAll() {
        return playlistService.findAll();
    }

    @Operation(summary = "Получить список альбома по id")
    @GetMapping("/{id}")
    public PlaylistDto getById(@PathVariable Long id) {
        return playlistService.findById(id);
    }

    @Operation(summary = "Поиск альбомов по названию")
    @GetMapping("/search")
    public List<PlaylistDto> getByName(@RequestParam(required = false) String name) {
        return playlistService.findByName(name);
    }

    @Operation(summary = "Создать новый альбом")
    @PostMapping
    public PlaylistDto create(@Valid @RequestBody CreatePlaylistDto createDto) {
        return playlistService.create(createDto);
    }

    @Operation(summary = "Обновить альбом по ID (полностью)")
    @PutMapping("/{id}")
    public PlaylistDto update(@PathVariable Long id, @Valid @RequestBody CreatePlaylistDto createDto) {
        return playlistService.patch(id, createDto);
    }

    @Operation(summary = "Частично обновить альбом по ID")
    @PatchMapping("/{id}")
    public PlaylistDto patch(@PathVariable Long id,
                             @Validated(CreatePlaylistDto.Groups.OnPatch.class)
                             @RequestBody CreatePlaylistDto createDto) {
        return playlistService.patch(id, createDto);
    }

    @Operation(summary = "Удалить альбом по ID")
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        playlistService.delete(id);
    }
}