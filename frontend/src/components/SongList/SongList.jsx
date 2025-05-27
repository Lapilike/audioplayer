import {
    List,
    ListItem,
    ListItemText,
    ListItemButton,
    Typography,
    Box,
} from '@mui/material';
import MusicNoteIcon from '@mui/icons-material/MusicNote';
import React from "react"

function SongList({ songs, selectedSongs, onSongClick}) {
    if(selectedSongs == null) {
        selectedSongs = [];
    }

    return (
        <Box sx={{
            backgroundColor: '#121212',
            minWidth: '500px',
        }}>
            <List>
                {songs.map((song, index) => {
                    const isSelected = selectedSongs.includes(song.id);
                    return (
                        <React.Fragment key={index}>
                            <ListItem
                                disablePadding
                            >
                                <ListItemButton
                                    onClick={() => onSongClick(song)}
                                    selected={isSelected}
                                    sx={{
                                        borderRadius: isSelected ? 0 : 3,
                                        backgroundColor: isSelected ? '#311b92' : 'transparent',
                                        '&:hover': {
                                            backgroundColor: isSelected ? '#4527a0' : '#1e1e1e'
                                        }
                                    }}>
                                    <MusicNoteIcon sx={{color: '#b39ddb', marginRight: 2}}/>
                                    <ListItemText
                                        primary={
                                            <Typography variant="subtitle1" color="white">
                                                {song.title}
                                            </Typography>
                                        }
                                        secondary={
                                            <Typography variant="body2" color="gray">
                                                {song.artists.join(', ')}
                                            </Typography>
                                        }
                                    />
                                </ListItemButton>
                            </ListItem>
                        </React.Fragment>
                    );
                })}
            </List>
        </Box>
    );
}

export default SongList;