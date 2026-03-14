import React from 'react';
import './Footer.css';

function Footer() {
  return (
    <footer className="footer">
      <div className="footer-container">
        <div className="footer-content">
          <div className="footer-brand">
            <h3>아고라</h3>
            <span className="footer-subtitle">3학년 2반 학급 웹사이트</span>
          </div>
          <div className="footer-divider"></div>
          <div className="footer-credits">
            <p>개발자: 전우빈</p>
            <p>&copy; 2025 아고라 3-2. All rights reserved.</p>
          </div>
        </div>
      </div>
    </footer>
  );
}

export default Footer;
