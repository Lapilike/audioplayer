package audioplayer.service.impl;

import audioplayer.exception.NotFoundException;
import audioplayer.model.dto.CreatePlaylistDto;
import audioplayer.model.dto.PlaylistDto;
import audioplayer.model.entity.Playlist;
import audioplayer.model.entity.Song;
import audioplayer.repository.PlaylistRepository;
import audioplayer.service.PlaylistService;
import audioplayer.service.SongService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PlaylistServiceImpl implements PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final SongService songService;

    @Override
    public List<PlaylistDto> findAll() {
        List<Playlist> playlistList = playlistRepository.findAll();
        return playlistList.stream().map(PlaylistDto::new).toList();
    }

    @Override
    public List<PlaylistDto> findByName(String name) {
        List<Playlist> playlistList = playlistRepository.findByName(name);
        if (playlistList.isEmpty()) {
            throw new NotFoundException(NotFoundException.ALBUM_NOT_FOUND);
        }
        return playlistList.stream().map(PlaylistDto::new).toList();
    }

    @Override
    public PlaylistDto findById(Long id) {
        return new PlaylistDto(playlistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundException.ALBUM_NOT_FOUND)));
    }

    @Override
    public PlaylistDto create(CreatePlaylistDto createDto) {
        Playlist playlist = new Playlist(createDto);
        if (createDto.getSongs() != null && !createDto.getSongs().isEmpty()) {
            List<Song> songList = songService.findAllById(createDto.getSongs());
            playlist.setSongs(songList);
        }

        Playlist savedPlaylist = playlistRepository.save(playlist);

        return new PlaylistDto(savedPlaylist);
    }

    @Override
    public PlaylistDto patch(Long id, CreatePlaylistDto createDto) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundException.ALBUM_NOT_FOUND));
        if (createDto.getName() != null && !createDto.getName().isBlank()) {
            playlist.setName(createDto.getName());
        }
        if (createDto.getSongs() != null) {
            List<Song> songList = songService.findAllById(createDto.getSongs());
            playlist.setSongs(songList);
        }
        Playlist savedPlaylist = playlistRepository.save(playlist);
        return new PlaylistDto(savedPlaylist);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundException.ALBUM_NOT_FOUND));

        List<Song> songList = playlist.getSongs();
        if (songList != null && !songList.isEmpty()) {
            songList.forEach(song -> song.setPlaylists(null));
            playlist.getSongs().clear();
            playlistRepository.save(playlist);
        }

        playlistRepository.deleteById(id);
    }
}
