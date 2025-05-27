import React from "react";
import {VisibilityContext} from "react-horizontal-scrolling-menu";
import {IconButton} from "@mui/material";
import {ChevronLeft} from "@mui/icons-material";
import {ChevronRight} from "@mui/icons-material";

export const LeftArrow = () => {
    const { isLastItemVisible, scrollPrev} = React.useContext(VisibilityContext);
    return (
        <IconButton
            onClick={() => scrollPrev()}
            disabled={isLastItemVisible}
            sx={{
                position: "absolute",
                left: 8,
                top: "45%",
                background: "#252525",
                transform: "translateY(-50%)",
                boxShadow: 1,
                borderRadius: "50%",
                width: 50,
                height: 50,
                zIndex: 10,
                "&:hover": {
                    background: "#373737",
                },
            }}
            aria-label="scroll left"
        >
            <ChevronLeft />
        </IconButton>
    );
};

export const RightArrow = () => {
    const {isFirstItemVisible, scrollNext} = React.useContext(VisibilityContext);
    return (
        <IconButton
            onClick={() => scrollNext()}
            disabled={isFirstItemVisible}
            sx={{
                position: "absolute",
                right: 8,
                top: "45%",
                background: "#252525",
                transform: "translateY(-50%)",
                boxShadow: 1,
                borderRadius: "50%",
                width: 50,
                height: 50,
                zIndex: 10,
                "&:hover": {
                    background: "#373737",
                },
            }}
            aria-label="scroll right"
        >
            <ChevronRight />
        </IconButton>
    );
};