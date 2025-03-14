package by.lapil.audioplayer.controllers;

import by.lapil.audioplayer.model.dto.ArtistDto;
import by.lapil.audioplayer.model.dto.CreateArtistDto;
import by.lapil.audioplayer.model.dto.UpdateArtistDto;
import by.lapil.audioplayer.model.entity.Artist;
import by.lapil.audioplayer.service.ArtistService;
import java.util.List;
import lombok.AllArgsConstructor;
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
public class ArtistController {
    ArtistService artistService;

    @GetMapping
    public List<ArtistDto> getAll() {
        List<Artist> artistList = artistService.findAll();
        return artistList.stream().map(ArtistDto::new).toList();
    }

    @GetMapping("/{id}")
    public ArtistDto getById(@PathVariable Long id) {
        return new ArtistDto(artistService.findById(id));
    }

    @GetMapping("/search")
    public List<ArtistDto> getById(@RequestParam() String name) {
        return artistService.findByName(name);
    }

    @PostMapping
    public ArtistDto create(@RequestBody CreateArtistDto createArtistDto) {
        return artistService.create(createArtistDto);
    }

    @PutMapping("/{id}")
    public ArtistDto update(@PathVariable Long id, @RequestBody UpdateArtistDto updateArtistDto) {
        return artistService.update(id, updateArtistDto);
    }

    @PatchMapping("/{id}")
    public ArtistDto patch(@PathVariable Long id, @RequestBody UpdateArtistDto updateArtistDto) {
        return artistService.patch(id, updateArtistDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        artistService.deleteById(id);
    }
}
