package by.lapil.audioplayer.controllers;

import by.lapil.audioplayer.model.dto.CreateSongDto;
import by.lapil.audioplayer.model.dto.SongDto;
import by.lapil.audioplayer.model.entity.Song;
import by.lapil.audioplayer.service.SongService;
import by.lapil.audioplayer.utils.Genres;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
public class SongController {
    SongService songService;

    @GetMapping
    public List<Song> getAll() {
        return songService.findAll();
    }

    @GetMapping("{id}")
    public Song getById(@PathVariable Long id) {
        return songService.findById(id);
    }

    @GetMapping("/search")
    public List<SongDto> getByName(@RequestParam(required = false) String name,
                                   @RequestParam(required = false) String genre) {
        Genres genreEnum = Genres.valueOf(genre);
        return songService.findByTitleAndGenre(name, genreEnum);
    }

    @PostMapping
    public SongDto saveMusic(@RequestBody CreateSongDto musicDto) {
        return songService.create(musicDto);
    }

    @PutMapping("/{id}")
    public SongDto updateMusic(@PathVariable Long id, @RequestBody CreateSongDto createSongDto) {
        return songService.update(id, createSongDto);
    }

    @DeleteMapping("delete_music/{id}")
    public void deleteById(@PathVariable Long id) {
        songService.deleteById(id);
    }
}
