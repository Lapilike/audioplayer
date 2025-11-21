import React from 'react';
import { AppBar, Toolbar} from '@mui/material';
import {IconButton} from "@mui/material";
import HomeIcon from '@mui/icons-material/Home';
import { Link } from 'react-router-dom';
import SearchBar from "./SearchBar";
import MusicNoteIcon from "@mui/icons-material/MusicNote";

export default function Navbar() {
    return (
        <AppBar position="static" sx={{ margin: 0, padding: 0 }}>
            <Toolbar>
                <IconButton edge="start" color="inherit" aria-label="home" component={Link} to="/">
                    <HomeIcon />
                </IconButton>
                <IconButton edge="start" color="inherit" aria-label="home" component={Link} to="/mysongs">
                    <MusicNoteIcon />
                </IconButton>
                <SearchBar onSearch={"wanna play something?"}></SearchBar>
            </Toolbar>
        </AppBar>
    );
}
