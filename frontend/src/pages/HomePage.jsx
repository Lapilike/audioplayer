import SongScrollList from "../components/SongScrollList";
import {Box} from "@mui/material";

export default function HomePage() {

    return (
        <Box sx={{padding: 10 + "px"}}>
            <SongScrollList/>
        </Box>
    )
}