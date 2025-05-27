import React from "react"
import {Stack} from "@mui/material";
import {IconButton} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import {Typography} from "@mui/material";
import {Paper} from "@mui/material";
import axios from "axios";
import {useState} from "react";
import {Box} from "@mui/material";
import {TextField} from "@mui/material";
import SongChooseMenu from "../SongChooseMenu";

function AddPlaylistCard({onPlaylistAdded }) {
    const [isAdding, setIsAdding] = useState(false);
    const [playlistName, setPlaylistName] = useState('');
    const [songIds, setSongIds] = useState([]);

    const handleSubmit = async () => {
        if (!playlistName.trim()) return;
        try {
            const response = axios.post('/api/v1/playlist', {
                name: playlistName,
                songs: songIds,
            });
            setIsAdding(false);
            setPlaylistName('');
            onPlaylistAdded?.(response.data); // обновление внешнего списка, если нужно
        } catch (err) {
            console.error(err);
        }
    };

    return (
        <Box
            sx={{
                display: 'inline-block', margin: '10px'
        }}>
            <Paper
                sx={{
                    minWidth: 250,
                    minHeight: 350,
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    backgroundColor: '#292929',
                    color: '#b39ddb',
                    cursor: 'pointer',
                    borderRadius: 3,
                    border: '2px dashed #b39ddb',
                    transition: 'background-color 0.3s',
                    '&:hover': {
                        backgroundColor: '#333',
                    },
                }}
                onClick={() => {
                    setIsAdding(true);
                    console.log("text -", isAdding)}
                }
            >
                <Stack alignItems="center" >
                    <IconButton color="inherit" onClick={() => setIsAdding(true)}>
                        <AddIcon />
                    </IconButton>
                    <Typography variant="body2">Добавить</Typography>
                </Stack>
            </Paper>
            {isAdding && (
                <Box>
                    <SongChooseMenu
                        songIds={songIds}
                        setSongIds={setSongIds}
                        updatePlaylist={() => {}}
                        isAdding={isAdding}
                        handleConfirm={handleSubmit}
                        setIsAdding={() => setIsAdding(false)}
                    >
                        <Paper
                            sx={{
                            }}
                        >
                            <TextField
                                size="small"
                                value={playlistName}
                                onChange={(e) => setPlaylistName(e.target.value)}
                                placeholder="Название нового плейлиста"
                                variant="outlined"
                                fullWidth
                            />
                        </Paper>
                    </SongChooseMenu>
                </Box>
            )}
        </Box>

    );
}

export default AddPlaylistCard;