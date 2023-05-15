import React from 'react';
import ReactDOM from 'react-dom/client';
import { GlobalStyles } from './styles/GlobalStyle';
import { QueryClientProvider, QueryClient } from '@tanstack/react-query';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import Carousel from "./components/Organisms/Carousel/Carousel";
import { Signup } from './pages/Signup/index.tsx';
import styled from 'styled-components';

const Stule = styled.div`

`

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
      <Stule>
       <GlobalStyles />
      </Stule>
      {/* <Carousel contentList={['1', '2', '3', '4', '5', '6', '7', '8']} contentNumber={2} contentwidth={50} contentheight={20} contentDot={true}></Carousel> */}
      <RouterProvider router={router} fallbackElement={<p>Loading...</p>} />
    </QueryClientProvider>
  </React.StrictMode>
);
