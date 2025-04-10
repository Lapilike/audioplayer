package by.lapil.audioplayer.controllers;

import by.lapil.audioplayer.model.dto.ArtistDto;
import by.lapil.audioplayer.model.dto.CreateArtistDto;
import by.lapil.audioplayer.model.dto.UpdateArtistDto;
import by.lapil.audioplayer.model.entity.Artist;
import by.lapil.audioplayer.service.ArtistService;
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
@RequestMapping("/api/v1/artist")
@AllArgsConstructor
@Slf4j
@Tag(name = "Артисты", description = "Управление артистами")
public class ArtistController {
    ArtistService artistService;

    @Operation(summary = "Получить список всех исполнителей")
    @GetMapping
    public List<ArtistDto> getAll() {
        List<Artist> artistList = artistService.findAll();
        return artistList.stream().map(ArtistDto::new).toList();
    }

    @Operation(summary = "Получить исполнителя по ID")
    @GetMapping("/{id}")
    public ArtistDto getById(@PathVariable Long id) {
        return new ArtistDto(artistService.findById(id));
    }

    @Operation(summary = "Поиск исполнителей по имени")
    @GetMapping("/search")
    public List<ArtistDto> getById(@RequestParam String name) {
        return artistService.findByName(name);
    }

    @Operation(summary = "Создать нового исполнителя")
    @PostMapping
    public ArtistDto create(@Valid @RequestBody CreateArtistDto createArtistDto) {
        return artistService.create(createArtistDto);
    }

    @Operation(summary = "Обновить исполнителя по ID (полностью)")
    @PutMapping("/{id}")
    public ArtistDto update(@PathVariable Long id, @Valid @RequestBody UpdateArtistDto updateArtistDto) {
        return artistService.update(id, updateArtistDto);
    }

    @Operation(summary = "Частично обновить исполнителя по ID")
    @PatchMapping("/{id}")
    public ArtistDto patch(@PathVariable Long id, @Valid @RequestBody UpdateArtistDto updateArtistDto) {
        return artistService.patch(id, updateArtistDto);
    }

    @Operation(summary = "Удалить исполнителя по ID")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        artistService.deleteById(id);
    }
}
