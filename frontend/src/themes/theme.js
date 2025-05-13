import { createTheme } from '@mui/material/styles';

export const darkTheme = createTheme({
    palette: {
        mode: 'dark', // Включаем темный режим
        background: {
            default: '#121212',  // Цвет фона всего приложения
            paper: '#232323',    // Цвет фона для карт и других компонентов
        },
        text: {
            primary: '#ffffff',  // Основной цвет текста
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

export const neonTheme = (accentColor: 'gre en' | 'blue' | 'purple') => {
    const accentMap = {
        green: '#52ff7e',
        blue: '#6ee7ff',
        purple: '#c084fc',
    };

    const neon = accentMap[accentColor];

    return createTheme({
        palette: {
            mode: 'dark',
            background: {
                default: '#1a1a1a',
                paper: '#1e1e2f',
            },
            text: {
                primary: '#f0f0f0',
                secondary: '#b0b0b0',
            },
            primary: {
                main: neon,
            },
        },
        components: {
            MuiPaper: {
                styleOverrides: {
                    root: {
                        backgroundImage: 'none',
                    },
                },
            },
            MuiCard: {
                styleOverrides: {
                    root: {
                        backgroundColor: '#1e1e2f',
                        borderRadius: '12px',
                        border: '1px solid transparent',
                        backgroundClip: 'padding-box',
                        backgroundImage: `linear-gradient(#1e1e2f, #1e1e2f), linear-gradient(45deg, ${neon}, ${neon})`,
                        backgroundOrigin: 'border-box',
                        boxShadow: `0 0 10px ${neon}33`,
                        transition: 'box-shadow 0.3s ease, border-color 0.3s ease',
                        '&:hover': {
                            boxShadow: `0 0 14px ${neon}`,
                        },
                    },
                },
            },
        },
    });
};