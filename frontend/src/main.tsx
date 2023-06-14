import React, { lazy } from 'react';
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
import { MyStudy } from './pages/MyStudy/index.tsx';
import RoomPage from './pages/Room/RoomPage.tsx';

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
    path: '/my',
    Component: () => <MyStudy />,
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
  {
    path: '/search',
    Component: () => <Search />,
  },
  {
    path: '/room',
    Component: lazy(() => import('./pages/Room/RoomPage.tsx')),
  },
]);

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: false,
      staleTime: 30000,
    },
  },
});

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <GlobalStyles />
      <RouterProvider router={router} fallbackElement={<p>Loading...</p>} />
    </QueryClientProvider>
  </React.StrictMode>
);
