import Slider, { Settings } from 'react-slick';
import styled from 'styled-components';
import { IoIosArrowForward, IoIosArrowBack } from 'react-icons/io';
import { HomeListItem } from '../../moecules/HomeListItem';
import { useState } from 'react';

export type HomeListItemProps = {
  title: string;
  description: string;
  slidesToShow: number;
  imgMaxWidth?: string;
};

export const HomeList = (props: HomeListItemProps) => {
  const [current, setCurrent] = useState(0);

  const settings: Settings = {
    infinite: false,
    autoplay: false,
    dots: false,
    speed: 100,
    arrows: true,
    slidesToShow: props.slidesToShow,
    slidesToScroll: 1,
    prevArrow: (
      <DivPre>
        <IoIosArrowBack size={40} color="black" />
      </DivPre>
    ),
    nextArrow: (
      <Div>
        <IoIosArrowForward size={40} color="black" />
      </Div>
    ),
    afterChange: (num: number) => {
      setCurrent(num);
    },
  };

  return (
    <Container current={current}>
      <h2>{props.title}</h2>
      <p className="subject_describe">{props.description}</p>

      <Slider {...settings}>
        <ItemContainer imgMaxWidth={props.imgMaxWidth}>
          <HomeListItem />
        </ItemContainer>
        <ItemContainer imgMaxWidth={props.imgMaxWidth}>
          <HomeListItem />
        </ItemContainer>
        <ItemContainer imgMaxWidth={props.imgMaxWidth}>
          <HomeListItem />
        </ItemContainer>
        <ItemContainer imgMaxWidth={props.imgMaxWidth}>
          <HomeListItem />
        </ItemContainer>
      </Slider>
    </Container>
  );
};

const Container = styled.div<{ current: number }>`
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

  .slick-prev {
    display: ${({ current }) => (!current ? 'none !important' : 'block')};
  }

  .slick-next {
    display: ${({ current }) => (current ? 'none !important' : 'block')};
  }
`;

const ItemContainer = styled.div<{ imgMaxWidth?: string }>`
  width: 100%;
  max-width: ${({ imgMaxWidth }) => imgMaxWidth || '500px'};
`;

const Div = styled.div`
  position: absolute;
  width: 40px;
  height: 40px;
  right: 16px;
  border-radius: 50%;
  background: #ffffff;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
  transform: translateY(-50px);
  z-index: 99;

  &:hover {
    background: #ffffff;
  }
`;
const DivPre = styled.div`
  position: absolute;
  width: 40px;
  height: 40px;
  left: 16px;
  text-align: left;
  border-radius: 50%;
  background: #ffffff;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
  transform: translateY(-50px);
  z-index: 99;

  &:hover {
    background: #ffffff;
  }
`;
