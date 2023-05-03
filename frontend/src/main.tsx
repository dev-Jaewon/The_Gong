import React from 'react';
import ReactDOM from 'react-dom/client';
import { App } from './App.tsx';
import { GlobalStyles } from './styles/GlobalStyle';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';

if (process.env.NODE_ENV === 'development') {
  const { worker } = require('./__mocks__/browser');
  worker.start();
}

let router = createBrowserRouter([
  {
    path: '/',
    Component: () => <App />,
  },
]);

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
  <React.StrictMode>
    <>
      <GlobalStyles />
      <RouterProvider router={router} fallbackElement={<p>Loading...</p>} />
    </>
  </React.StrictMode>
);
