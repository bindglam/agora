import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import ReactMarkdown from 'react-markdown';

import { getPost, createComment } from '../api/post';

import './Post.css';

function Post() {
  const { postId } = useParams();
  const navigate = useNavigate();

  const [loading, setLoading] = React.useState(true);
  const [error, setError] = React.useState(null);

  const [post, setPost] = React.useState(null);
  const [commentContent, setCommentContent] = React.useState('');
  const [submittingComment, setSubmittingComment] = React.useState(false);

  React.useEffect(() => {
    const fetchPost = async () => {
      try {
        setLoading(true);
        const post = await getPost(postId);
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

  const handleCommentSubmit = async (e) => {
    e.preventDefault();

    if (!commentContent.trim()) {
      alert('댓글 내용을 입력해주세요.');
      return;
    }

    try {
      setSubmittingComment(true);
      const response = await createComment(postId, commentContent);
      if (response.comment) {
        setCommentContent('');
        // 게시글을 다시 불러와서 댓글 목록 갱신
        const updatedPost = await getPost(postId);
        setPost(updatedPost);
      }
    } catch (err) {
      alert(err.message);
    } finally {
      setSubmittingComment(false);
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
          <ReactMarkdown>{post.content}</ReactMarkdown>
        </div>
      </div>

      {/* 댓글 섹션 */}
      <div className="comments-section">
        <h2 className="comments-title">댓글 ({post.comments?.length || 0})</h2>

        {/* 댓글 작성 폼 */}
        <form className="comment-form" onSubmit={handleCommentSubmit}>
          <textarea
            className="comment-input"
            value={commentContent}
            onChange={(e) => setCommentContent(e.target.value)}
            placeholder="댓글을 입력하세요..."
            rows={3}
          />
          <button
            type="submit"
            className="comment-submit-btn"
            disabled={submittingComment || !commentContent.trim()}
          >
            {submittingComment ? '작성 중...' : '댓글 작성'}
          </button>
        </form>

        {/* 댓글 목록 */}
        <div className="comments-list">
          {post.comments && post.comments.length > 0 ? (
            post.comments.map((comment) => (
              <div key={comment.id} className="comment-item">
                <div className="comment-header">
                  <span className="comment-author">{comment.author.username}</span>
                  <span className="comment-date">
                    {new Date(comment.createdAt).toLocaleString()}
                  </span>
                </div>
                <div className="comment-content">{comment.content}</div>
              </div>
            ))
          ) : (
            <div className="no-comments">댓글이 없습니다. 첫 번째 댓글을 작성해보세요!</div>
          )}
        </div>
      </div>
    </div>
  );
}

export default Post;
