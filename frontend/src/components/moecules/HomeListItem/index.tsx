import styled from 'styled-components';
import { AiFillHeart, AiOutlineHeart } from 'react-icons/ai';
import { MouseEvent, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { RoomType } from '../../templates/MainTemplate';
import { api } from '../../../util/api';
import { useMutation } from '@tanstack/react-query';
import { formatDate } from '../../../util/formatDate';
import { startTransition } from 'react';
import { FaUser } from 'react-icons/fa';



type ToogleFavorite = {
  room_id: number;
  member_id: number;
  is_favorite: boolean;
};

export const HomeListItem = (props: RoomType) => {

  const [token, setToken] = useState('');
  const [memberId, setMemberId] = useState('');

  useEffect(() => {
    // 페이지 진입 시 로컬 스토리지 값 확인
    // 페이지 진입 시 로컬 스토리지 값 확인
    const userInfoString = localStorage.getItem('access_token');
    const usermemberId = localStorage.getItem('member_id');

    if (userInfoString && usermemberId) {
      setToken(JSON.parse(userInfoString));
      setMemberId(JSON.parse(usermemberId));
      console.log(userInfoString);
      console.log(usermemberId);
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

  const roomDelete = () =>{

    api
    .delete(
      // `${import.meta.env.VITE_BASE_URL}rooms/${memberId}/add`,
      `https://www.apithegong.com/rooms/${props.room_id}?member=${memberId}`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    )
    .then((response) => {
      // 요청 성공 시 처리
      window.location.reload(); // 페이지 리로드
    })
    .catch((error) => {
      // 요청 실패 시 처리
      console.error(error);
    });
  }


  return (
    <Container >
      {/* <button onClick={roomDelete}>삭제</button> */}
      <ImageContaienr onClick={room}>
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
      <h3 onClick={room}>{props.title}</h3>
      <Description className="describe">{props.info}</Description>
      <Info>
        <InfoItem>
          <p className="subject"><FaUser /></p>
          <p className="value">
            {props.member_current_count}/{props.member_max_count}명
          </p>
        </InfoItem>

        <InfoItem>
          <p className="subject"><AiFillHeart/></p>
          <p className="value">{props.favorite_count}</p>
        </InfoItem>

      </Info>
      <div className="tags">
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

  h3 {
    color: #333;
    font-size: 0.8rem;
    font-weight: bold;
    margin-bottom: 10px;
    cursor: pointer;
  }

`;

const Info = styled.div`
  display: flex;
  /* flex-wrap: wrap;
  gap: 15px; */
  justify-content: space-between;
  padding-bottom: 0.5rem;
  border-bottom: 1px solid #eee;

`;

const InfoItem = styled.div`
  display: flex;
  gap: 5px;

  .subject {
    font-size: 0.7rem;
    font-weight: bold;
    color: #4FAFB1;
  }

  .value {
    font-size: 0.7rem;
    font-weight: bold;
    color: #4FAFB1
  }
`;

const Tag = styled.button`
  padding: 0.5rem 0.8rem;
  border-radius: 2rem;
  background-color: #4fafb12c;
  font-size: 0.7rem;
`;

const ImageContaienr = styled.div<{ imgMaxWidth?: string }>`
  cursor: pointer;
  max-width: 100%;
  position: relative;
  aspect-ratio: 16 / 9; /* 16:9 비율 지정 */
  margin-bottom: 10px;

  img {
    object-fit: cover;
    border-radius: 8px;
    margin-bottom: 10px;
    width: 100%;
    height: 100%;
    border: 1px solid #eee;
  }

  i {
    position: absolute;
    right: 10px;
    top: 10px;

    z-index: 10;
  }
`;

const Description = styled.p`
  color: #494949;
  font-size: 1rem;
  font-weight: normal;
  margin-bottom: 1.5rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
`;