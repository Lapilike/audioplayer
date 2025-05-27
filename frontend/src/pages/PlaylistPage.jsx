import React from "react"
import SongList from "../components/SongList/SongList";
import {Box} from "@mui/material";
import {useContext} from "react";
import {AudioContext} from "../layout/Layout";
import {useEffect} from "react";
import axios from "axios";
import {useState} from "react";
import {useLocation} from "react-router-dom";
import {Typography} from "@mui/material";
import SongChooseMenu from "../components/SongChooseMenu";
import SongListAddItem from "../components/SongList/SongListAddItem";
import {CircularProgress} from "@mui/material";

export default function PlaylistPage() {
    const {currentSongs} = useContext(AudioContext);
    const {setCurrentSongs} = useContext(AudioContext);
    const {setStartIndex} = useContext(AudioContext);
    const [songs, setSongs] = useState([]);
    const [isAdding, setIsAdding] = useState(false);
    const [playlist, setPlaylist] = useState([]);
    const [songIds, setSongIds] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const { state } = useLocation();
    const [retryCount, setRetryCount] = useState(0);
    const MAX_RETRIES = 5;
    const playlistId = state?.playlistId;

    useEffect(() => {
        updatePlaylist();
    }, []);

    useEffect(() => {
        if (loading && retryCount >= MAX_RETRIES) {
            setError("Loading error occured");
            return;
        }

        const retryTimeout = setTimeout(() => {
            console.log("Повтор запроса...", retryCount + 1);
            setRetryCount(prev => prev + 1);
            updatePlaylist();
        }, 3000);
        clearTimeout(retryTimeout);
    }, [loading, retryCount]);

    const updatePlaylist = () => {
        axios.get(`api/v1/playlist/${playlistId}`)
            .then(response => {
                const data = response.data;
                setPlaylist(data);
                setSongIds(data.songIds);
                console.log('loading ended - ', playlist);
                return axios.get('/api/v1/music/all', {
                    params: { ids: data.songIds },
                    paramsSerializer: {indexes: null}
                });
            })
            .then(response => {
                setSongs(response.data);
                setLoading(false);
            })
            .catch(err => {
                console.log('error', err.toString());
                setLoading(true);
            });
    }

    const handleConfirm = async () => {
        try {
            console.log('new songs - ', songIds);
            const response = await axios.patch(`/api/v1/playlist/${playlist.id}`, {
                songs: songIds
            });
            updatePlaylist();
            setIsAdding(false);
            console.log('Успешно отправлено:', response.data);
        } catch (error) {
            console.error('Ошибка:', error);
            // Можно уточнить:
            // console.error('Ошибка:', error.response?.data || error.message);
        }
    };

    console.log('songIds - ', songIds)

    if (loading) {
        return (
            <Box
                sx={{
                    minHeight: "400px",
                    alignContent: "center"
                }}
            >
                <CircularProgress/>
            </Box>
        );
    }

    if (error) {
        return (
            <Box
                sx={{
                    minHeight: "300px",
                    alignContent: "center"
                }}
            >
                {error}
            </Box>
        );
    }

    const handleSongClick = (song) => {
        if(currentSongs !== songs) {
            setCurrentSongs(songs);
        }
        const index = songs.indexOf(song);
        console.log('current index - ', index);
        setStartIndex(index);
    }

    return (
        <Box margin={2}>
            <Box
                sx={{
                    height: 100,
                    backgroundColor: "primary",
                }}
            >
                <Typography
                    align={"center"}
                    variant={"h3"}
                    sx={{
                        fontWeight: "bold",
                        padding: 3,
                    }}
                >
                    {playlist.name}
                </Typography>
            </Box>
            <SongList songs={songs} onSongClick={handleSongClick}/>
            <SongListAddItem onAddClick={() => setIsAdding(true)}/>
            <SongChooseMenu
                songIds={songIds}
                setSongIds={setSongIds}
                playlist={playlist}
                updatePlaylist={updatePlaylist}
                isAdding={isAdding}
                setIsAdding={() => setIsAdding(false)}
                handleConfirm={handleConfirm}
            />
        </Box>
    );
}