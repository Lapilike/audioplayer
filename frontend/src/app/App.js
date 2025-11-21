import React, { useEffect } from 'react';
import { Routes, Route } from 'react-router-dom';
import HomePage from "../pages/HomePage";
import AdminPage from "../pages/AdminPage";
import Layout from "../layout/Layout";
import PlaylistPage from "../pages/PlaylistPage";
import SongPage from "../pages/SongPage";

function App() {
    useEffect(() => {
    console.log('Компонент загрузился');
  }, []);

  return (
      <>
      <div>
              <Routes>
                  <Route path="/" element={<Layout />}>
                      <Route index element={<HomePage />} />
                      <Route path="playlist" element={<PlaylistPage />}/>
                      <Route path="admin" element={<AdminPage />} />
                      <Route path="mysongs" element={<SongPage />} />
                  </Route>
              </Routes>

      </div>
      </>
  );
}

export default App;
