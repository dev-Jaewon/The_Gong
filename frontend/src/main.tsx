import React from 'react';
import ReactDOM from 'react-dom/client';
import { GlobalStyles } from './styles/GlobalStyle';
import { QueryClientProvider, QueryClient } from '@tanstack/react-query';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';

import { Signup } from './pages/Signup/index.tsx';

if (process.env.NODE_ENV === 'development') {
  const { worker } = require('./__mocks__/browser');
  worker.start();
}

let router = createBrowserRouter([
  {
    path: '/signup',
    Component: () => <Signup />,
  },
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
