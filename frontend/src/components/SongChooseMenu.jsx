import React from "react"
import {Box} from "@mui/material";
import axios from "axios";
import {useEffect} from "react";
import {useState} from "react";
import SongList from "./SongList/SongList";
import {ClickAwayListener} from "@mui/material";
import {Button} from "@mui/material";

function SongChooseMenu(
    {
        children,
        songIds,
        setSongIds,
        isAdding,
        setIsAdding,
        handleConfirm
    }) {
    const [songs, setSongs] = useState([]);

    useEffect(() => {
        axios.get('api/v1/music')
            .then(response => {
                setSongs(response.data);       // Завершаем процесс загрузки
            })
            .catch(err => {
                console.log('Error occured', err.toString());
            });
    }, []);

    if(!isAdding) {
        return null;
    }

    const handleAddSong = (song) => {
        setSongIds(prev =>
            prev.includes(song.id)
                ? prev.filter(songId => songId !== song.id) // убрать, если уже есть
                : [...prev, song.id] // добавить, если нет
        );
    };

    return (

        <Box
            sx= {{
                display: 'flex',
                position: "fixed",
                top: 0,
                left: 0,
                width: '100vw',
                height: '100vh',
                justifyContent: 'center',
                alignItems: 'center',
                backgroundColor: "rgba(0, 0, 0, 0.6)", // затемнение фона
                zIndex: 1300 // поверх всего, но можно настроить
            }}
        >
            <ClickAwayListener onClickAway={setIsAdding}>
                <Box>
                    {children}
                    <Box
                        sx = {{
                        maxHeight: "400px",
                        borderRadius: 2,
                        overflowY: 'auto',
                        '&::-webkit-scrollbar': {
                            display: 'none',         // Chrome, Safari, Opera
                        },
                    }}>
                    <SongList
                        songs={songs}
                        selectedSongs={songIds}
                        onSongClick={handleAddSong}
                    />
                    </Box>
                    <Box
                        sx={{
                            p: 2,
                            display: 'flex',
                            justifyContent: 'center'  // центрируем кнопку по горизонтали
                        }}
                    >
                    <Button
                        sx = {{
                            alignSelf: 'center'
                        }}
                        variant="contained"
                        color="secondary"
                        onClick={handleConfirm}
                        disabled={songIds.length === 0}
                    >
                        Подтвердить выбор
                    </Button>
                    </Box>
                </Box>
            </ClickAwayListener>
        </Box>

    );
}

export default SongChooseMenu;