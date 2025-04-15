package by.lapil.audioplayer;

import by.lapil.audioplayer.cache.Cache;
import by.lapil.audioplayer.exception.NotFoundException;
import by.lapil.audioplayer.model.dto.CreateSongDto;
import by.lapil.audioplayer.model.dto.SongDto;
import by.lapil.audioplayer.model.entity.Album;
import by.lapil.audioplayer.model.entity.Artist;
import by.lapil.audioplayer.model.entity.Song;
import by.lapil.audioplayer.utils.Genres;
import by.lapil.audioplayer.repository.SongRepository;
import by.lapil.audioplayer.service.ArtistService;
import by.lapil.audioplayer.service.impl.SongServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SongServiceTest {

    @InjectMocks
    private SongServiceImpl songService;

    @Mock
    private SongRepository songRepository;

    @Mock
    private ArtistService artistService;

    @Mock
    private Cache<List<Song>> cache;

    private Song song;
    private CreateSongDto createDto;
    private Artist artist;

    @BeforeEach
    void setup() {
        artist = new Artist();
        artist.setId(1L);
        artist.setName("Artist 1");

        song = new Song();
        song.setId(1L);
        song.setTitle("Title");
        song.setGenre(Genres.ROCK);
        song.setArtist(List.of(artist));

        createDto = new CreateSongDto();
        createDto.setTitle("Title");
        createDto.setGenre(Genres.ROCK);
        createDto.setFilePath("path");
        createDto.setArtists(List.of(1L));
    }

    @Test
    void findAll_ShouldReturnList() {
        when(songRepository.findAll()).thenReturn(List.of(song));

        List<Song> result = songService.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void findAllById_ShouldReturnList() {
        when(songRepository.findAllById(any())).thenReturn(List.of(song));

        List<Song> result = songService.findAllById(List.of(1L));

        assertEquals(1, result.size());
    }

    @Test
    void findById_ShouldReturnSong() {
        when(songRepository.findById(1L)).thenReturn(Optional.of(song));

        Song result = songService.findById(1L);

        assertEquals("Title", result.getTitle());
    }

    @Test
    void findById_WhenNotFound_ShouldThrow() {
        when(songRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> songService.findById(99L));
    }

    @Test
    void findByCriteria_Cached_ShouldReturnCachedList() {
        when(cache.get(anyString())).thenReturn(List.of(song));

        List<SongDto> result = songService.findByCriteria("title", "artist");

        assertEquals(1, result.size());
    }

    @Test
    void findByCriteria_NotFound_ShouldThrow() {
        when(cache.get(anyString())).thenReturn(null);
        when(songRepository.findByCriteria(any(), any())).thenReturn(List.of());

        assertThrows(NotFoundException.class, () -> songService.findByCriteria("t", "a"));
    }

    @Test
    void update_ShouldUpdateSong() {
        when(songRepository.findById(1L)).thenReturn(Optional.of(song));
        when(songRepository.save(any())).thenReturn(song);
        when(artistService.findAllById(any())).thenReturn(new ArrayList<>());

        SongDto result = songService.update(1L, createDto);

        assertNotNull(result);
    }

    @Test
    void deleteById_ShouldRemoveSong() {
        song.setArtist(new ArrayList<>());
        Album album = new Album();
        album.setSongs(new ArrayList<>(List.of(song)));
        song.setAlbum(album);

        when(songRepository.findById(1L)).thenReturn(Optional.of(song));

        songService.deleteById(1L);

        verify(songRepository).deleteById(1L);
        verify(cache).remove(1L);
    }
}
