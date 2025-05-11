package by.lapil.audioplayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.lapil.audioplayer.exception.NotFoundException;
import by.lapil.audioplayer.model.dto.AlbumDto;
import by.lapil.audioplayer.model.dto.CreateAlbumDto;
import by.lapil.audioplayer.model.entity.Album;
import by.lapil.audioplayer.model.entity.Artist;
import by.lapil.audioplayer.model.entity.Song;
import by.lapil.audioplayer.repository.AlbumRepository;
import by.lapil.audioplayer.service.ArtistService;
import by.lapil.audioplayer.service.SongService;
import by.lapil.audioplayer.service.impl.AlbumServiceImpl;
import by.lapil.audioplayer.utils.Genres;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AlbumServiceTest {
    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private SongService songService;

    @Mock
    private ArtistService artistService;

    @InjectMocks
    private AlbumServiceImpl albumService;
    private Album album;
    private Artist artist;
    private Song song;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        album = new Album();
        album.setId(1L);
        album.setName("Test Album");

        artist = new Artist();
        artist.setId(1L);
        artist.setName("Test Artist");

        song = new Song();
        song.setId(1L);
        song.setTitle("Test Song");
        song.setGenre(Genres.ROCK);
        song.setFilePath("/path");
        song.setAlbum(album);
        song.setArtist(new ArrayList<>(List.of(artist)));

        album.setArtist(artist);
        album.setSongs(new ArrayList<>(List.of(song)));
        artist.setSongs(new ArrayList<>(List.of(song)));
        artist.setAlbums(new ArrayList<>());
    }

    @Test
    void findAll_ShouldReturnList() {
        when(albumRepository.findAll()).thenReturn(List.of(album));
        List<AlbumDto> result = albumService.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void findByName_ShouldReturnList() {
        when(albumRepository.findByName(album.getName())).thenReturn(List.of(album));
        List<AlbumDto> result = albumService.findByName(album.getName());
        assertEquals(1, result.size());
    }

    @Test
    void findByName_ShouldThrowNotFoundException() {
        when(albumRepository.findByName("Nonexistent name")).thenReturn(Collections.emptyList());
        assertThrows(NotFoundException.class, () -> albumService.findByName("Nonexistent name"));
    }

    @Test
    void create_shouldReturnAlbumDto_whenValidCreateDto() {
        CreateAlbumDto createDto = new CreateAlbumDto();
        createDto.setName("new Album");
        createDto.setArtist(1L);
        createDto.setSongs(List.of(1L));

        Album newAlbum = new Album(createDto);
        newAlbum.setId(2L);
        newAlbum.setArtist(artist);
        newAlbum.setSongs(new ArrayList<>(List.of(song)));

        List<Song> songList = new ArrayList<>(List.of(song));

        when(songService.findAllById(createDto.getSongs())).thenReturn(songList);
        when(artistService.findById(createDto.getArtist())).thenReturn(artist);
        when(albumRepository.save(any(Album.class))).thenReturn(newAlbum);

        AlbumDto result = albumService.create(createDto);

        assertNotNull(result);
        assertEquals(createDto.getName(), result.getName());
        assertEquals(2L, result.getId());

        verify(albumRepository).save(any(Album.class));
        verify(artistService).update(anyList());
        verify(songService).update(songList);
    }

    @Test
    void patch_ShouldThrowNotFoundException() {
        CreateAlbumDto createDto = new CreateAlbumDto();
        when(albumRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> albumService.patch(2L, createDto));
    }

    @Test
    void patch_ShouldUpdateAllFields() {
        artist.setAlbums(new ArrayList<>(List.of(album)));

        Artist newArtist = new Artist();
        newArtist.setId(2L);
        newArtist.setName("New Artist");
        newArtist.setAlbums(new ArrayList<>());

        Song newSong = new Song();
        newSong.setId(2L);
        newSong.setTitle("New Song");

        CreateAlbumDto dto = new CreateAlbumDto();
        dto.setName("New Name");
        dto.setArtist(2L);
        dto.setSongs(List.of(2L));

        when(albumRepository.findById(1L)).thenReturn(Optional.of(album));
        when(artistService.findById(2L)).thenReturn(newArtist);
        when(albumRepository.save(any(Album.class))).thenAnswer(inv -> inv.getArgument(0));

        AlbumDto result = albumService.patch(1L, dto);

        assertEquals("New Name", result.getName());
        assertEquals(newArtist.getName(), result.getArtistName());
        verify(artistService).update(anyList());
        verify(albumRepository).save(album);
    }

    @Test
    void patch_shouldUpdateAlbumName() {
        CreateAlbumDto dto = new CreateAlbumDto();
        dto.setName("New Name");

        when(albumRepository.findById(1L)).thenReturn(Optional.of(album));
        when(albumRepository.save(any(Album.class))).thenAnswer(inv -> inv.getArgument(0));

        AlbumDto result = albumService.patch(1L, dto);

        assertEquals("New Name", result.getName());
        verify(albumRepository).save(album);
    }

    @Test
    void patch_shouldUpdateArtist() {
        artist.setAlbums(new ArrayList<>(List.of(album)));

        Artist newArtist = new Artist();
        newArtist.setId(2L);
        newArtist.setName("New Artist");
        newArtist.setAlbums(new ArrayList<>());

        CreateAlbumDto dto = new CreateAlbumDto();
        dto.setArtist(2L);

        when(albumRepository.findById(1L)).thenReturn(Optional.of(album));
        when(artistService.findById(2L)).thenReturn(newArtist);
        when(albumRepository.save(any(Album.class))).thenAnswer(inv -> inv.getArgument(0));

        AlbumDto result = albumService.patch(1L, dto);

        assertEquals(newArtist.getName(), result.getArtistName());
        verify(artistService).update(anyList());
        verify(albumRepository).save(album);
    }

    @Test
    void patch_shouldUpdateSongs() {
        Song newSong = new Song();
        newSong.setId(2L);
        newSong.setTitle("New Song");

        CreateAlbumDto dto = new CreateAlbumDto();
        dto.setSongs(List.of(2L));

        when(albumRepository.findById(1L)).thenReturn(Optional.of(album));
        when(songService.findAllById(List.of(2L))).thenReturn(new ArrayList<>(List.of(newSong)));
        when(albumRepository.save(any(Album.class))).thenAnswer(inv -> inv.getArgument(0));

        AlbumDto result = albumService.patch(1L, dto);

        assertEquals(1, result.getSongName().size());
        assertTrue(result.getSongName().stream().anyMatch(s -> s.equals(newSong.getTitle())));
        verify(albumRepository).save(album);
    }

    @Test
    void delete_ShouldThrowNotFoundException() {
        when(albumRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> albumService.delete(2L));
    }

    @Test
    void delete_shouldRemoveSongsAndDeleteAlbum_whenAlbumFound() {
        List<Song> songList = new ArrayList<>(List.of(song));
        album.setSongs(songList);

        when(albumRepository.findById(1L)).thenReturn(Optional.of(album));
        doNothing().when(albumRepository).deleteById(1L);
        when(albumRepository.save(any(Album.class))).thenAnswer(inv -> inv.getArgument(0));

        albumService.delete(1L);

        assertTrue(album.getSongs().isEmpty());
        verify(albumRepository).deleteById(1L);
        verify(albumRepository).save(album);
    }
}


