import Slider, { Settings } from 'react-slick';
import styled from 'styled-components';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';

import banner_1 from '../../../assets/image/banner_1.jpg';

export const Banner = () => {
  const settings: Settings = {
    dots: false,
    infinite: true,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
  };

  return (
    <Container>
      <Slider {...settings}>
        <ImgContainer>
          <Img src={banner_1} alt="" />
        </ImgContainer>
        <ImgContainer>
          <Img src={banner_1} alt="" />
        </ImgContainer>
        <ImgContainer>
          <Img src={banner_1} alt="" />
        </ImgContainer>
        <ImgContainer>
          <Img src={banner_1} alt="" />
        </ImgContainer>
      </Slider>
    </Container>
  );
};

const Container = styled.div`
overflow: hidden;
`;

const ImgContainer = styled.div`
  width: 100vw;
  height: 20rem;
  display: flex;
  justify-content: center;
  align-items: center;
`;

const Img = styled.img`
  width: 100%;
  height: 20rem;
  object-fit: cover;
`;
