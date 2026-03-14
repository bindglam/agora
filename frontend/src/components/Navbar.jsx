import React, { useState, useEffect } from 'react';
import './Navbar.css';

function Navbar() {
  const [scrolled, setScrolled] = useState(false);

  useEffect(() => {
    const handleScroll = () => {
      const offset = window.scrollY;
      if (offset > 50) {
        setScrolled(true);
      } else {
        setScrolled(false);
      }
    };

    window.addEventListener('scroll', handleScroll);

    return () => {
      window.removeEventListener('scroll', handleScroll);
    };
  }, []);

  return (
    <nav className={`navbar ${scrolled ? 'navbar-scrolled' : ''}`}>
      <div className="navbar-container">
        <div className="navbar-logo">
          <h1>아고라</h1>
          <span className="navbar-subtitle">3학년 2반</span>
        </div>
        <ul className="nav-links">
          <li><a href="/">홈</a></li>
          <li><a href="/notice">공지사항</a></li>
          <li><a href="/board">자유게시판</a></li>
          <li><a href="/schedule">학급일정</a></li>
          <li><a href="/timetable">시간표</a></li>
        </ul>
      </div>
    </nav>
  );
}

export default Navbar;
