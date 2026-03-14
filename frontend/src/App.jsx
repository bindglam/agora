import { BrowserRouter, Routes, Route } from "react-router-dom";
import { useState, useEffect } from "react";

import './App.css';

import Home from "./components/Home";

import Navbar from './components/Navbar';
import Footer from './components/Footer';

function App() {
  const [navbarHeight, setNavbarHeight] = useState(0);

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

  return (
    <div className="App">
      <BrowserRouter>
        <Navbar />
        <div className="main-content" style={{ paddingTop: `${navbarHeight + 40}px` }}>
          <Routes>
            <Route path="/" element={<Home />} />
          </Routes>
        </div>
        <Footer />
      </BrowserRouter>
    </div>
  );
}

export default App;
