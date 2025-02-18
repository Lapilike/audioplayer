package by.lapil.audioplayer.controllers;

import by.lapil.audioplayer.model.Music;
import by.lapil.audioplayer.service.impl.InMemoryMusicService;
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

    @GetMapping("/get")
    public Music getByIdRequest(@RequestParam(required = false, defaultValue = "0") int id) {
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
