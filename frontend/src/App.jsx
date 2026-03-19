import { BrowserRouter, Routes, Route } from "react-router-dom";
import { useState, useEffect } from "react";
import { getCookie, setCookie } from './utils';

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

    // 초기 계산
    calculateNavbarHeight();

    // resize 이벤트 리스너 추가
    window.addEventListener('resize', calculateNavbarHeight);

    // 컴포넌트 언마운트 시 리스너 제거
    return () => {
      window.removeEventListener('resize', calculateNavbarHeight);
    };
  }, []);

  useEffect(() => {
    const username = getCookie('username');
    if (!username) {
      setIsUserInfoModalOpen(true);
    }
  }, []);

  const handleUserInfoSubmit = (username) => {
    setCookie('username', username, 365); // 1년 동안 쿠키 유지
    setIsUserInfoModalOpen(false);
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
