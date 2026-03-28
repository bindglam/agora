import { API_BASE_URL } from '../constants';

export const getTimetable = async (date) => {
  const responseRaw = await fetch(`${API_BASE_URL}/school-api/timetable?date=${date}`);
  if (!responseRaw.ok) {
    throw new Error('시간표를 불러오는데 실패했습니다.');
  }
  return await responseRaw.json();
};

export const getMeal = async (date) => {
  const responseRaw = await fetch(`${API_BASE_URL}/school-api/mealmenu?date=${date}`);
  if (!responseRaw.ok) {
    throw new Error('급식표를 불러오는데 실패했습니다.');
  }
  return await responseRaw.json();
};
