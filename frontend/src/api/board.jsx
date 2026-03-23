import { API_BASE_URL } from '../constants';

export const getBoard = async (boardId) => {
  const responseRaw = await fetch(`${API_BASE_URL}/board-api/board/${boardId}`);
  if (!responseRaw.ok) {
    throw new Error('게시판 정보를 불러오는데 실패했습니다.');
  }
  return await responseRaw.json();
};

export const getPosts = async (boardId, page, pageSize) => {
  const responseRaw = await fetch(`${API_BASE_URL}/post-api/posts/${boardId}?page=${page}&size=${pageSize}`);
  if (!responseRaw.ok) {
    throw new Error('게시글을 불러오는데 실패했습니다.');
  }
  return await responseRaw.json();
};
