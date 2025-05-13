package audioplayer.controllers;

import audioplayer.model.dto.AlbumDto;
import audioplayer.model.dto.CreateAlbumDto;
import audioplayer.service.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/v1/album")
@AllArgsConstructor
@Slf4j
@Tag(name = "Альбомы", description = "Управление альбомами")
public class AlbumController {
    private final AlbumService albumService;

    @Operation(summary = "Получить список всех альбомов")
    @GetMapping
    public List<AlbumDto> getAll() {
        return albumService.findAll();
    }

    @Operation(summary = "Поиск альбомов по названию")
    @GetMapping("/search")
    public List<AlbumDto> getByName(@RequestParam(required = false) String name) {
        return albumService.findByName(name);
    }

    @Operation(summary = "Создать новый альбом")
    @PostMapping
    public AlbumDto create(@Valid @RequestBody CreateAlbumDto createDto) {
        return albumService.create(createDto);
    }

    @Operation(summary = "Обновить альбом по ID (полностью)")
    @PutMapping("{id}")
    public AlbumDto update(@PathVariable Long id, @Valid @RequestBody CreateAlbumDto createDto) {
        return albumService.patch(id, createDto);
    }

    @Operation(summary = "Частично обновить альбом по ID")
    @PatchMapping("{id}")
    public AlbumDto patch(@PathVariable Long id, @Valid @RequestBody CreateAlbumDto createDto) {
        return albumService.patch(id, createDto);
    }

    @Operation(summary = "Удалить альбом по ID")
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        albumService.delete(id);
    }
}