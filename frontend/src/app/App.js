import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from "../components/Navbar";
import HomePage from "../pages/HomePage";
import {ThemeProvider} from "@mui/material";
import {darkTheme} from "../themes/theme";
import AdminPage from "../pages/AdminPage";

function App() {

  useEffect(() => {
    console.log('Компонент загрузился');
  }, []);

  return (
      <>
      <div>

          <Router>
                  <Navbar/>
                  <Routes>
                      <Route path={"/"} element={<HomePage/>} />
                      <Route path={"/admin"} element={<AdminPage/>} />
                  </Routes>
          </Router>

      </div>
      </>
  );
}

export default App;
