import React from 'react';
import { Card, CardContent, Typography } from '@mui/material';
import {ThemeProvider} from "@mui/material";
import {darkTheme} from "../../themes/theme";
import {CardMedia} from "@mui/material";
import {Box} from "@mui/material";
import {PlayArrow} from "@mui/icons-material";
import {IconButton} from "@mui/material";

function SongCard({ song, onClick }) {
    return (
        <ThemeProvider theme={darkTheme}>
        <Card sx={{
                position: 'relative',
                minWidth: 250,
                maxHeight: 370,
                textAlign: 'center',
                backgroundColor: 'transparent',
                boxShadow: 'none',
                borderRadius: '10px',
                border: '5px solid transparent',
                transition: 'background-color 0.3s ease',
                '&:hover': {
                    backgroundColor: 'rgba(255, 255, 255, 0.08)'
                },
                '&:hover .hover-play': {
                    opacity: 1,
                },
        }} onClick={onClick}>

            <CardMedia
                component="img"
                height="250"
                image={"default-cover.jpg"} // Путь к изображению обложки
                sx={{ borderRadius: '10px' }}
                alt="Album cover"
            />
            <Box
                className="hover-play"
                sx={{
                    position: 'absolute',
                    top: '30%',
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
                    <PlayArrow sx={{ color: 'white', fontSize: 45 }} />
                </IconButton>
            </Box>
            <CardContent>
                <Box sx={{
                    alignContent: "center"
                }}>
                <Typography variant="h7" >{song.title}</Typography>
                <Typography variant="body2" sx={{color: '#A0A0A0'}}>{song.artists.join(", ")}</Typography>
                </Box>
            </CardContent>
        </Card>
        </ThemeProvider>
    );
}

export default SongCard;
