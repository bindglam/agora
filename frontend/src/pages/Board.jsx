import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { API_BASE_URL } from '../constants';
import { getCookie } from '../utils';

import './Board.css';
import WritePostModal from '../components/WritePostModal';

function Board() {
  const { boardId } = useParams();
  const navigate = useNavigate();
  const [boardProperties, setBoardProperties] = React.useState('');
  const [posts, setPosts] = React.useState([]);
  const [loading, setLoading] = React.useState(true);
  const [error, setError] = React.useState(null);
  const [isWriteModalOpen, setIsWriteModalOpen] = React.useState(false);
  const [page, setPage] = React.useState(0);
  const [pageSize, setPageSize] = React.useState(10);
  const [totalCount, setTotalCount] = React.useState(0);

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

        const totalPostsResponse = await fetch(`${API_BASE_URL}/post-api/total-posts/${boardId}`);
        if (!totalPostsResponse.ok) {
          throw new Error('게시글을 불러오는데 실패했습니다.');
        }
        setTotalCount(parseInt(await totalPostsResponse.text()));

        const postsResponse = await fetch(`${API_BASE_URL}/post-api/posts/${boardId}?page=${page}&size=${pageSize}`);
        if (!postsResponse.ok) {
          throw new Error('게시글을 불러오는데 실패했습니다.');
        }
        const postsData = await postsResponse.json();
        setPosts(postsData.content || postsData);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    if (boardId) {
      fetchInformation();
    }
  }, [boardId, page, pageSize]);

  const handleCreatePost = () => {
    setIsWriteModalOpen(true);
  };

  const handleWriteModalClose = () => {
    setIsWriteModalOpen(false);
  };

  const handleWriteSubmit = async (postData) => {
    const username = getCookie('username');

    if (!username) {
      alert('유저 정보 등록이 필요합니다.');
      return;
    }

    try {
      const responseRaw = await fetch(`${API_BASE_URL}/post-api/post`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          ...postData,
          boardId: boardId,
          author: username,
        }),
      });

      if (!responseRaw.ok) {
        throw new Error('게시글 작성에 실패했습니다.');
      }

      const response = await responseRaw.json();

      if(response.censorResult.good === false) {
        alert('게시글에 부적절한 내용이 포함되어 있습니다. 수정 후 다시 시도해주세요.\n사유: ' + response.censorResult.reason);
      } else {
        setPage(0);
      }
    } catch (err) {
      throw err;
    }
  };

  const handlePostClick = (postId) => {
    navigate(`/post/${postId}`);
  };

  const handlePageChange = (newPage) => {
    setPage(newPage);
  };

  const handlePreviousPage = () => {
    if (page > 0) {
      setPage(page - 1);
    }
  };

  const handleNextPage = () => {
    const totalPages = Math.ceil(totalCount / pageSize);
    if (page < totalPages - 1) {
      setPage(page + 1);
    }
  };

  if (loading) {
    return <div className="board-container">로딩 중...</div>;
  }

  if (error) {
    return <div className="board-container error">{error}</div>;
  }

  return (
    <div className="board-container">
      <WritePostModal
        isOpen={isWriteModalOpen}
        onClose={handleWriteModalClose}
        onSubmit={handleWriteSubmit}
        boardName={boardProperties.displayName}
      />

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
        <>
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
              {posts.filter((post) => post.public === true).map((post) => (
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

          {totalCount > 0 && (
            <div className="pagination">
              <button
                className="pagination-btn"
                onClick={handlePreviousPage}
                disabled={page === 0}
              >
                이전
              </button>
              <span className="pagination-info">
                {page + 1} / {Math.max(1, Math.ceil(totalCount / pageSize))}
              </span>
              <button
                className="pagination-btn"
                onClick={handleNextPage}
                disabled={page >= Math.ceil(totalCount / pageSize) - 1}
              >
                다음
              </button>
            </div>
          )}
        </>
      )}
    </div>
  );
}

export default Board;