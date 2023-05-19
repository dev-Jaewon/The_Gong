import styled from 'styled-components';
import { AiFillHeart, AiOutlineHeart } from 'react-icons/ai';
import { MouseEvent } from 'react';
import { useNavigate } from 'react-router-dom';

export const HomeListItem = () => {
  const navigate = useNavigate();

  const handleTagClick = (e: MouseEvent<HTMLButtonElement>, tag: string) => {
    e.stopPropagation();
    navigate(`/search?keyword=${tag}`);
  };

  return (
    <Container>
      <ImageContaienr>
        <img
          src={
            'https://cdn.eyesmag.com/content/uploads/posts/2020/08/11/the-patrick-star-show-spongebob-squarepants-spin-off-1-516d0d4f-fcf0-4106-ab95-a407167fee2c.jpg'
          }
          alt={''}
        />
        <i>
          <AiOutlineHeart size={'2rem'} color={'white'} />
        </i>
      </ImageContaienr>
      <h3>제목</h3>
      <p className="describe">동해물과 백두산이</p>
      <Info>
        <InfoItem>
          <p className="subject">인원</p>
          <p className="value">
            {1}/{2}명
          </p>
        </InfoItem>
        <InfoItem>
          <p className="subject">그룹장</p>
          <p className="value">{'가나다라'}</p>
        </InfoItem>
        <InfoItem>
          <p className="subject">추천수</p>
          <p className="value">{1}</p>
        </InfoItem>
        <InfoItem>
          <p className="subject">생성일</p>
          <p className="value">{'2023. 3. 4'}</p>
        </InfoItem>
      </Info>
      <div className="tags">
        <Tag onClick={(e) => handleTagClick(e, 'java')}>{'javascript'}</Tag>
        {/* {props.tags.map((tag, index) => (
    <Tag onClick={(e) => handleTagClick(e, tag)} key={index}>
      {tag}
    </Tag>
  ))} */}
      </div>
    </Container>
  );
};

const Container = styled.div`
  h3 {
    color: #333;
    font-size: 18px;
    font-weight: 600;
    margin-bottom: 10px;
  }

  .describe {
    color: #8a8a8a;
    font-size: 13px;
    font-weight: 400;
    margin-bottom: 10px;
  }
`;

const Info = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
`;

const InfoItem = styled.div`
  display: flex;
  gap: 5px;

  .subject {
    font-size: 15px;
    font-weight: 700;
    color: #666;
  }

  .value {
    font-size: 15px;
    color: #666;
  }
`;

const Tag = styled.button`
  padding: 10px 10px;
  border-radius: 8px;
  background-color: #edecea;
`;

const ImageContaienr = styled.div<{ imgMaxWidth?: string }>`
  width: fit-content;
  position: relative;

  img {
    object-fit: cover;
    border-radius: 8px;
    margin-bottom: 10px;
    width: 100%;
  }

  i {
    position: absolute;
    right: 10px;
    top: 10px;

    z-index: 10;
  }
`;
