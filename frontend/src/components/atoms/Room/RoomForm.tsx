import styled from "styled-components";
import { Input } from "../Input";
import useForm from "../../../hooks/useForm";
import { InputLabel } from "../../moecules/InputLabel";
import { Button } from "../Button";

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
  member_max_count: string;
  is_private: string;
  password: string;
  tags: string;
};

export type RoomFormProps = {
  isLoading: boolean;
  onSubmit: (value: RoomData) => void;
};

const RoomForm = (props: RoomFormProps) => {
  
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
      is_private: {
        required: {
          value: true,
          message: '공개 여부는 필수입력 항목입니다.',
        },
      },
      password: {
        required: {
          value: true,
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
    console.log(data)
    props.onSubmit(data);
  }

  return (
    <RoomFormContainer>
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
          label="인원 수"
          onChange={handleChange('member_max_count')}
          placeholder="영문, 숫자 8자이상의 비밀번호를 입력해주세요."
          errorMessage={errors.member_max_count}
          isValid={errors.member_max_count ? false : true}
        />
        <InputLabel
          label="공개 여부"
          onChange={handleChange('is_private')}
          placeholder="영문, 숫자 8자이상의 비밀번호를 입력해주세요."
          errorMessage={errors.is_private}
          isValid={errors.is_private ? false : true}
        />
        <InputLabel
          type="password"
          label="비밀번호"
          onChange={handleChange('password')}
          placeholder="영문, 숫자 8자이상의 비밀번호를 입력해주세요."
          errorMessage={errors.password}
          isValid={errors.password ? false : true}
        />
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
