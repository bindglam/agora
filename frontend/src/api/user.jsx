import { API_BASE_URL } from '../constants';
import { getCookie, setCookie } from '../utils';

export const USERNAME_COOKIE_KEY = 'username';

export const getUsernameInCookie = () => {
  return getCookie(USERNAME_COOKIE_KEY);
};

export const setUsernameInCookie = (username) => {
  setCookie(USERNAME_COOKIE_KEY, username, 365);
};

export const resetUsernameInCookie = () => {
  setCookie(USERNAME_COOKIE_KEY, '', -1);
};

export const getUser = async (username) => {
  const responseRaw = await fetch(`${API_BASE_URL}/user-api/user/${username}`);
  if (!responseRaw.ok) {
    return null;
  }
  const response = await responseRaw.json();
  return response;
};

export const getCurrentUser = async () => {
  const username = getUsernameInCookie();
  if(username === null) {
    return null;
  }
  return await getUser(username);
};

export const createUser = async (username) => {
  const responseRaw = await fetch(`${API_BASE_URL}/user-api/user`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      username: username
    }),
  });

  if (!responseRaw.ok) {
    throw new Error('가입에 실패했습니다.');
  }

  const response = await responseRaw.json();

  if(response.errorMessage) {
    throw new Error('가입에 실패했습니다. 사유: ' + response.errorMessage);
  }

  return response.user;
};