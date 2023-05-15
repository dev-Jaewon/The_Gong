import styled from "styled-components";
import Carousel from "../../components/Organisms/Carousel/Carousel";
import Header from "./Header";
import banner1 from  "./img/banner (1).png"
import banner2 from  "./img/banner (2).png"
import banner3 from  "./img/banner (3).png"
import banner4 from  "./img/banner (4).png"
import banner5 from  "./img/banner (5).png"
import banner6 from  "./img/banner (6).png"
import banner7 from  "./img/banner (7).png"
import banner8 from  "./img/banner (8).png"
import Footer from "./Footer";

const MainPageContainer = styled.div`
  position: relative;
  height: fit-content;
`
const MainPageList = styled.div`
  max-width: 74rem;
  margin: 5rem auto;
`

const MainPageImg = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
`

const MainPageItem = styled.img`
  width: 18rem;
  height: 18rem;
  object-fit: cover;
`
const MainPageListTitle = styled.div`
  font-size: 1.5rem;
  font-weight: bold;
  color: #4A5056;
`

const MainPage = () => {
  const banner = [banner1, banner2, banner3, banner4, banner5, banner6, banner7, banner8]
  const banners = banner.map(el => <MainPageImg src={el}></MainPageImg>)
  const list = banner.map(el => <MainPageItem src={el}></MainPageItem>)

  return (
    <MainPageContainer>
      <Header></Header>
      <Carousel contentList={banners} contentNumber={1} contentwidth={128} contentheight={40} contentDot={true}></Carousel>
      
      <MainPageList>
        <MainPageListTitle>최신 스터디</MainPageListTitle>
        <Carousel contentList={list} contentNumber={4} contentwidth={74} contentheight={20} contentDot={false}></Carousel>
      </MainPageList>

      <MainPageList>
        <MainPageListTitle>추천 스터디</MainPageListTitle>
        <Carousel contentList={list} contentNumber={4} contentwidth={74} contentheight={20} contentDot={false}></Carousel>
      </MainPageList>

      <Footer></Footer>
    
    </MainPageContainer>
  )
}

export default MainPage;