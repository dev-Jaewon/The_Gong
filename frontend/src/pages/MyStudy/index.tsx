import styled from 'styled-components';
import { Header } from '../../components/organisms/Header';
import { MyStudyTemplate } from '../../components/templates/MyStudyTemplate';
import { Footer } from '../../components/moecules/Footer';
import { useQueries } from '@tanstack/react-query';
import { api } from '../../util/api';
import { Skeleton } from '../../components/atoms/Skeleton/Skeleton';
import { BottomHeader } from '../../components/organisms/BottomHeader';

export const MyStudy = () => {

  const [myRoom, newRoom, popularRoom, historyRoom] = useQueries({
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
        queryKey: ['popularRoom', 3],
        queryFn: () =>
          api
            .get(
              `${import.meta.env.VITE_BASE_URL}search?keyword=&sort=favoriteCount`
            )
            .then((res) => res.data),
        staleTime: Infinity,
      },
      {
        queryKey: ['historyRoom', 4],
        queryFn: () =>
          api
            .get(
              `${import.meta.env.VITE_BASE_URL}members/${localStorage.getItem(
                'member_id'
              )}/history`)
            .then((res) => {
              console.log(res.data)
              return res.data
            }),
        staleTime: Infinity,
      },
    ],
  });

  return (
    <Container>
      <Header />
      {myRoom.isFetching || newRoom.isFetching || popularRoom.isFetching || historyRoom.isFetching ? (
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
        <MyStudyTemplate
          myRoom={myRoom.data}
          newRoom={newRoom.data}
          popularRoom={popularRoom.data}
          historyRoom={historyRoom.data}
        />
      )}
      <BottomHeader />
    </Container>
  );
};



const Container = styled.div`
  position: relative;
  width: 100%;
  height: 100%;
`;

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
