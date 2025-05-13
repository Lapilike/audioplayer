// SearchBar.tsx
import React, { useState } from 'react';
import { TextField, Button, Box } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import {IconButton} from "@mui/material";
import {InputAdornment} from "@mui/material";

interface SearchBarProps {
    onSearch: (query: string) => void;
}

const SearchBar: React.FC<SearchBarProps> = ({ onSearch }) => {
    const [searchQuery, setSearchQuery] = useState('');

    const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setSearchQuery(event.target.value);
    };

    const handleSearchSubmit = () => {
        onSearch(searchQuery); // Передаем значение в родительский компонент
        setSearchQuery(''); // Очистка поля после поиска
    };

    const handleSearch = () => {
        onSearch(searchQuery);
    };

    return (
        <Box sx={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            width: '100%'
        }}>
            <TextField
                placeholder="Want to find something new?"
                variant="outlined"
                value={searchQuery}
                onChange={handleSearchChange}
                sx={{
                    width: '50%',
                    marginRight: 2,
                    '& .MuiOutlinedInput-root': {
                        borderRadius: '15px', // Скругленные углы для поля ввода
                        height: '40px', // Уменьшаем высоту для поля ввода
                    },
                }}
                onKeyDown={(e) => {
                    if (e.key === 'Enter') handleSearch();
                }}
                InputProps = {{
                    endAdornment: (
                        <InputAdornment position="end">
                            <IconButton onClick={handleSearch}
                                        edge="end"
                                        sx={{
                                            height: '30px',
                                            width: '30px',
                                            right: '3px',
                                            color: 'inherit', // наследует цвет от текста
                                            '&:hover': {
                                                borderRadius: '15px'// убираем подсветку при наведении
                                            },
                                            '&:focus': {
                                                backgroundColor: 'transparent', // убираем подсветку при фокусе
                                            },
                                            '&:active': {
                                                backgroundColor: 'transparent', // убираем при клике
                                            }
                                        }}>
                                <SearchIcon/>
                            </IconButton>
                        </InputAdornment>
                    )}}
            />
        </Box>
    );
};

export default SearchBar;