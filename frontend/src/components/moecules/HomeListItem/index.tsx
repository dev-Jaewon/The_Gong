import styled from 'styled-components';
import { AiFillHeart, AiOutlineHeart } from 'react-icons/ai';
import { MouseEvent, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { RoomType } from '../../templates/MainTemplate';
import { api } from '../../../util/api';
import { useMutation } from '@tanstack/react-query';
import { formatDate } from '../../../util/formatDate';
import { startTransition } from 'react';


type ToogleFavorite = {
  room_id: number;
  member_id: number;
  is_favorite: boolean;
};



export const HomeListItem = (props: RoomType) => {

  const [memberId, setMemberId] = useState('');

  useEffect(() => {
    // 페이지 진입 시 로컬 스토리지 값 확인
    const usermemberId = localStorage.getItem('member_id');
    if ( usermemberId) {
      setMemberId(JSON.parse(usermemberId));
    } else {
      console.log('스토리지 값 없음');
    }
  }, []);

  const navigate = useNavigate();

  const handleTagClick = (e: MouseEvent<HTMLButtonElement>, tag: string) => {
    e.stopPropagation();
    navigate(`/search?keyword=${tag}`);
  };

  const mutation = useMutation({
    mutationFn: async (data: ToogleFavorite) =>
      api.post(`/rooms/${data.room_id}/favorite`, data),
    onSuccess: ({ data }) => {},
  });

  const handleToogleFavorite = (is_favorite: boolean) => {
    mutation.mutate({
      room_id: 2,
      member_id: 1,
      is_favorite,
    });
  };

  const room = () => {

    if(memberId){

      if(props.member_current_count >= props.member_max_count){
        alert('입장 인원을 초과했습니다.')
      } else {
        startTransition(() => {
          navigate(`/room?roomId=${props.title}`);
        });
      }

    } else {
      alert('로그인이 필요한 서비스 입니다.')
      navigate(`/signin`);
    }

  }


  return (
    <Container onClick={room}>
      <ImageContaienr>
        <img src={props.image_url} alt={`${props.title} 이미지`} />
        <i>
          {props.favorite_status === 'NONE' ? (
            <AiOutlineHeart
              size={'2rem'}
              color={'white'}
              onClick={() => handleToogleFavorite(true)}
            />
          ) : (
            <AiFillHeart
              size={'2rem'}
              color={'red'}
              onClick={() => handleToogleFavorite(false)}
            />
          )}
        </i>
      </ImageContaienr>
      <h3>{props.title}</h3>
      <p className="describe">{props.info}</p>
      <Info>
        <InfoItem>
          <p className="subject">인원</p>
          <p className="value">
            {props.member_current_count}/{props.member_max_count}명
          </p>
        </InfoItem>
        <InfoItem>
          <p className="subject">그룹장</p>
          <p className="value">{'가나다라'}</p>
        </InfoItem>
        <InfoItem>
          <p className="subject">추천수</p>
          <p className="value">{props.favorite_count}</p>
        </InfoItem>
        <InfoItem>
          <p className="subject">생성일</p>
          <p className="value">
            {props.created_at ? formatDate(props.created_at) : '비공개'}
          </p>
        </InfoItem>
      </Info>
      <div className="tags">
        <Tag onClick={(e) => handleTagClick(e, 'java')}>{'javascript'}</Tag>
        {props.tags.map((tag, index) => (
          <Tag onClick={(e) => handleTagClick(e, tag.name)} key={index}>
            {tag.name}
          </Tag>
        ))}
      </div>
    </Container>
  );
};

const Container = styled.div`
  cursor: pointer;

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
  max-width: 300px;
  position: relative;
  height: 200px;
  margin-bottom: 10px;

  img {
    object-fit: cover;
    border-radius: 8px;
    margin-bottom: 10px;
    width: 100%;
    height: 100%;

  }

  i {
    position: absolute;
    right: 10px;
    top: 10px;

    z-index: 10;
  }
`;
