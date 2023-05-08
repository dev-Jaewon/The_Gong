import axios from 'axios';

const api = axios.create({
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 2000,
});

if (process.env.NODE_ENV === 'production') {
  api({ baseURL: import.meta.env.VITE_BASE_URL });
}

export { api };
