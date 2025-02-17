package by.lapil.audioplayer.controllers;

import by.lapil.audioplayer.model.Music;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/music")
public class MusicController {

    @GetMapping
    public List<Music> findAllMusic() {
        return List.of(
                Music.builder().name("Tenebre Rosso Sangue").artist("KEYGEN CHURCH").album("ULTRAKILL").id(0).build(),
                Music.builder().name("Bury The Light").artist("Casey Edwards").album("Devil May Cry 5").id(1).build(),
                Music.builder().name("The Rebel Path").artist("P.T. Adamczyk").album("Cyberpunk 2077").id(2).build()
        );
    }
}
