import {Box} from "@mui/material";
import {Button} from "@mui/material";
import axios from "axios";
import {useState} from "react";

export default function AdminPage() {
    const [data, setData] = useState(null);
    const [error, setError] = useState('');

    const handleClick = async () => {
        try {
            const response = await axios.get('/api/v1/music/parse');
            setData(response.data);
            setError('');
        } catch (err) {
            setError('Ошибка при загрузке данных');
            console.error(err);
        }
    };

    const attach = async () => {
        try {
            const response = await axios.get('/api/v1/music/attach');
            setData(response.data);
            setError('');
        } catch (err) {
            setError('Ошибка при загрузке данных');
            console.error(err);
        }
    };

    return (
        <Box sx={{
            alignSelf: 'center',
            padding: 10 + "px",
            height: '100px',
            width: '200px'
        }}>
            <Button variant="contained" onClick={handleClick}>
                Update DB
            </Button>
            <p/>
            <Button variant="contained" onClick={attach}>
                Update connection
            </Button>
        </Box>
    )
}