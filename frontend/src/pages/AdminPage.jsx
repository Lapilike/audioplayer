import {Box} from "@mui/material";
import {Button} from "@mui/material";
import axios from "axios";
import {useNotification} from "../components/NotificationProvider";


export default function AdminPage() {
    const { showNotification } = useNotification();

    const handleClick = async () => {
        try {
            await axios.get('/api/v1/music/parse');
        } catch (err) {
            console.error(err);
        }
    };

    const rename = async () => {
        try {
            const response = await axios.get('/api/v1/music/change');
            setData(response.data);
            setError('');
        } catch (err) {
            setError('Ошибка при загрузке данных');
            console.error(err);
        }
    };

    return <Box sx={{
        alignSelf: 'center',
        padding: 10 + "px",
        height: '100px',
        width: '200px'
    }}>
        <Button variant="contained" onClick={handleClick}>
            Update DB
        </Button>
        <p/>
        <Button variant="contained" onClick={rename}>
            notify
        </Button>
        <p/>
        <Button variant="contained" onClick={() => {showNotification("jdasjjdajdl")}}>
            notify
        </Button>
    </Box>
}