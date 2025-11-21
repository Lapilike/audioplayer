// src/index.js
import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './app/App';
import reportWebVitals from './reportWebVitals';
import { ThemeProvider } from '@mui/material';
import { darkTheme } from './themes/theme';
import { CssBaseline } from '@mui/material';
import { NotificationProvider } from './components/NotificationProvider';
import axios from 'axios';
import { HashRouter } from 'react-router-dom';

axios.defaults.withCredentials = true;

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <NotificationProvider>
            <ThemeProvider theme={darkTheme}>
                <CssBaseline />
                <HashRouter>
                    <App />
                </HashRouter>
            </ThemeProvider>
        </NotificationProvider>
    </React.StrictMode>
);

reportWebVitals();
