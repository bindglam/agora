import React from 'react';

import './Home.css';

function Home() {
  return (
    <div>
        <div className="home">
            <h1>환영합니다!</h1>
            <p>아고라 3학년 2반 학급 웹사이트에 오신 것을 환영합니다.</p>
            <p>
                공지사항, 자유게시판, 학급일정, 시간표 등 다양한 정보를 확인하실 수 있습니다.
                우리 반 친구들과 소통하고 학급 활동을 함께 만들어가요!
            </p>
            <div style={{ marginTop: '2rem', display: 'flex', gap: '1rem', justifyContent: 'center', flexWrap: 'wrap' }}>
                <a href="/board/announcements" className="btn btn-primary">
                공지사항 보기
                </a>
                <a href="/board/free" className="btn btn-secondary">
                자유게시판
                </a>
            </div>
        </div>
        <div className="card" style={{ marginTop: '3rem', maxWidth: '80%', marginLeft: 'auto', marginRight: 'auto' }}>
            <h2 className="card-title">여기서 무엇을 할 수 있나요?</h2>
            <div className="card-content">
                <p>'아고라'는 과천문원중학교 3학년 2반 학생들을 위한 학급 소통 플랫폼입니다.</p>
                <p>공지사항을 통해 중요한 학급 소식을 확인하고, 자유게시판에서 친구들과 이야기를 나눌 수 있습니다.</p>
                <p>또한, 학급일정과 시간표를 통해 학교 생활을 더욱 편리하게 관리할 수 있습니다.</p>
                <p>우리 반 친구들과 함께 소통하고 즐거운 학교 생활을 만들어가요!</p>
            </div>
        </div>
    </div>
  );
}

export default Home;
