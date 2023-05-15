import { useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';

export const Oauth = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  useEffect(() => {
    const accessToken = searchParams.get('access_token');
    const refreshToken = searchParams.get('refresh_token');

    localStorage.setItem('accessToken', JSON.stringify(accessToken));
    localStorage.setItem('refreshToken', JSON.stringify(refreshToken));

    navigate('/');
  }, []);

  return <></>;
};
