import {useRef, useState} from "react";
import {useEffect} from "react";
import React from "react";
import {Box, IconButton, Slider, Typography,} from "@mui/material";
import {Pause, PlayArrow} from "@mui/icons-material";
import VolumeUpIcon from '@mui/icons-material/VolumeUp';
import VolumeOffIcon from '@mui/icons-material/VolumeOff';
import {Stack} from "@mui/material";
import {SkipNext} from "@mui/icons-material";
import {SkipPrevious} from "@mui/icons-material";

const BASE_MUSIC_URL = "http://localhost:8080/api/v1/music/file/";

function getMusicUrl(id) {
    return `${BASE_MUSIC_URL}${id}`;
}

export default function AudioPlayer({currentSongs, startIndex}) {
    const audioRef = useRef(null);
    const [volume, setVolume] = useState(1);
    const [muted, setMuted] = useState(false);
    const [isPlaying, setIsPlaying] = useState(false);
    const [progress, setProgress] = useState(0);
    const [duration, setDuration] = useState(0);
    const [currentIndex, setCurrentIndex] = useState(startIndex);
    const [src, setSrc] = useState(null); // <-- источник для <audio>

    const togglePlay = () => {
        if (!audioRef.current) return;
        if (isPlaying) {
            audioRef.current.pause();
        } else {
            audioRef.current.play();
        }
        setIsPlaying(!isPlaying);
    };

    const next = () => {
        if (currentSongs.length > 1) {
            setCurrentIndex(prev => prev + 1);
        }
        if (currentIndex >= currentSongs.length - 1) {
            setCurrentIndex(0);
        }

        console.log('index - ', currentSong);
    };

    const previous = () => {
        if (currentSongs.length > 1) {
            setCurrentIndex(prev => prev - 1);
        }
        if (currentIndex <= 0) {
            setCurrentIndex(currentSongs.length - 1);
        }
    };

    const handleTimeUpdate = () => {
        const audio = audioRef.current;
        if (audio && duration) {
            setProgress((audio.currentTime / duration) * 100);
        }
    };

    const handleLoadedMetadata = () => {
        const audio = audioRef.current;
        if (audio) {
            setDuration(audio.duration);
        }
    };

    const handleSliderChange = (_, value) => {
        const audio = audioRef.current;
        if (audio && duration) {

            audio.currentTime = (value / 100) * duration;
            setProgress(value);
        }
    };

    const handleVolumeChange = (event, newValue) => {
        const audio = audioRef.current;
        if (!audio) return;

        setVolume(newValue);
        audio.volume = newValue;
        if (newValue === 0) {
            setMuted(true);
            audio.muted = true;
        } else {
            setMuted(false);
            audio.muted = false;
        }
    };

    const toggleMute = () => {
        const audio = audioRef.current;
        if (!audio) return;

        audio.muted = !muted;
        setMuted(!muted);
    };

    const currentSong = currentSongs[currentIndex];

    useEffect(() => {
        setCurrentIndex(startIndex);
    }, [startIndex]);


    useEffect(() => {
        if (currentSongs.length > 0) {
            setCurrentIndex(0);
        }
    }, [currentSongs]);

    useEffect(() => {
        let cancelled = false;
        async function resolveSource() {
            if (!currentSong) {
                setSrc(null);
                return;
            }

            // определяем идентификатор для запроса: используем id, filePath или urls
            const idOrUrl = getMusicUrl(currentSong.id);

            if (!idOrUrl) {
                // fallback: если есть поле filePath, используем его как локальный путь
                if (currentSong.filePath) {
                    const fileUrl = `file://${currentSong.filePath.replace(/\\/g, '/')}`;
                    setSrc(fileUrl);
                    return;
                }
                setSrc(null);
                return;
            }

            try {
                // вызываем main через preload
                const res = await window.electronAPI.getTrackForPlayback(idOrUrl);
                if (cancelled) return;

                if (res && res.local && res.filepath) {
                    // локальный файл есть — воспроизводим его
                    const fileUrl = `file://${res.filepath.replace(/\\/g, '/')}`;
                    setSrc(fileUrl);
                } else if (res && res.metadata && res.metadata.url) {
                    // нет локального файла, но есть URL в metadata — стримим URL
                    setSrc(res.metadata.url);
                    // опционально: запустить фоновое сохранение (не ждём)
                    window.electronAPI.saveTrackFromUrl(res.metadata.url, res.metadata).catch(e => {
                        console.warn('Background save failed', e);
                    });
                } else {
                    // нет записи — если currentSong содержит прямой URL, используем его
                    const possibleUrl = getMusicUrl(currentSong.id);
                    if (possibleUrl && typeof possibleUrl === 'string') {
                        setSrc(possibleUrl);
                        // и опционально сохранить
                        window.electronAPI.saveTrackFromUrl(possibleUrl, { title: currentSong.title }).catch(() => {});
                    } else {
                        setSrc(null);
                    }
                }
            } catch (err) {
                console.error('Error resolving track source', err);
                // fallback: если есть filePath — пробуем его
                if (currentSong.filePath) {
                    setSrc(`file://${currentSong.filePath.replace(/\\/g, '/')}`);
                } else {
                    setSrc(null);
                }
            }
        }

        resolveSource();
        return () => { cancelled = true; };
    }, [currentSong]);

    useEffect(() => {
        const audio = audioRef.current;
        if (!audio) return;

        const handleEnded = () => {
            console.log('Песня закончилась');
            next();
        };

        audio.addEventListener("timeupdate", handleTimeUpdate);
        audio.addEventListener("loadedmetadata", handleLoadedMetadata);
        audio.addEventListener('ended', handleEnded);

        return () => {
            audio.removeEventListener('ended', handleEnded);
            audio.removeEventListener("timeupdate", handleTimeUpdate);
            audio.removeEventListener("loadedmetadata", handleLoadedMetadata);
        };
    }, [next, duration, handleLoadedMetadata, handleTimeUpdate]);

    useEffect(() => {
        if (currentSong && audioRef.current) {
            // если src изменился, audio.load() будет вызван автоматически при изменении src prop
            audioRef.current.load();
            audioRef.current.play().catch(e => {
                console.log("Autoplay prevented or error:", e);
            });
            setIsPlaying(true);
        }
    }, [currentSong, src]);

    if (!currentSong || !currentSong.filePath || !currentSong.title) return null;

    return (
        <Box padding={1}
        sx = {{
            display: "flex",
            flexDirection: "row"
        }}>
            <Box sx={{
                display: "flex",
                flexDirection: "column",
                justifyContent: "center",
                alignItems: "center",
                width: '25%'// по горизонтали центрируем содержимое
            }}>
                <Typography variant="h7" >{currentSong.title}</Typography>
                <Typography variant="body2" sx={{color: '#A0A0A0'}}>{currentSong.artists.join(", ")}</Typography>
            </Box>
            <Box sx={{
                width: '50%',
                justifyContent: "center",
            }}>
                <Box sx={{
                    display: "flex",
                    justifyContent: "center",
                    gap: 2
                }}>
                    <IconButton color="primary" onClick={previous}>
                        <SkipPrevious fontSize="medium" />
                    </IconButton>
                    <IconButton color="primary" onClick={togglePlay}>
                        {isPlaying ? <Pause fontSize="medium" /> : <PlayArrow fontSize="medium" />}
                    </IconButton>
                    <IconButton color="primary" onClick={next}>
                        <SkipNext fontSize="medium" />
                    </IconButton>
                </Box>
                <Box sx={{
                    display: "flex",
                    position: "relative",
                    justifyContent: "center"
                }}>
                    <Slider
                        size="small"
                        value={progress}
                        onChange={handleSliderChange}
                        sx={{
                            height: 4,
                            width: "70%",
                            maxWidth: "500px",
                            color: "primary.main" }} // или любой нужный размер
                    />
                </Box>
                <audio  ref={audioRef} src={src}
                        preload="metadata"/>
            </Box>
            <Stack
                spacing={2}
                direction="row"
                sx={{
                    width: '25%',
                    alignItems: "center",
                    justifyContent: "center",
                }}
            >
                <IconButton onClick={toggleMute}>
                    {muted || volume === 0 ? <VolumeOffIcon /> : <VolumeUpIcon />}
                </IconButton>

                <Slider
                    min={0}
                    max={1}
                    step={0.01}
                    value={muted ? 0 : volume}
                    onChange={handleVolumeChange}
                    sx={{ width: 150 }}
                />
            </Stack>
        </Box>
    );
}
