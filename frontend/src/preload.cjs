// preload.cjs
const { contextBridge, ipcRenderer } = require('electron');

contextBridge.exposeInMainWorld('electronAPI', {
    getTrackForPlayback: (idOrUrl) => ipcRenderer.invoke('get-track-for-playback', idOrUrl),
    saveTrackFromUrl: (url, meta) => ipcRenderer.invoke('save-track-from-url', url, meta),
    listTracks: () => ipcRenderer.invoke('list-tracks'),
    removeTrack: (id) => ipcRenderer.invoke('remove-track', id),
    updateProgress: (id, progress) => ipcRenderer.invoke('update-progress', id, progress),
    send: (channel, data) => {
        // whitelist channels if нужно
        ipcRenderer.send(channel, data);
    },
    on: (channel, callback) => {
        ipcRenderer.on(channel, (event, ...args) => callback(...args));
    }

});