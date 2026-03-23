import { BrowserRouter, Routes, Route } from "react-router-dom";
import { useState, useEffect } from "react";

import { getUsernameInCookie, setUsernameInCookie, resetUsernameInCookie, getCurrentUser, createUser } from './api/user';

import './App.css';

import Home from "./pages/Home";
import Board from "./pages/Board";
import Post from "./pages/Post";

import Navbar from './components/Navbar';
import Footer from './components/Footer';
import UserInfoModal from './components/UserInfoModal';

function App() {
  const [navbarHeight, setNavbarHeight] = useState(0);
  const [isUserInfoModalOpen, setIsUserInfoModalOpen] = useState(false);

  useEffect(() => {
    const calculateNavbarHeight = () => {
      const navbar = document.querySelector('.navbar');
      if (navbar) {
        setNavbarHeight(navbar.offsetHeight);
      }
    };

    calculateNavbarHeight();

    window.addEventListener('resize', calculateNavbarHeight);

    return () => {
      window.removeEventListener('resize', calculateNavbarHeight);
    };
  }, []);

  useEffect(() => {
    getCurrentUser().then((user) => {
      if(user === null) {
        resetUsernameInCookie();
        setIsUserInfoModalOpen(true);
      }
    });
  }, []);

  const handleUserInfoSubmit = async (username) => {
    try {
      const response = await createUser(username);

      setUsernameInCookie(username);

      setIsUserInfoModalOpen(false);
    } catch (err) {
      alert(err.message);
    }
  };

  return (
    <div className="App">
      <BrowserRouter>
        <UserInfoModal isOpen={isUserInfoModalOpen} onSubmit={handleUserInfoSubmit} />
        <Navbar />
        <div className="main-content" style={{ paddingTop: `${navbarHeight + 40}px` }}>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/board/:boardId" element={<Board />} />
            <Route path="/post/:postId" element={<Post />} />
          </Routes>
        </div>
        <Footer />
      </BrowserRouter>
    </div>
  );
}

export default App;
