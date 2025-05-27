import SongScrollList from "../components/SongScrollList/SongScrollList";
import {Box} from "@mui/material";
import {Typography} from "@mui/material";
import {useState} from "react";
import {useEffect} from "react";
import axios from "axios";
import {useContext} from "react";
import { AudioContext } from "../layout/Layout";
import PlaylistScrollMenu from "../components/PlaylistScrollList/PlaylistScrollList";


export default function HomePage() {
    const {setCurrentSong} = useContext(AudioContext);
    const {setPlaylistSongs} = useContext(AudioContext);
    const [songs, setSongs] = useState([]);
    const [playlists, setPlaylists] = useState([]);
    const [loading, setLoading] = useState(true);
    const [playlistAdded, setPlaylistAdded] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        axios.get('https://audioplayer-backend.onrender.com/api/v1/music', {
            withCredentials: true
        })
            .then(response => {
                console.log('Запрос отправлен на:', response.config.url);
                setSongs(response.data);  // Присваиваем полученные данные в состояние
                setLoading(false);         // Завершаем процесс загрузки
            })
            .catch(err => {
                setError('Ошибка загрузки данных', err.toString());
                setLoading(true);
            });
    }, []);

    const loadPlaylist = async () => {
        axios.get('https://audioplayer-backend.onrender.com/api/v1/playlist', {
            withCredentials: true
        })
            .then(response => {
                setPlaylists(response.data);  // Присваиваем полученные данные в состояние
                setLoading(false);         // Завершаем процесс загрузки
            })
            .catch(err => {
                setError('Ошибка загрузки данных', err.toString());
                setLoading(true);
            });
        console.log("loaded playlists - ", playlists);
    };

    const handleDelete = (playlist) => {
        axios.delete(`api/v1/playlist/${playlist.id}`)
            .catch(error => {
                console.log('Delete error', error)
            });
        loadPlaylist().catch(console.error);
    }

    useEffect(() => {
        loadPlaylist().catch(console.error);
        console.log("loaded playlists - ", playlists);
        setPlaylistAdded(false);
    }, []);

    console.log('songs - ', songs);
    return (
        <Box>
            <Box sx={{padding: 10 + "px"}}>
                <Typography variant="h2" fontWeight = "bold" textAlign="center">Сборник треков</Typography>
                <SongScrollList songs={songs} loading={loading} error={error} onClick={setCurrentSong}/>
            </Box>
            <Box sx={{padding: 10 + "px"}}>
                <Typography variant="h2" fontWeight = "bold" textAlign="center">Любимые плейлисты</Typography>
                <PlaylistScrollMenu
                    playlists={playlists}
                    loading={loading}
                    error={error}
                    playlistAdded={() => loadPlaylist()}
                    onClick={setPlaylistSongs}
                    onDeleteClick={handleDelete}
                />
            </Box>
        </Box>
    )
}