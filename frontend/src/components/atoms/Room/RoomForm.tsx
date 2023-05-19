import styled from "styled-components";
import { Input } from "../Input";
import useForm from "../../../hooks/useForm";
import { InputLabel } from "../../moecules/InputLabel";
import { Button } from "../Button";
import { useState } from "react";
import axios from "axios";
import TagBox from "../../Molecules/TagBox";
import TagForm from "../../Organisms/TagForm";

const RoomFormContainer = styled.div`


`;

const ContainerForm = styled.form`
  display: flex;
  flex-direction: column;
  min-width: 400px;
  gap: 20px;

  font-family: Noto Sans KR;

  button {
    margin-top: 20px;
  }
`

export type RoomData = {
  title: string;
  info: string;
  image_url: string;
  member_max_count: number;
  is_private: boolean;
  password: string;
  tags: string;
};

export type RoomFormProps = {
  isLoading: boolean;
  onSubmit: (value: RoomData) => void;
};

const RoomForm = (props: RoomFormProps) => {

  const fetchData = async () => {
    try {
      const response = await axios.get('https://a5fa-211-193-143-25.ngrok-free.app/new', {
        params: {
          page: 1,
          size: 5
        }
      });
  
      console.log(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  fetchData()

  const [isPrivate, setIsPrivate] = useState(false);
  const [ maxCount, setMaxCount ] = useState(null);

  const maxCountChange = (event:any) => {
    setMaxCount(event.target.value);
  } 

  const handleChange2 = (event:any) => {
    setIsPrivate(event.target.value === 'private');
    console.log(isPrivate)
  };

  const { data, errors, handleChange, handleSubmit } = useForm<RoomData>({
    validations: {
      title: {
        required: {
          value: true,
          message: '방 제목은 필수입력 항목입니다.',
        },
      },
      info: {
        required: {
          value: true,
          message: '방 소개는 필수입력 항목입니다.',
        },
      },
      image_url: {
        required: {
          value: true,
          message: '대표 이미지는 필수입력 항목입니다.',
        },
      },
      member_max_count: {
        required: {
          value: true,
          message: '인원 수는 필수입력 항목입니다.',
        },
      },
      password: {
        required: {
          value: isPrivate,
          message: '패스워드는 필수입력 항목입니다.',
        },
      },
      tags: {
        required: {
          value: true,
          message: '태그는 필수입력 항목입니다.',
        },
      },
    },
    onSubmit: handleSubmitFormHook,
  });

  function handleSubmitFormHook() {
    const subData = {...data, 'is_private' : isPrivate, 'member_max_count' : Number(data.member_max_count), 'tags' : [data.tags]};
    props.onSubmit(subData);
  }


  return (
    <RoomFormContainer>
      <TagForm></TagForm>
      <ContainerForm onSubmit={handleSubmit}>
        <InputLabel
          label="방 제목"
          onChange={handleChange('title')}
          placeholder="닉네임을 입력해주세요."
          errorMessage={errors.title}
          isValid={errors.title ? false : true}
        />
        <InputLabel
          label="방 소개"
          onChange={handleChange('info')}
          placeholder="방 소개를 입력해주세요."
          errorMessage={errors.info}
          isValid={errors.info ? false : true}
        />
        <InputLabel
          label="대표 이미지"
          onChange={handleChange('image_url')}
          placeholder="영문, 숫자 8자이상의 비밀번호를 입력해주세요."
          errorMessage={errors.image_url}
          isValid={errors.image_url ? false : true}
        />
        <InputLabel
          type="number"
          label="인원 수"
          onChange={handleChange('member_max_count')}
          placeholder="인원수를 입력해 주세요"
          errorMessage={errors.member_max_count}
          isValid={errors.member_max_count ? false : true}
        />
        <h1>공개여부</h1>
        <div>
          <label>
            <input
              type="radio"
              name="privacy"
              value="public"
              checked={!isPrivate}
              onChange={handleChange2}
            />
            공개
          </label>
          <label>
            <input
              type="radio"
              name="privacy"
              value="private"
              checked={isPrivate}
              onChange={handleChange2}
            />
            비공개
          </label>
        </div>
        {isPrivate && <InputLabel
          type="password"
          label="비밀번호"
          onChange={handleChange('password')}
          placeholder="영문, 숫자 8자이상의 비밀번호를 입력해주세요."
          errorMessage={errors.password}
          isValid={errors.password ? false : true}
        />} 
        <InputLabel
          label="태그"
          onChange={handleChange('tags')}
          placeholder="영문, 숫자 8자이상의 비밀번호를 입력해주세요."
          errorMessage={errors.tags}
          isValid={errors.tags ? false : true}
        />
        <Button fillColor isLoading={props.isLoading}>
          회원가입
        </Button>
      </ContainerForm>
    </RoomFormContainer>
  );
}

export default RoomForm;
