import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${JSON.parse(
      localStorage.getItem('access_token') as string
    )}`,
  },
  timeout: 3000,
});

api.defaults.withCredentials = true;

api.interceptors.response.use(
  (res) => res,
  async (err) => {
    const {
      config,
      response: { status },
    } = err;

    if (status !== 401) {
      return Promise.reject(err);
    }

    try {
      const resToken = await axios.post<{ access_token: string }>(
        `${import.meta.env.VITE_BASE_URL}/auth/refresh`,
        {
          refreshToken: JSON.parse(
            localStorage.getItem('refresh_token') as string
          ),
        }
      );

      if (resToken.data.access_token) {
        config.headers.Authorization = `Bearer ${resToken.data.access_token}`;
      }

      return axios(config);
    } catch (error) {
      localStorage.removeItem('member_id');
      localStorage.removeItem('nickname');
      localStorage.removeItem('image_url');
      localStorage.removeItem('access_token');
      localStorage.removeItem('refresh_token');

      return Promise.reject(err);
    }
  }
);

export { api };
