package by.lapil.audioplayer.controllers;

import by.lapil.audioplayer.model.dto.CreateSongDto;
import by.lapil.audioplayer.model.dto.SongDto;
import by.lapil.audioplayer.model.entity.Song;
import by.lapil.audioplayer.service.SongService;
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
@RequestMapping("/api/v1/music")
@AllArgsConstructor
public class SongController {
    SongService songService;

    @GetMapping
    public List<SongDto> getAll() {
        List<Song> songList = songService.findAll();
        return songList.stream().map(SongDto::new).toList();
    }

    @GetMapping("/{id}")
    public SongDto getById(@PathVariable Long id) {
        return new SongDto(songService.findById(id));
    }

    @GetMapping("/search")
    public List<SongDto> getByName(@RequestParam(required = false) String title,
                                   @RequestParam(required = false) String artist) {
        return songService.findByCriteria(title, artist);
    }

    @PostMapping
    public SongDto save(@RequestBody CreateSongDto musicDto) {
        return songService.create(musicDto);
    }

    @PutMapping("/{id}")
    public SongDto update(@PathVariable Long id, @RequestBody CreateSongDto createSongDto) {
        return songService.update(id, createSongDto);
    }

    @PatchMapping("/{id}")
    public SongDto patch(@PathVariable Long id, @RequestBody CreateSongDto createSongDto) {
        return songService.patch(id, createSongDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        songService.deleteById(id);
    }
}
