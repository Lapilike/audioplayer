package audioplayer.service;

import static org.assertj.core.api.Assertions.assertThat;

import audioplayer.service.impl.VisitCounterServiceImpl;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class VisitCounterServiceTest {
    @InjectMocks
    VisitCounterServiceImpl visitCounterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void increment_shouldIncreaseCountForUrl() {
        String url = "https://example.com";
        visitCounterService.increment(url);

        assertThat(visitCounterService.getCount(url)).isEqualTo(1);
    }

    @Test
    void getCount_shouldReturnZeroIfUrlNotExists() {
        String url = "https://unknown.com";
        int count = visitCounterService.getCount(url);

        assertThat(count).isZero();
    }

    @Test
    void getAllCounts_shouldReturnMapOfAllCounts() {
        visitCounterService.increment("https://example.com");
        visitCounterService.increment("https://example.com");
        visitCounterService.increment("https://test.com");

        Map<String, Integer> counts = visitCounterService.getAllCounts();

        assertThat(counts)
                .hasSize(2)
                .containsEntry("https://example.com", 2)
                .containsEntry("https://test.com", 1);
    }

    @Test
    void resetAllCounts_shouldClearAllCounts() {
        String url1 = "https://example.com";
        String url2 = "https://test.com";

        visitCounterService.increment(url1);
        visitCounterService.increment(url2);
        visitCounterService.increment(url2);

        visitCounterService.resetAllCounts();

        assertThat(visitCounterService.getCount(url1)).isZero();
        assertThat(visitCounterService.getCount(url2)).isZero();
        assertThat(visitCounterService.getAllCounts()).isEmpty();
    }

    @Test
    void resetAllCounts_shouldLeaveNewCountsEmpty() {
        visitCounterService.resetAllCounts();
        String url = "https://newurl.com";
        visitCounterService.increment(url);

        assertThat(visitCounterService.getCount(url)).isEqualTo(1);
    }
}
