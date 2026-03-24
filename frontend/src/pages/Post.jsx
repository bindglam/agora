import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';

import { getUsernameInCookie } from '../api/user';
import { getPost } from '../api/post';

import './Post.css';

function Post() {
  const { postId } = useParams();
  const navigate = useNavigate();

  const [loading, setLoading] = React.useState(true);
  const [error, setError] = React.useState(null);

  const [post, setPost] = React.useState(null);

  React.useEffect(() => {
    const fetchPost = async () => {
      try {
        setLoading(true);
        const post = await getPost(getUsernameInCookie(), postId);
        setPost(post);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    if (postId) {
      fetchPost();
    }
  }, [postId]);

  const handleGoBack = () => {
    navigate(-1);
  };

  const handleGoToBoard = () => {
    if (post?.boardId) {
      navigate(`/board/${post.boardId}`);
    }
  };

  if (loading) {
    return <div className="post-container">로딩 중...</div>;
  }

  if (error) {
    return <div className="post-container error">{error}</div>;
  }

  if (!post) {
    return <div className="post-container">게시글을 찾을 수 없습니다.</div>;
  }

  return (
    <div className="post-container">
      <div className="post-header">
        <button className="back-btn" onClick={handleGoBack}>← 뒤로</button>
        {post.boardId && (
          <button className="list-btn" onClick={handleGoToBoard}>목록</button>
        )}
      </div>

      <div className="post-content">
        <h1 className="post-title">{post.title}</h1>

        <div className="post-meta">
          <span className="post-author">작성자: {post.author.username}</span>
          <span className="post-date">
            작성일: {new Date(post.createdAt).toLocaleString()}
          </span>
        </div>

        <div className="post-body">
          {post.content}
        </div>
      </div>
    </div>
  );
}

export default Post;
