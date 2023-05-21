import styled from 'styled-components';
import { Header } from '../../components/organisms/Header';
import { MainTemplate } from '../../components/templates/MainTemplate';
import { Footer } from '../../components/moecules/Footer';
import { useQueries, useQuery } from '@tanstack/react-query';
import { api } from '../../util/api';

export const Main = () => {
  const [myRoom, newRoom, popularRoom] = useQueries({
    queries: [
      {
        queryKey: ['myRoom', 1],
        queryFn: () =>
          api
            .get(
              `/members/${localStorage.getItem(
                'member_id'
              )}/created?page=1&size=4`
            )
            .then((res) => res.data),
        staleTime: Infinity,
      },
      {
        queryKey: ['newRoom', 2],
        queryFn: () =>
          api.get('/rooms/new?page=1&size=5').then((res) => res.data),
        staleTime: Infinity,
      },
      {
        queryKey: ['popularRoom', 2],
        queryFn: () =>
          api
            .get('/search?keyword=&sort=favoriteCount')
            .then((res) => res.data),
        staleTime: Infinity,
      },
    ],
  });
  // console.log(myRoom);

  return (
    <Container>
      <Header />
      {myRoom.isFetching || newRoom.isFetching || popularRoom.isFetching ? (
        <div>로딩중</div>
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
