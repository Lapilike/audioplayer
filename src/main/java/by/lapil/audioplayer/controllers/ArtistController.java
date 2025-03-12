package by.lapil.audioplayer.controllers;

import by.lapil.audioplayer.model.dto.ArtistDto;
import by.lapil.audioplayer.model.dto.CreateArtistDto;
import by.lapil.audioplayer.model.entity.Artist;
import by.lapil.audioplayer.service.ArtistService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public List<Artist> getAll() {
        return artistService.findAll();
    }

    @GetMapping("{id}")
    public Artist getById(@RequestParam Long id) {
        return artistService.findById(id);
    }

    @PostMapping
    public ArtistDto create(@RequestBody CreateArtistDto createArtistDto) {
        return artistService.create(createArtistDto);
    }
}
