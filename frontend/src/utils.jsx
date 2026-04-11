export const getCookie = (name) => {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(';').shift();
  return null;
};

export const setCookie = (name, value, days = 7) => {
  const date = new Date();
  date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
  const expires = `expires=${date.toUTCString()}`;
  document.cookie = `${name}=${value};${expires};path=/`;
};

export const areCookiesEnabled = () => {
  try {
    // 테스트용 쿠키 설정 시도
    const testCookie = 'test_cookie_enabled';
    document.cookie = `${testCookie}=1;path=/`;

    // 쿠키가 제대로 설정되었는지 확인
    const cookiesEnabled = document.cookie.indexOf(`${testCookie}=`) !== -1;

    // 테스트 쿠키 삭제
    document.cookie = `${testCookie}=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/`;

    return cookiesEnabled;
  } catch (e) {
    // 쿠키 설정 중 에러 발생 시 쿠키 비활성화로 간주
    return false;
  }
};