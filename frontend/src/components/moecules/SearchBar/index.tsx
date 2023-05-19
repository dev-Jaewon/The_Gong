import styled from 'styled-components';
import { RxMagnifyingGlass } from 'react-icons/rx';
import { AiFillCloseCircle } from 'react-icons/ai';

import { ChangeEvent, FormEvent, useState } from 'react';
import { useSearchParams, useLocation, useNavigate } from 'react-router-dom';

export const SearchBar = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [searchParams, setSearchParams] = useSearchParams();
  const [value, setValue] = useState<string>(searchParams.get('keyword') || '');

  const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {
    setValue(e.target.value);
  };

  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();

    if (location.pathname !== '/search') {
      navigate(`/search?keyword=${value}`);
    } else {
      setSearchParams({ keyword: value });
    }
  };

  return (
    <Container onSubmit={handleSubmit}>
      <input
        type="text"
        value={value}
        onChange={handleInputChange}
        placeholder="검색어를 입력해주세요."
      />
      {value && (
        <AiFillCloseCircle
          color="#cccccc"
          size={18}
          onClick={() => setValue('')}
        />
      )}
      <RxMagnifyingGlass size={30} />
    </Container>
  );
};

const Container = styled.form`
  padding: 5px 5px 5px 10px;
  display: flex;
  align-items: center;
  height: 40px;
  width: 100%;
  max-width: 400px;
  border: 1px solid #4fafb1;
  border-radius: 8px;

  input {
    flex: 1 1 auto;
    width: 100%;
    height: 100%;
    font-size: 15px;
    font-family: Noto Sans KR;
    border: none;
  }

  svg {
    cursor: pointer;
  }
`;
