import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import { AiOutlineLock } from 'react-icons/ai';
import { formatDate } from '../../../util/formatDate';
import { MouseEvent } from 'react';

export type SearchItem = {
  room_id: number;
  title: string;
  info: string;
  admin: {
    member_id: number;
    nickname: string;
    about_me: string;
    image_irl: string;
  };
  image_url: string;
  member_max_count: number;
  member_current_count: number;
  is_private: true;
  created_at: string;
  last_modified_at: string;
  favorite_count: number;
  tags: string[];
};

export const SearchListItem = (props: SearchItem) => {
  const navigate = useNavigate();

  const handleItemClick = () => {
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
            <p className="subject">그룹장</p>
            <p className="value">{props.admin.nickname}</p>
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
            <Tag onClick={(e) => handleTagClick(e, tag)} key={index}>
              {tag}
            </Tag>
          ))}
        </div>
      </div>
    </Container>
  );
};

const Container = styled.div`
  display: flex;
  padding: 20px 0;
  cursor: pointer;

  img {
    width: 300px;
    height: 200px;
    object-fit: cover;
    border-radius: 8px;
    margin-right: 20px;
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
