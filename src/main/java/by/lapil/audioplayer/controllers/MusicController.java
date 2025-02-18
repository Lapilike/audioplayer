package by.lapil.audioplayer.controllers;

import by.lapil.audioplayer.model.Music;
import by.lapil.audioplayer.service.impl.InMemoryMusicService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/music")
@AllArgsConstructor
public class MusicController {

    InMemoryMusicService musicService;

    @GetMapping
    public List<Music> getAll() {
        return musicService.findAll();
    }

    @GetMapping("/get/{id}")
    public Music getById(@PathVariable int id) {
        return musicService.findById(id);
    }

    @PostMapping("save_music")
    public Music saveMusic(@RequestBody Music music) {
        return musicService.save(music);
    }

    @PutMapping("update_music")
    public Music updateMusic(@RequestBody Music music) {
        return musicService.update(music);
    }

    @DeleteMapping("delete_music/{id}")
    public void deleteById(@PathVariable int id) {
        musicService.deleteById(id);
    }
}
