import React from 'react';

import './WritePostModal.css';

function WritePostModal({ isOpen, onClose, onSubmit, boardName }) {
  const [title, setTitle] = React.useState('');
  const [content, setContent] = React.useState('');
  const [isSubmitting, setIsSubmitting] = React.useState(false);

  if (!isOpen) return null;

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!title.trim() || !content.trim()) {
      alert('제목과 내용을 모두 입력해주세요.');
      return;
    }

    setIsSubmitting(true);
    try {
      await onSubmit({
        title: title.trim(),
        content: content.trim(),
      });
      setTitle('');
      setContent('');
      onClose();
    } catch (error) {
      alert('게시글 작성에 실패했습니다.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleClose = () => {
    if (!isSubmitting) {
      setTitle('');
      setContent('');
      onClose();
    }
  };

  return (
    <div className="write-post-modal-overlay" onClick={handleClose}>
      <div className="write-post-modal" onClick={(e) => e.stopPropagation()}>
        <div className="write-post-modal-header">
          <h2 className="write-post-modal-title">
            {boardName || '게시판'} - 글쓰기
          </h2>
          <button className="close-btn" onClick={handleClose} disabled={isSubmitting}>
            ×
          </button>
        </div>

        <form onSubmit={handleSubmit} className="write-post-modal-form">
          <div className="form-group">
            <label htmlFor="title">제목</label>
            <input
              type="text"
              id="title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="제목을 입력하세요"
              className="form-input"
              maxLength={100}
              disabled={isSubmitting}
            />
            <span className="char-count">{title.length}/100</span>
          </div>

          <div className="form-group">
            <label htmlFor="content">내용</label>
            <textarea
              id="content"
              value={content}
              onChange={(e) => setContent(e.target.value)}
              placeholder="내용을 입력하세요"
              className="form-textarea"
              rows={10}
              disabled={isSubmitting}
            />
          </div>

          <div className="write-post-modal-footer">
            <button
              type="button"
              onClick={handleClose}
              className="cancel-btn"
              disabled={isSubmitting}
            >
              취소
            </button>
            <button
              type="submit"
              className="submit-btn"
              disabled={isSubmitting || !title.trim() || !content.trim()}
            >
              {isSubmitting ? '작성 중...' : '작성'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default WritePostModal;
