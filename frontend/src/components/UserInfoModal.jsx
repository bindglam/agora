import React from 'react';

import './UserInfoModal.css';

function UserInfoModal({ isOpen, onSubmit }) {
  const [username, setUsername] = React.useState('');

  if (!isOpen) return null;

  const handleSubmit = (e) => {
    e.preventDefault();
    if (username.trim()) {
      onSubmit(username.trim());
      setUsername('');
    }
  };

  const handleChange = (e) => {
    setUsername(e.target.value);
  };

  return (
    <div className="username-modal-overlay">
      <div className="username-modal" onClick={(e) => e.stopPropagation()}>
        <h2 className="username-modal-title">사용자 이름 입력</h2>
        <p className="username-modal-description">
          안녕하세요! Agora에 오신 것을 환영합니다. 원활한 커뮤니케이션을 위해 사용자 이름을 입력해주세요.
        </p>
        <form onSubmit={handleSubmit} className="username-modal-form">
          <input
            type="text"
            value={username}
            onChange={handleChange}
            placeholder="사용자 이름"
            className="username-modal-input"
            autoFocus
          />
          <div className="username-modal-buttons">
            <button
              type="submit"
              className="username-modal-submit"
              disabled={!username.trim()}
            >
              확인
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default UserInfoModal;
