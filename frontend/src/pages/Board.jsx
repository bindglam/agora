import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';

import { getCurrentUser, getUsernameInCookie, UserPermission, hasPermission } from '../api/user';
import { getBoard } from '../api/board';
import { getTotalPosts, getPosts, createPost } from '../api/post';

import './Board.css';
import WritePostModal from '../components/WritePostModal';

const truncateText = (text, maxLength) => {
  if (!text) return text;
  return text.length > maxLength ? text.slice(0, maxLength) + '...' : text;
};

function Board() {
  const { boardId } = useParams();
  const navigate = useNavigate();

  const [loading, setLoading] = React.useState(true);
  const [error, setError] = React.useState(null);

  const [currentUser, setCurrentUser] = React.useState(null);
  const [boardProperties, setBoardProperties] = React.useState('');
  const [posts, setPosts] = React.useState([]);
  const [isWriteModalOpen, setIsWriteModalOpen] = React.useState(false);
  const [page, setPage] = React.useState(0);
  const [pageSize, setPageSize] = React.useState(10);
  const [totalCount, setTotalCount] = React.useState(0);

  React.useEffect(() => {
    const fetchInformation = async () => {
      try {
        setLoading(true);

        setCurrentUser(await getCurrentUser());

        const boardData = await getBoard(boardId);
        setBoardProperties(boardData.properties);

        setTotalCount(await getTotalPosts(boardId));
        setPosts(await getPosts(boardId, page, pageSize));
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
    const username = getUsernameInCookie();
    if (!username) {
      alert('유저 정보 등록이 필요합니다.');
      return;
    }

    try {
      const response = await createPost(postData.title, postData.content, boardId);

      //setPage(0);
      setPosts([response.post, ...posts]);
    } catch (err) {
      alert(err.message);
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
        {hasPermission(UserPermission[currentUser.permission], UserPermission[boardProperties.permissions.posting]) &&
          <button className="create-post-btn" onClick={handleCreatePost}>
          글쓰기
          </button>
        }
      </div>

      {posts.length === 0 ? (
        <div className="no-posts">게시글이 없습니다.</div>
      ) : (
        <>
          <div className="board-table-wrapper">
            <table className="board-table">
            <thead>
              <tr>
                <th className="col-title">제목</th>
                {/*<th className="col-content">내용</th>*/}
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
                  onClick={() => handlePostClick(post.postId)}
                >
                  <td className="col-title"><span className="text-truncate">{truncateText(post.title, 40)}</span></td>
                  {/*<td className="col-content"><span className="text-truncate">{truncateText(post.content, 10)}</span></td>*/}
                  <td className="col-author"><span className="text-truncate">{truncateText(post.author.username, 10)}</span></td>
                  <td className="col-date">
                    {new Date(post.createdAt).toLocaleDateString()}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          </div>
        </>
      )}

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
    </div>
  );
}

export default Board;
