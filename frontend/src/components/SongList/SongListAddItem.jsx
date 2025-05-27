import React from "react"
import {ListItem} from "@mui/material";
import {ListItemButton} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import {ListItemText} from "@mui/material";
import {Typography} from "@mui/material";

export default function ({onAddClick}) {
    return (
        <ListItem disablePadding>
        <ListItemButton onClick={onAddClick} sx={{borderRadius: 3}}>
            <AddIcon sx={{
                color: '#b39ddb',
                marginRight: 2
            }}
            />
            <ListItemText
                primary={
                    <Typography variant="subtitle1" color="#b39ddb">
                        Добавить песню
                    </Typography>
                }
            />
        </ListItemButton>
    </ListItem>
    );
}