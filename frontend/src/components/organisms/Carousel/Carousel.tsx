import styled from "styled-components";
import { useState } from "react";
import { FiChevronLeft } from "react-icons/fi";
import { FiChevronRight } from "react-icons/fi";

import CarouselWindow from "../../atoms/Carousel/CarouselWindow";
import CarousalSlide from "../../atoms/Carousel/CarousalSlide";
import CarousalSlideBox from "../../atoms/Carousel/CarousalSlideBax";
import CarouselButton from "../../atoms/Carousel/CarouselButton";
import CarouselDot from "../../atoms/Carousel/CarouselDot";

interface MainProps {
  contentList: React.ReactNode[];
  contentNumber: number;
  contentwidth: number;
  contentheight: number;
  contentMove?: number;
  contentDot?: boolean;
}

interface ContainerProps {
  contentwidth: number;
  contentheight: number;
}

interface NumObject {
  [key: number]: number;
}

function Carousel({contentList, contentNumber, contentwidth, contentheight, contentMove, contentDot}: MainProps){

  // 현재 슬라이드를 관리하는 스테이트
  const [currentContent, setCurrentContent] = useState(contentNumber);

  // 트렌지션을 관리하는 스테이트
  const [transitionTime, setTransitionTime] = useState(0.5);
  
  // 콘텐츠에 양옆에 클론 슬라이드를 추가 
  const content = [...contentList.slice(contentList.length-contentNumber, contentList.length), ...contentList, ...contentList.slice(0, contentNumber)];

  // 슬라이드의 갯수에 따른, 한 칸 비율
  const num: NumObject = {
    1: 100,
    2: 50,
    3: 33.33,
    4: 25,
    5: 20,
    6: 20,
    7: 20,
    8: 20,
    9: 20,
    10: 20,
  };

  // 한 개의 슬라이드 너비
  const width: number = num[contentNumber];
  
  // 슬라이드 이동 너비
  const move: number = num[contentMove || contentNumber];

  // 버튼을 클릭했을 때 실행되는 함수
  const changeContent = (direction:string) => {

    // 트렌지션 없이 이동하는 함수
    const replaceContent = (contentNumber?:number) => {

      // 이동하고 트렌지션 없이 이동
      setTimeout(() => {
        setTransitionTime(0);
        setCurrentContent(contentNumber);
      }, 500)
    }

    // 방향이 오른쪽일 때
    if(direction === 'right'){
      
      // 현재 슬라이드 순번에 + 1
      setCurrentContent(prev => {  
        setTransitionTime(0.5);
        console.log('오른')
        console.log(prev)
        // 만약 현재 슬라이드가 클론 슬라이드를 제외한 마지막 슬라이드 라면
        // 0.5초 뒤에 보여주는 콘텐츠의 갯수 - 1로 이동
        // (prev의 업데이트는 늦기 때문에 -1)
        if(prev === content.length - contentNumber -1){
          // 첫번째 슬라이드로 한 칸 뒤로 이동
          replaceContent(contentNumber)
        }
        return prev + 1
      });

    // 방향이 왼쪽일 때
    } else if(direction === 'left'){

      // 현재 순번의 슬라이드 - 1
      setCurrentContent(prev => {
        setTransitionTime(0.5);
        console.log('왼')
        console.log(prev)
        // 만약 현재 슬라이드의 인덱스가 1 이라면
        // 0.5초 뒤에 전체 길이의 - 클론 길이 - 보여주는 콘텐츠의 갯수 + 1로 이동
        // (prev의 업데이트는 늦기 때문에 -1)
        if(prev === 1){
          replaceContent(content.length - (contentNumber*2))
        }

        return prev - 1
      });
    }
  
  }

  // 점을 클릭했을 때 실행되는 함수
  const clickDot = (idx:number) => {
    setCurrentContent(idx)
  }

  return(
    // 캐러셀의 크기를 잡아주는 컨테이너
    <CarouselContainer 
      contentwidth={contentwidth} 
      contentheight={contentheight}
    >
      {/* 캐러셀의 노출되는 부분을 보여주는 박스 */}
      <CarouselWindow>
        
        {/* 캐러셀의 슬라이드들을 담고있는 기다란 박스 */}
        <CarousalSlideBox width={width} length={content.length} currentBanner={currentContent} move={move} transitionTime={transitionTime} >
          
          {/* 캐러셀의 실제 콘텐츠 */}
          {content.map((el, idx) => <CarousalSlide key={idx} length={content.length} contentheight={contentheight}>{el}</CarousalSlide>)}
        </CarousalSlideBox>
      </CarouselWindow>

      {/* 캐러셀의 버튼을 관리하는 박스 */}
      <CarouselController contentNumber={contentList.length} contentList={contentList} contentheight={contentheight} contentwidth={contentwidth}>
       
        <Container></Container>

        {/* CarousalSlideBox를 좌우로 이동시키는 버튼 */}
        <ButtonContainer>
          <CarouselButton direction='left' func={changeContent}><FiChevronLeft /></CarouselButton>
          <CarouselButton direction='right' func={changeContent}><FiChevronRight /></CarouselButton>
        </ButtonContainer>

        {/* CarousalSlideBox를 원하는 번호로 이동시키는 버튼 */}
        <Container>
          {contentDot && contentList.map((el, idx) => <CarouselDot key={idx} idx={idx+contentNumber} func={clickDot} ></CarouselDot>)}
        </Container>

      </CarouselController>

    </CarouselContainer>
  )
}

const CarouselContainer = styled.div<ContainerProps>`
  position: relative;
  max-width: ${({contentwidth}) => contentwidth}rem;
  max-height: ${({contentheight}) => contentheight ? `${contentheight}rem` : 'none'};
  width: 100%;
  margin: 0 auto;
`;

const CarouselController = styled.div<MainProps>`
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


export default Carousel;
