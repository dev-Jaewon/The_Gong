import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import { AiOutlineLock } from 'react-icons/ai';
import { formatDate } from '../../../util/formatDate';
import { MouseEvent } from 'react';
import searchThumbnail from '../../../assets/image/searchThumbnail.jpg';

export type SearchItem = {
  info: string;
  title: string;
  created_at: string;
  favorite_count: number;
  favorite_status: 'NONE';
  image_url: string;
  is_private: false;
  member_current_count: number;
  member_max_count: number;
  room_id: number;
  tags: Array<{
    name: string;
    tag_id: number;
  }>;
};

export const SearchListItem = (props: SearchItem) => {
  const navigate = useNavigate();

  const handleItemClick = () => {
    // 제윤님 여기 작업 private or public 일 경우 패스 작업
    props.is_private ? navigate('') : navigate('');
  };

  const handleTagClick = (e: MouseEvent<HTMLButtonElement>, tag: string) => {
    e.stopPropagation();
    navigate(`/search?keyword=${tag}`);
  };

  return (
    <Container onClick={handleItemClick}>
      <img src={props.image_url} alt={props.title + 'thumbnail'} />
      <div className="content">
        <div className="title">
          <h3>{props.title}</h3>
          {props.is_private && <AiOutlineLock size={18} />}
        </div>
        <p className="value">{props.info}</p>
        <Info>
          <InfoItem>
            <p className="subject">인원</p>
            <p className="value">
              {props.member_current_count}/{props.member_max_count}명
            </p>
          </InfoItem>
          <InfoItem>
            <p className="subject">추천수</p>
            <p className="value">{props.favorite_count}</p>
          </InfoItem>
          <InfoItem>
            <p className="subject">생성일</p>
            <p className="value">{formatDate(props.created_at)}</p>
          </InfoItem>
        </Info>
        <div className="tags">
          {props.tags.map((tag, index) => (
            <Tag onClick={(e) => handleTagClick(e, tag.name)} key={index}>
              {tag.name}
            </Tag>
          ))}
        </div>
      </div>
    </Container>
  );
};

const Container = styled.div`
  display: flex;
  padding: 20px;
  cursor: pointer;

  img {
    width: 300px;
    height: 200px;
    object-fit: cover;
    border-radius: 8px;
    margin-right: 40px;

    box-shadow: rgba(0, 0, 0, 0.15) 0px 5px 15px 0px;
  }

  .content {
    display: flex;
    flex-direction: column;
    width: 100%;

    .title {
      display: flex;
      align-items: center;
      margin-bottom: 10px;
      gap: 10px;

      h3 {
        font-size: 20px;
        font-weight: 500;
      }
    }

    .tags {
      gap: 10px;
      display: flex;
      flex-wrap: wrap;
      margin-top: auto;
    }

    .value {
      margin-bottom: 10px;
      color: #333;
    }
  }

  @media (max-width: 720px) {
    padding: 20px;
    flex-direction: column;
    gap: 10px;

    img {
      width: 100%;
      height: auto;
    }
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
