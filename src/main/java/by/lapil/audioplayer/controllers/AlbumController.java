package by.lapil.audioplayer.controllers;

import by.lapil.audioplayer.model.dto.AlbumDto;
import by.lapil.audioplayer.model.dto.CreateAlbumDto;
import by.lapil.audioplayer.service.AlbumService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/album")
@AllArgsConstructor
public class AlbumController {
    private final AlbumService albumService;

    @GetMapping
    public List<AlbumDto> getAll() {
        return albumService.findAll();
    }

    @GetMapping("/search")
    public List<AlbumDto> getByName(@RequestParam(required = false) String name) {
        return albumService.findByName(name);
    }

    @PostMapping
    public AlbumDto create(@RequestBody CreateAlbumDto createDto) {
        return albumService.create(createDto);
    }

    @PatchMapping("{id}")
    public AlbumDto update(@RequestBody CreateAlbumDto createAlbumDto, @PathVariable Long id) {
        return albumService.update(id, createAlbumDto);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        albumService.delete(id);
    }
}
