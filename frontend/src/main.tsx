import React from 'react';
import ReactDOM from 'react-dom/client';
import { GlobalStyles } from './styles/GlobalStyle';
import { QueryClientProvider, QueryClient } from '@tanstack/react-query';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import CreateRoomPage from './pages/CreateRoom/CreateRoom.tsx';
import { Signup } from './pages/Signup/index.tsx';
import { Signin } from './pages/Signin/index.tsx';
import { Oauth } from './pages/Oauth/index.tsx';
import { Search } from './pages/search/index.tsx';
import { Main } from './pages/Main/index.tsx';

if (process.env.NODE_ENV === 'development') {
  const { worker } = require('./__mocks__/browser');
  // worker.start();
}

let router = createBrowserRouter([
  {
    path: '/',
    Component: () => <Main />,
  },
  {
    path: '/signup',
    Component: () => <Signup />,
  },
  {
    path: '/createRoom',
    Component: () => <CreateRoomPage />,
  },
  {
    path: '/oauth',
    Component: () => <Oauth />,
  },
  {
    path: '/signin',
    Component: () => <Signin />,
  },
  { path: '/search', Component: () => <Search /> },
]);

const queryClient = new QueryClient();

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <GlobalStyles />
      <RouterProvider router={router} fallbackElement={<p>Loading...</p>} />
    </QueryClientProvider>
  </React.StrictMode>
);
