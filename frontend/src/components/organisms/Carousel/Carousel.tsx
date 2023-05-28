import styled from "styled-components";
import { useState } from "react";
import { FiChevronLeft } from "react-icons/fi";
import { FiChevronRight } from "react-icons/fi";
import { BiChevronLeft } from "react-icons/bi";
import { BiChevronRight } from "react-icons/bi";

import CarouselWindow from "../../atoms/Carousel/CarouselWindow";
import CarousalSlide from "../../atoms/Carousel/CarousalSlide";
import CarousalSlideBox from "../../atoms/Carousel/CarousalSlideBax";
import CarouselButton from "../../atoms/Carousel/CarouselButton";
import CarouselDot from "../../atoms/Carousel/CarouselDot";

interface Props {
  contentList: React.ReactNode[];
  contentNumber: number;
  contentwidth: number;
  contentheight: number;
  contentMove?: number;
  contentDot?: boolean;
}

const CarouselContainer = styled.div<Props>`
  position: relative;
  max-width: ${({contentwidth}) => contentwidth}rem;
  width: 100%;
  max-height: ${({contentheight}) => contentheight ? `${contentheight}rem` : 'none'};
  margin: 0 auto;
  overflow: hidden;
`;

const CarouselController = styled.div<Props>`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);

  display: flex;
  flex-direction: column;
  justify-content: space-between;

  padding: 1rem;
  width: 100%;
  height: 100%;
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: space-between;
`;

const Container = styled.div`
  display: flex;
  justify-content: center;
`;

function Carousel({contentList, contentNumber, contentwidth, contentheight, contentMove, contentDot}: Props){

  // 몇개의 슬라이드를 보여주고 싶나여
  const slide = contentNumber;

  // 시작하고 싶은 슬라이드 입력
  const [currentBanner, setCurrentBanner] = useState(slide);

  // 트렌지션을 관리하는 스테이트
  const [transitionTime, setTransitionTime] = useState(0.5);

  // 원본 콘텐츠
  const original = contentList;
  
  // 수정 콘텐츠
  const content = [...original.slice(original.length-slide, original.length), ...original, ...original.slice(0, slide)];

  interface NumObject {
    [key: number]: number;
  }
  
  const num: NumObject = {
    1: 100,
    2: 50,
    3: 33.33,
    4: 25,
    5: 20,
  };
  
  // 슬라이드 너비
  const width: number = num[slide];
  
  // 슬라이드 이동 너비
  const move: number = num[contentMove || slide];

  // 슬라이드 이동 함수
  const changeBanner = (dr:string) => {

    const replaceSlide = (index:number) => {
      setTimeout(() => {
        setTransitionTime(0);
        setCurrentBanner(index);
      }, 500)
    }

    if(dr === 'right'){
      setCurrentBanner(prev => {  
        setTransitionTime(0.5);
        if(prev >= content.length-slide-1){
          replaceSlide(slide)
        }
        return prev + 1
      });

    } else if(dr === 'left'){
      setCurrentBanner(prev => {
        setTransitionTime(0.5);
        if(prev <= 1){
          replaceSlide(content.length-slide-1)
        }
        return prev - 1
      });
    }
  }

  const clickDot = (idx:number) => {
    setCurrentBanner(idx)
  }

  return(
    <CarouselContainer 
      contentwidth={contentwidth} 
      contentheight={contentheight}
      contentList={contentList}
      contentNumber={contentNumber}
    >
      <CarouselWindow>
        <CarousalSlideBox currentBanner={currentBanner} move={move} transitionTime={transitionTime} width={width} length={content.length}>
          {content.map((el, idx) => <CarousalSlide key={idx} length={content.length}>{el}</CarousalSlide>)}
        </CarousalSlideBox>
      </CarouselWindow>

      <CarouselController contentNumber={contentList.length} contentList={contentList} contentheight={contentheight} contentwidth={contentwidth}>
        <Container></Container>
        <ButtonContainer>
          <CarouselButton  dr='left' func={changeBanner}><FiChevronLeft /></CarouselButton>
          <CarouselButton  dr='right' func={changeBanner}><FiChevronRight /></CarouselButton>
        </ButtonContainer>
        <Container>
          {contentDot && original.map((el, idx) => <CarouselDot key={idx} idx={idx+slide} func={clickDot} ></CarouselDot>)}
        </Container>
      </CarouselController>

    </CarouselContainer>
  )
}

export default Carousel;
