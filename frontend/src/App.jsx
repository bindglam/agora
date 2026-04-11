import { BrowserRouter, Routes, Route } from "react-router-dom";
import { useState, useEffect } from "react";

import { setUserKeyInCookie, resetUserKeyInCookie, getCurrentUser, createUser } from './api/user';
import { areCookiesEnabled } from './utils';

import './App.css';

import Home from "./pages/Home";
import Board from "./pages/Board";
import Post from "./pages/Post";
import School from "./pages/School";
import PerformanceAssessments from "./pages/PerformanceAssessments";

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
    // 쿠키 허용 여부 먼저 확인
    if (!areCookiesEnabled()) {
      alert('쿠키가 비활성화되어 있습니다. 이 웹사이트를 이용하기 위해서는 쿠키를 허용해야 합니다.');
      return;
    }

    getCurrentUser().then((user) => {
      if(user === null) {
        resetUserKeyInCookie();
        setIsUserInfoModalOpen(true);
      }
    });
  }, []);

  const handleUserInfoSubmit = async (username) => {
    try {
      const response = await createUser(username);

      setUserKeyInCookie(response.key);

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
            <Route path="/school" element={<School />} />
            <Route path="/assessments" element={<PerformanceAssessments />} />
          </Routes>
        </div>
        <Footer />
      </BrowserRouter>
    </div>
  );
}

export default App;
