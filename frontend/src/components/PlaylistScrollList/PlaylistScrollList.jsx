import React from "react";
import {ScrollMenu} from "react-horizontal-scrolling-menu";
import "react-horizontal-scrolling-menu/dist/styles.css";
import {
    Box,
    CircularProgress
} from "@mui/material";
import {LeftArrow, RightArrow} from "../Arrows"
import PlaylistCard from "./PlaylistCard";
import AddPlaylistCard from "./AddPlaylistCard";

export default function PlaylistScrollMenu({playlists, loading, error, playlistAdded, onClick, onDeleteClick}) {

    if (loading) {
        return (
            <Box align={"center"}
                 sx={{
                     minHeight: "300px",
                 }}>
                <CircularProgress/>
            </Box>
        );
    }

    if (error) {
        return <div>{error}</div>;
    }

    return (
        <Box sx = {{position: 'relative',
            "& .react-horizontal-scrolling-menu--scroll-container": {
                scrollbarWidth: "none",  // Firefox
                msOverflowStyle: "none", // IE 10+
                "&::-webkit-scrollbar": {
                    display: "none",       // Chrome, Safari, Edge
                }
            }
        }}>
            <ScrollMenu
                LeftArrow={<LeftArrow />}
                RightArrow={<RightArrow />}>
                {playlists.map((playlist) => (
                    //eslint-disable-next-line react/jsx-no-undef
                    <Box
                        key={playlists.id}
                        itemID={playlists.id}
                        sx={{
                            display: 'inline-block',
                            paddingY: 1,
                        }}
                    >
                        <PlaylistCard
                            playlist={playlist}
                            onClick={() => onClick(playlist)}
                            onDeleteClick={() => onDeleteClick(playlist)}
                        />
                    </Box>
                ))}
                <AddPlaylistCard
                    onPlaylistAdded={(newPlaylist) => {
                        console.log('Добавлен:', newPlaylist);
                        playlistAdded();
                    }}/>
            </ScrollMenu>
        </Box>
    );
}