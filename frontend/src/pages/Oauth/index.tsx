import { useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';

export const Oauth = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  useEffect(() => {
    const accessToken = searchParams.get('access_token');
    const refreshToken = searchParams.get('refresh_token');

    localStorage.setItem('access_token', JSON.stringify(accessToken));
    localStorage.setItem('refresh_token', JSON.stringify(refreshToken));

    navigate('/');
  }, []);

  return <></>;
};