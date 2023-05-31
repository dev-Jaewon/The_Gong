import styled from 'styled-components';
import { Header } from '../../components/organisms/Header';
import { MainTemplate } from '../../components/templates/MainTemplate';
import { Footer } from '../../components/moecules/Footer';
import { useQueries } from '@tanstack/react-query';
import { api } from '../../util/api';
import { Skeleton } from '../../components/atoms/Skeleton/Skeleton';
import { Banner } from '../../components/organisms/Banner';
import { useEffect } from 'react';

export const Main = () => {

  const [myRoom, newRoom, popularRoom] = useQueries({
    queries: [
      {
        queryKey: ['myRoom', 1],
        queryFn: () =>
          api
            .get(
              `${import.meta.env.VITE_BASE_URL}members/${localStorage.getItem(
                'member_id'
              )}/created?page=1&size=4`
            )

            .then((res) => res.data),
        refetchOnMount: 'always',
      },
      {
        queryKey: ['newRoom', 2],
        queryFn: () =>
          api
            .get(`${import.meta.env.VITE_BASE_URL}rooms/new?page=1&size=5`)
            .then((res) => res.data),
        staleTime: Infinity,
      },
      {
        queryKey: ['popularRoom', 2],
        queryFn: () =>
          api
            .get(
              `${import.meta.env.VITE_BASE_URL}search?keyword=&sort=favoriteCount`
            )
            .then((res) => res.data),
        staleTime: Infinity,
      },
    ],
  });

  return (
    <Container>
      <Header />
      <Banner />
      {myRoom.isFetching || newRoom.isFetching || popularRoom.isFetching ? (
        <LodingContainer>
          <LodingContent>
            <div>
              <Skeleton width="40%" height="30px" />
              <Skeleton width="100%" height="30px" />
              <Skeleton width="100%" height="30px" />
            </div>
            <div>
              <Skeleton width="40%" height="30px" />
              <Skeleton width="100%" height="30px" />
              <Skeleton width="100%" height="30px" />
            </div>
            <div>
              <Skeleton width="40%" height="30px" />
              <Skeleton width="100%" height="30px" />
              <Skeleton width="100%" height="30px" />
            </div>
            <div>
              <Skeleton width="40%" height="30px" />
              <Skeleton width="100%" height="30px" />
              <Skeleton width="100%" height="30px" />
            </div>
            <div>
              <Skeleton width="40%" height="30px" />
              <Skeleton width="100%" height="30px" />
              <Skeleton width="100%" height="30px" />
            </div>
          </LodingContent>
        </LodingContainer>
      ) : (
        <MainTemplate
          myRoom={myRoom.data}
          newRoom={newRoom.data}
          popularRoom={popularRoom.data}
        />
      )}
      <Footer />
    </Container>
  );
};

const Container = styled.div``;

const LodingContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const LodingContent = styled.div`
  display: flex;
  flex-direction: column;
  gap: 50px;
  width: 100%;
  padding-top: 50px;
  max-width: 1050px;

  & > div {
    display: flex;
    flex-direction: column;
    gap: 10px;
    width: 100%;
  }
`;
