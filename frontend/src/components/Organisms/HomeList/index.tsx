import styled from 'styled-components';
import { HomeListItem } from '../../moecules/HomeListItem';
import { RoomType } from '../../templates/MainTemplate';
import { Swiper, SwiperSlide, SwiperProps } from 'swiper/react';
import { FreeMode, Navigation } from 'swiper';
import { IoIosArrowBack, IoIosArrowForward } from 'react-icons/io';

import 'swiper/swiper.min.css';
import 'swiper/swiper-bundle.min.css';

export type HomeListItemProps = {
  id: string;
  title: string;
  description: string;
  slidesToShow: number;
  imgMaxWidth?: string;
  list: Array<RoomType>;
};

export const HomeList = (props: HomeListItemProps) => {
  const swiperSetProperty: SwiperProps = {
    modules: [FreeMode, Navigation],
    spaceBetween: 20,
    slidesPerView: 3,
    slidesPerGroup: 4,
    wrapperTag: 'ul',
    navigation: {
      nextEl: `.next-button-${props.id}`,
      prevEl: `.prev-button-${props.id}`,
    },
  };

  return (
    <Container>
      <h2>{props.title}</h2>
      <p className="subject_describe">{props.description}</p>
      <SwiperContainer>
        <i
          className={`swiper-button prev prev-button-${props.id}`}
          aria-label="이전 상품리스트 버튼"
        >
          <IoIosArrowBack size={30} />
        </i>
        <i
          className={`swiper-button next next-button-${props.id}`}
          aria-label="다음 상품리스트 버튼"
        >
          <IoIosArrowForward size={30} />
        </i>
        <Swiper {...swiperSetProperty}>
          {props.list.map((room) => (
            <SwiperSlide>
              <ItemContainer imgMaxWidth={props.imgMaxWidth}>
                <HomeListItem {...room} />
              </ItemContainer>
            </SwiperSlide>
          ))}
        </Swiper>
      </SwiperContainer>
    </Container>
  );
};

const Container = styled.div`
  display: flex;
  position: relative;
  flex-direction: column;
  width: 100%;

  .divider {
    width: 100%;
    height: 1px;
  }

  h2 {
    font-size: 22px;
    font-weight: 700;
    margin-bottom: 15px;
  }

  .subject_describe {
    font-size: 15px;
    font-weight: 400;
    color: #8a8a8a;
    margin-bottom: 15px;
  }

  .tags {
    gap: 10px;
    display: flex;
    flex-wrap: wrap;
    margin-top: 10px;
  }

  .slick-list {
    margin-right: -20px;
  }

  .slick-slide {
    padding-right: 20px;
  }

  .slick-prev::before,
  .slick-next::before {
    opacity: 0;
    display: none;
  }
  .slick-slide div {
    cursor: pointer;
  }
`;

const ItemContainer = styled.div<{ imgMaxWidth?: string }>`
  width: 100%;
  max-width: ${({ imgMaxWidth }) => imgMaxWidth || '500px'};
`;

const SwiperContainer = styled.div`
  position: relative;
  width: 100%;
  max-width: 1050px;
  margin-top: 10px;

  .swiper-button {
    top: calc(50% - 50px);
    position: absolute;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 55px;
    height: 55px;
    border-radius: 50%;
    background: white;
    box-shadow: rgba(99, 99, 99, 0.2) 0px 2px 8px 0px;
    z-index: 10;
    transform: translateY(-50px);
    cursor: pointer;
  }

  .next {
    right: 0;
    transform: translate(30px, -50%);
  }

  .prev {
    transform: translate(-30px, -50%);
  }

  .swiper-wrapper {
    li {
      &:not(:last-child) {
        margin-right: 17px;
      }
    }
  }

  .swiper-slide {
    display: flex;
    align-items: center;
    justify-content: center;
    width: fit-content;
  }

  .swiper-button-disabled {
    display: none;
  }
`;
