import { API_BASE_URL } from '../constants';

import { getUsernameInCookie } from './user';

export const getTotalPosts = async (boardId) => {
  const responseRaw = await fetch(`${API_BASE_URL}/post-api/total-posts/${boardId}?user=${getUsernameInCookie()}`);
  if (!responseRaw.ok) {
    throw new Error('게시글을 불러오는데 실패했습니다.');
  }
  return parseInt(await responseRaw.text());
};

export const getPosts = async (boardId, page, pageSize) => {
  const responseRaw = await fetch(`${API_BASE_URL}/post-api/posts/${boardId}?user=${getUsernameInCookie()}&page=${page}&size=${pageSize}`);
  if (!responseRaw.ok) {
    throw new Error('게시글 목록을 불러오는데 실패했습니다.');
  }
  return await responseRaw.json();
};

export const getPost = async (postId) => {
  const responseRaw = await fetch(`${API_BASE_URL}/post-api/post/${postId}?user=${getUsernameInCookie()}`);
  if (!responseRaw.ok) {
    throw new Error('게시글을 불러오는데 실패했습니다.');
  }
  return await responseRaw.json();
};

export const createPost = async (title, content, boardId) => {
  const responseRaw = await fetch(`${API_BASE_URL}/post-api/post`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      title: title,
      content: content,
      boardId: boardId,
      authorUsername: getUsernameInCookie(),
    }),
  });
  if (!responseRaw.ok) {
    throw new Error('게시글을 작성하는데 실패했습니다.');
  }
  
  const response = await responseRaw.json();

  if(response.errorMessage) {
    throw new Error('게시글 작성에 실패했습니다. 사유: ' + response.errorMessage);
  }

  if(response.censorResult && response.censorResult.good === false) {
    throw new Error('게시글에 부적절한 내용이 포함되어 있습니다. 수정 후 다시 시도해주세요.\n사유: ' + response.censorResult.reason);
  }

  return response;
};
