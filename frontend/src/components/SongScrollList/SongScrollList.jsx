import React from "react";
import { ScrollMenu} from "react-horizontal-scrolling-menu";
import "react-horizontal-scrolling-menu/dist/styles.css";
import {
    Box,
    CircularProgress
} from "@mui/material";
import SongCard from "./SongCard";
import {LeftArrow, RightArrow} from "../Arrows"

export default function SongScrollMenu({songs, loading, error, onClick}) {
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
            {songs.map((song) => (
                //eslint-disable-next-line react/jsx-no-undef
                <Box
                    key={song.id}
                    itemID={song.id}
                    style={{
                        display: 'inline-block', margin: '10px'}}
                >
                    <SongCard song={song} onClick = {() => onClick(song)}/>
                </Box>
            ))}
        </ScrollMenu>

        </Box>
    );
}