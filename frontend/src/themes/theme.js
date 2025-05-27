import { createTheme } from '@mui/material/styles';

export const darkTheme = createTheme({
    palette: {
        mode: 'dark', // Включаем темный режим
        background: {
            default: '#121212',  // Цвет фона всего приложения
            paper: '#232323',    // Цвет фона для карт и других компонентов
        },
        text: {
            fontFamily: 'Rubik, sans-serif',
            primary: '#dddddd',  // Основной цвет текста
            secondary: '#B0B0B0FF',  // Вторичный цвет текста
        },
        primary: {
            main: '#bb86fc',  // Цвет для основных элементов, например кнопок
        },
        secondary: {
            main: '#03dac6',  // Вторичный цвет
        },
    },
});