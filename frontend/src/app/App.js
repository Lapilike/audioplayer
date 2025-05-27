import React, { useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from "../pages/HomePage";
import AdminPage from "../pages/AdminPage";
import Layout from "../layout/Layout";
import PlaylistPage from "../pages/PlaylistPage";

function App() {
    useEffect(() => {
    console.log('Компонент загрузился');
  }, []);

  return (
      <>
      <div>

          <Router>
              <Routes>
                  <Route path="/" element={<Layout />}>
                      <Route index element={<HomePage />} />
                      <Route path="playlist" element={<PlaylistPage />}/>
                      <Route path="admin" element={<AdminPage />} />
                  </Route>
              </Routes>
          </Router>

      </div>
      </>
  );
}

export default App;
