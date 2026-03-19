import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { API_BASE_URL } from '../constants';

import './Board.css';

function Board() {
  const { boardId } = useParams();
  const navigate = useNavigate();
  const [boardProperties, setBoardProperties] = React.useState('');
  const [posts, setPosts] = React.useState([]);
  const [loading, setLoading] = React.useState(true);
  const [error, setError] = React.useState(null);

  React.useEffect(() => {
    const fetchInformation = async () => {
      try {
        setLoading(true);

        const boardResponse = await fetch(`${API_BASE_URL}/board-api/board/${boardId}`);
        if (!boardResponse.ok) {
          throw new Error('게시판 정보를 불러오는데 실패했습니다.');
        }
        const boardData = await boardResponse.json();
        setBoardProperties(boardData.properties);

        const postsResponse = await fetch(`${API_BASE_URL}/post-api/posts/${boardId}`);
        if (!postsResponse.ok) {
          throw new Error('게시글을 불러오는데 실패했습니다.');
        }
        const postsData = await postsResponse.json();
        setPosts(postsData);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    if (boardId) {
      fetchInformation();
    }
  }, [boardId]);

  const handleCreatePost = () => {
    navigate(`/board/${boardId}/new`);
  };

  const handlePostClick = (postId) => {
    navigate(`/post/${postId}`);
  };

  if (loading) {
    return <div className="board-container">로딩 중...</div>;
  }

  if (error) {
    return <div className="board-container error">{error}</div>;
  }

  return (
    <div className="board-container">
      <div className="board-header">
        <h1 className="board-title">{boardProperties.displayName}</h1>
        {boardProperties.permissions.posting == "DEFAULT" &&
          <button className="create-post-btn" onClick={handleCreatePost}>
          글쓰기
          </button>
        }
      </div>

      {posts.length === 0 ? (
        <div className="no-posts">게시글이 없습니다.</div>
      ) : (
        <table className="board-table">
          <thead>
            <tr>
              <th className="col-title">제목</th>
              <th className="col-content">내용</th>
              <th className="col-author">작성자</th>
              <th className="col-date">작성일</th>
              {/*<th className="col-views">조회</th>*/}
            </tr>
          </thead>
          <tbody>
            {posts.map((post) => (
              <tr
                key={post.id}
                className="board-row"
                onClick={() => handlePostClick(post.id)}
              >
                <td className="col-title">{post.title}</td>
                <td className="col-content">{post.content}</td>
                <td className="col-author">{post.author}</td>
                <td className="col-date">
                  {new Date(post.createdAt).toLocaleDateString()}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default Board;