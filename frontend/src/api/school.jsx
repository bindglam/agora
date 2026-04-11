import { API_BASE_URL } from '../constants';
import { getUserKeyInCookie } from './user';

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

export const getPerformanceAssessments = async (date) => {
  const responseRaw = await fetch(`${API_BASE_URL}/school-api/performance-assessments?date=${date}`);
  if (!responseRaw.ok) {
    throw new Error('수행평가를 불러오는데 실패했습니다.');
  }
  return await responseRaw.json();
};

export const createPerformanceAssessment = async (subject, title, description, evaluateAt) => {
  const responseRaw = await fetch(`${API_BASE_URL}/school-api/performance-assessment`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      subject,
      title,
      description,
      evaluateAt,
      uploaderKey: getUserKeyInCookie()
    }),
  });
  if (!responseRaw.ok) {
    throw new Error('수행평가를 생성하는데 실패했습니다.');
  }
  return await responseRaw.json();
};
