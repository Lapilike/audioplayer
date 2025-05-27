// Layout.jsx
import React from "react"
import { Outlet } from "react-router-dom";
import { useState } from "react";
import { Box } from "@mui/material";
import AudioPlayer from "../components/AudioPlayer";
import Navbar from "../components/Navbar";
import axios from "axios";

export const AudioContext = React.createContext();

function Layout() {
    const [currentSongs, setCurrentSongs] = useState([]);
    const [startIndex, setStartIndex] = useState(0);

    const setCurrentSong = (song) => {
        setCurrentSongs(prev => [song]);
        console.log('current songs - ', currentSongs);
    };

    const setPlaylistSongs = (playlist) => {
        axios.get('/api/v1/music/all', {
            params: { ids: playlist.songIds },
            paramsSerializer: {indexes: null}
        }).then(response => {
            setCurrentSongs(response.data);
        })
    }

    return (
        <AudioContext.Provider
            value={{
                currentSongs,
                setCurrentSongs,
                setCurrentSong,
                setPlaylistSongs,
                setStartIndex,
        }}
        >
            <Box
                sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    minHeight: '100vh',
                }}
            >
                <Navbar />
                <Box
                    sx={{
                        paddingBottom: '100px',
                    }}
                >
                    <Outlet />
                </Box>
                <Box
                    component={"footer"}
                    sx={{
                        backgroundColor: "#1a1a1a",
                        position: 'fixed',
                        bottom: 0,
                        left: 0,
                        width: '100%',
                        zIndex: 1000
                    }}
                >
                    <AudioPlayer currentSongs={currentSongs} startIndex={startIndex}/>
                </Box>
            </Box>
        </AudioContext.Provider>
    );
}

export default Layout;
