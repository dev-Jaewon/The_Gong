import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${JSON.parse(
      localStorage.getItem('access_token') as string
    )}`,
  },
  timeout: 2000,
});

export { api };
