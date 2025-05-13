import React from 'react';
import { Card, CardContent, Typography } from '@mui/material';
import {ThemeProvider} from "@mui/material";
import {darkTheme} from "../themes/theme";
import {CardMedia} from "@mui/material";
import {Box} from "@mui/material";

function SongCard({ song }) {
    return (
        <ThemeProvider theme={darkTheme}>
        <Card sx={{
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
        }}}>
            <CardMedia
                component="img"
                height="250"
                image={song.coverUrl ? song.coverUrl : "default-cover.jpg"} // Путь к изображению обложки
                sx={{ borderRadius: '10px' }}
                alt="Album cover"
            />

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
