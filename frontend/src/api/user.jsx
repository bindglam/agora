import { API_BASE_URL } from '../constants';
import { getCookie, setCookie } from '../utils';

export const USERKEY_COOKIE_KEY = 'user_key';

export const getUserKeyInCookie = () => {
  return getCookie(USERKEY_COOKIE_KEY);
};

export const setUserKeyInCookie = (userKey) => {
  setCookie(USERKEY_COOKIE_KEY, userKey, 365);
};

export const resetUserKeyInCookie = () => {
  setCookie(USERKEY_COOKIE_KEY, '', -1);
};

export const getUser = async (userKey) => {
  const responseRaw = await fetch(`${API_BASE_URL}/user-api/user/${userKey}`);
  if (!responseRaw.ok) {
    return null;
  }
  const response = await responseRaw.json();
  return response;
};

export const getCurrentUser = async () => {
  const userKey = getUserKeyInCookie();
  if(userKey === null) {
    return null;
  }
  return await getUser(userKey);
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

export const UserPermission = {
  DEFAULT: { name: 'DEFAULT', parent: null },
  ADMIN: { name: 'ADMIN', parent: 'DEFAULT', level: 1 },
};
Object.freeze(UserPermission);

export const hasPermission = (userPermission, requiredPermission) => {
  if (userPermission.name === requiredPermission.name) {
    return true;
  }

  if(userPermission.parent === null) {
    return false;
  }

  return hasPermission(UserPermission[userPermission.parent], requiredPermission);
};