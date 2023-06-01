import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
});


api.defaults.withCredentials = true;

api.interceptors.request.use(
  (config) => {
    const accessToken = localStorage.getItem('access_token');

    if (accessToken) {
      config.headers['Authorization'] = `Bearer ${JSON.parse(accessToken)}`;
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

api.interceptors.response.use(
  (res) => res,
  async (err) => {
    const {
      code,
      config,
      response: { status },
    } = err;

    if (!status || status !== 401 || code === 'ERR_NETWORK') {
      return Promise.reject(err);
    }

    try {
      const resToken = await axios.post<{ accessToken: string }>(
        `${import.meta.env.VITE_BASE_URL}auth/refresh`,
        {
          refreshToken: JSON.parse(
            localStorage.getItem('refresh_token') as string
          ),
        }
      );

      if (resToken.data.accessToken) {
        localStorage.setItem(
          'access_token',
          JSON.stringify(resToken.data.accessToken)
        );
        config.headers.Authorization = `Bearer ${resToken.data.accessToken}`;
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
