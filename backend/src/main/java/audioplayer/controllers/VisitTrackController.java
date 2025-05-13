package audioplayer.controllers;

import audioplayer.service.VisitCounterService;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/track")
@AllArgsConstructor
public class VisitTrackController {
    private final VisitCounterService visitCounterService;

    @GetMapping(params = "uri")
    public ResponseEntity<Map<String, Integer>> getCount(@RequestParam String uri) {
        return ResponseEntity.ok().body(Map.of(uri, visitCounterService.getCount(uri)));
    }

    @GetMapping(params = "!uri")
    public ResponseEntity<Map<String, Integer>> getAllCount() {
        return ResponseEntity.ok(visitCounterService.getAllCounts());
    }

    @DeleteMapping
    public void deleteAll() {
        visitCounterService.resetAllCounts();
    }
}
