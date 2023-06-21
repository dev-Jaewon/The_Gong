import styled from 'styled-components';
import { Header } from '../../components/organisms/Header';
import { MainTemplate } from '../../components/templates/MainTemplate';
import { Footer } from '../../components/moecules/Footer';
import { useQueries } from '@tanstack/react-query';
import { api } from '../../util/api';
import { Skeleton } from '../../components/atoms/Skeleton/Skeleton';
import { BottomHeader } from '../../components/organisms/BottomHeader';
import Carousel from '../../components/organisms/Carousel/Carousel';

import Banner1 from './1.png';
import Banner2 from './2.png';
import Banner3 from './3.png';
import Banner4 from './4.png';

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

  let banner = [
    <img src={Banner1}></img>,
    <img src={Banner2}></img>,
    <img src={Banner3}></img>,
    <img src={Banner4}></img>,
  ];

  return (
    <Container>
      <Header />
      
      <Carousel contentList = {banner} contentNumber = {1} contentwidth = {500} contentheight = {20}></Carousel>
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
      <BottomHeader />
      {/* <Footer /> */}
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

const ZContainer = styled.div`
  width: 200px;
  height: 200px;
  border: 2px solid red;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 4rem;
  font-weight: 900;
`;

