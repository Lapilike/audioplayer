import { app, ipcMain, BrowserWindow } from 'electron';
import fs from 'fs/promises';
import { pipeline } from 'stream/promises';
import { createWriteStream } from 'fs';
import Store from 'electron-store';

import path from 'path';
import { fileURLToPath } from 'url';
import axios from "axios";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

async function ensureAudioDir() {
    await fs.mkdir(audioDir, { recursive: true });
}

function findTrackRecord(trackIdOrUrl) {
    const tracks = store.get('tracks', []);
    return tracks.find(t => t.id === trackIdOrUrl || t.url === trackIdOrUrl || t.filename === trackIdOrUrl);
}

async function fileExists(filePath) {
    try {
        await fs.access(filePath);
        return true;
    } catch {
        return false;
    }
}

// Хендлер: вернуть локальный путь или метаданные
ipcMain.handle('get-track-for-playback', async (event, trackIdOrUrl) => {
    await ensureAudioDir();
    console.log('track id or url:', trackIdOrUrl);
    const rec = findTrackRecord(trackIdOrUrl);
    if (rec) {
        const exists = await fileExists(rec.filepath);
        if (exists) return { local: true, filepath: rec.filepath, metadata: rec };
        return { local: false, metadata: rec };
    }
    return { local: false, metadata: null };
});

function getTracks() {
    return store.get('tracks', []); // массив записей
}

function saveTracks(arr) {
    store.set('tracks', arr);
}

function createWindow() {
    const win = new BrowserWindow({
        width: 1000,
        height: 700,
        webPreferences: {
            preload: path.join(__dirname, 'preload.cjs'),
            contextIsolation: true,
            nodeIntegration: false
        }
    });

    if (process.env.ELECTRON_DEV === 'true') {
        win.loadURL('http://localhost:3000');
    } else {
        const indexPath = path.join(__dirname, '../build', 'index.html');
        win.loadFile(indexPath).catch(err => console.error('loadFile error:', err));
    }

    win.webContents.openDevTools();
    win.webContents.on('did-fail-load', (e, code, desc, url) => {
        console.error('did-fail-load', code, desc, url);
    });
}

const store = new Store({ name: 'tracks' });
const audioDir = path.join(app.getPath('userData'), 'audio');

ipcMain.handle('save-track-from-url', async (event, url, meta = {}) => {
    await ensureAudioDir();

    console.log('store path:', store.path);
    console.log('tracks count:', store.get('tracks', []).length);
    console.log(store.get('tracks', []));

    // Проверяем, есть ли уже запись с таким URL
    const tracks = getTracks();
    const existing = tracks.find(t => t.url === url || t.filepath === meta.filepath || t.filename === meta.filename);

    if (existing) {
        // Если уже сохранено — обновим метаданные по необходимости и вернём существующую запись
        let changed = false;
        if (meta.title && meta.title !== existing.title) { existing.title = meta.title; changed = true; }
        if (meta.artist && meta.artist !== existing.artist) { existing.artist = meta.artist; changed = true; }
        if (typeof meta.progress === 'number' && meta.progress !== existing.progress) { existing.progress = meta.progress; changed = true; }
        if (changed) saveTracks(tracks);
        return existing;
    }

    // Если нет записи — скачиваем и сохраняем новый файл
    const ext = path.extname(new URL(url).pathname) || '.mp3';
    const filename = meta.filename || `${Date.now()}-${Math.random().toString(36).slice(2,8)}${ext}`;
    const filepath = path.join(audioDir, filename);

    const tmpPath = filepath + '.tmp';
    const res = await axios.get(url, { responseType: 'stream' });
    const writer = createWriteStream(tmpPath);

    try {
        await pipeline(res.data, writer); // потоковая запись
        await fs.rename(tmpPath, filepath); // атомарно переименовать в финальное имя
    } catch (err) {
        // удалить временный файл при ошибке
        try { await fs.unlink(tmpPath); } catch (_) {}
        throw err;
    }

    const record = {
        id: Date.now(),
        url,
        title: meta.title || filename,
        artists: meta.artists || '',
        filename,
        filepath,
        listened_at: new Date().toISOString(),
        progress: meta.progress || 0,
        play_count: 0,
        last_played: null
    };

    tracks.unshift(record);
    saveTracks(tracks);
    return record;
});

ipcMain.handle('list-tracks', () => {
    return store.get('tracks', []);
});

ipcMain.handle('remove-track', async (event, id) => {
    const tracks = store.get('tracks', []);
    const idx = tracks.findIndex(t => t.id === id);
    if (idx === -1) return { ok: false };
    const [rec] = tracks.splice(idx, 1);
    store.set('tracks', tracks);
    try { await fs.unlink(rec.filepath); } catch (e) { /* игнорируем */ }
    return { ok: true };
});

ipcMain.handle('update-progress', (event, id, progress) => {
    const tracks = store.get('tracks', []);
    const rec = tracks.find(t => t.id === id);
    if (!rec) return { ok: false };
    rec.progress = progress;
    store.set('tracks', tracks);
    return { ok: true };
});

app.whenReady().then(createWindow);
