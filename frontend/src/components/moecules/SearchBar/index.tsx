import styled from 'styled-components';
import { TfiSearch } from 'react-icons/tfi';
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
          onClick={() => setValue('')}
          className='AiFillCloseCircle'
        />
      )}
      <TfiSearch className='TfiSearch'/>
    </Container>
  );
};

const Container = styled.form`
  padding: 0.4rem 0.5rem;
  display: flex;
  align-items: center;
  height: 40px;
  width: 100%;
  max-width: 400px;
  border: 1px solid #4fafb1;
  border-radius: 0.2rem;

  input {
    flex: 1 1 auto;
    width: 100%;
    height: 100%;
    font-size: 0.9rem;
    font-weight: 300;
    font-family: Noto Sans KR;
    border: none;
  }

  input::placeholder {
    color: #aaa;
    font-size: 0.9rem; 
  }

  .AiFillCloseCircle{
    color: #555555;
    font-size: 1.3rem;
    margin-right: 0.4rem;
  }

  .TfiSearch{
    color: #555555;
    font-size: 1.5rem;
  }

  @media screen and (max-width: 1024px) {
    padding: 0.4rem 0.4rem;
    height: 2rem;


    input {
      font-size: 0.8rem;
    }

    input::placeholder {
      color: #aaa;
      font-size: 0.8rem; 
    }

    .AiFillCloseCircle{
      font-size: 1rem;
    }

    .TfiSearch{
      font-size: 1rem;
    }
  }

  svg {
    cursor: pointer;
  }
`;
