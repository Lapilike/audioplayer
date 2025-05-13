import React, {useState, useEffect} from 'react';
import axios from "axios";
import {CircularProgress} from "@mui/material";
import SongCard from "./SongCard";
import {Box} from "@mui/material";
import {Typography} from "@mui/material";

function SongScrollList() {
    const [songs, setSongs] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        // Замени URL на URL твоего API
        axios.get('api/v1/music')
            .then(response => {
                setSongs(response.data);  // Присваиваем полученные данные в состояние
                setLoading(false);         // Завершаем процесс загрузки
            })
            .catch(err => {
                setError('Ошибка загрузки данных');
                setLoading(false);
            });
    }, []);

    if (loading) {
        return (
            <div align={"center"}>
                <CircularProgress/>
            </div>
        );
    }

    if (error) {
        return <div>{error}</div>;
    }

    return (
        <Box sx={{
            width: '100%',
            overflowX: 'auto',
            padding: '10px 0',
            backgroundColor: "transparent"
        }}>
            <Typography variant="h3" sx={{fontWeight: 'bold', lineHeight: 2}}>Популярные треки</Typography>
            <Box sx={{ display: 'flex', gap: 2 }}>
                {songs.map((song, index) => (
                    <SongCard key={index} song={song}/>
                ))}
            </Box>
        </Box>
    );
}

export default SongScrollList;