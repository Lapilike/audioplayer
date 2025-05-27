import React from 'react';
import { CardContent, Typography } from '@mui/material';
import {ThemeProvider} from "@mui/material";
import {darkTheme} from "../../themes/theme";
import {CardMedia} from "@mui/material";
import {Box} from "@mui/material";
import {useNavigate} from "react-router-dom";
import {PlaylistPlay} from "@mui/icons-material";
import MenuIcon from '@mui/icons-material/Menu';
import DeleteIcon from '@mui/icons-material/Delete';
import {Stack} from "@mui/material";
import {IconButton} from "@mui/material";

function PlaylistCard({playlist, onClick, onDeleteClick}) {
    const navigate = useNavigate();
    const playlistId = playlist.id;

    const handleClick = () => {
        navigate('/playlist', { state: { playlistId } });
    };

    return (
        <ThemeProvider theme={darkTheme}>
            <Box sx={{
                position: 'relative',
                minWidth: 250,
                minHeight: 350,
                maxHeight: 450,
                textAlign: 'center',
                backgroundColor: 'transparent',
                boxShadow: 'none',
                borderRadius: '5px',
                border: '5px solid transparent',
                transition: 'background-color 0.3s ease',
                '&:hover': {
                    backgroundColor: 'rgba(255, 255, 255, 0.08)'
                },
                '&:hover .hover-play': {
                    opacity: 1,
                }
            }}>
                <Stack
                    spacing={1}
                    className="hover-play"
                    sx={{
                        position: 'absolute',
                        top: '40%',
                        width: '100%',
                        height: 60,
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        justifySelf: 'center',
                        opacity: 0,
                        transition: 'opacity 0.3s ease',
                        zIndex: 2,
                    }}
                >
                    <IconButton
                        sx={{
                            backgroundColor: '#bb86fc',
                            transition: 'transform 0.2s ease',
                            '&:hover': {
                                transform: 'scale(1.1)',
                                backgroundColor: '#bb86fc',
                            },
                        }}
                    >
                        <PlaylistPlay
                            sx={{ color: 'white', fontSize: 45 }}
                            onClick={onClick}
                        />
                    </IconButton>
                    <IconButton
                        sx={{
                            backgroundColor: 'red',
                            transition: 'transform 0.2s ease',
                            '&:hover': {
                                transform: 'scale(1.1)',
                                backgroundColor: 'red',
                            },
                        }}
                    >
                        <DeleteIcon
                            sx={{ color: 'white', fontSize: 30 }}
                            onClick={onDeleteClick}
                        />
                    </IconButton>
                </Stack>
                <Box onClick={handleClick}
                     sx={{
                        backgroundColor: 'transparent',
                    }}
                >
                    <MenuIcon/>
                    <CardMedia
                        component="img"
                        height="250"
                        image={"default-cover.jpg"} // Путь к изображению обложки
                        sx={{ borderRadius: '10px' }}
                        alt="Album cover"
                    />
                    <CardContent>
                        <Box sx={{
                            alignContent: "center"
                        }}>
                            <Typography variant="h7" >{playlist.name}</Typography>
                        </Box>
                    </CardContent>
                </Box>
            </Box>
        </ThemeProvider>
    );
}

export default PlaylistCard;
