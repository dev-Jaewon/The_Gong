import styled from 'styled-components';
import { api } from '../../util/api';
import { useQuery } from '@tanstack/react-query';
import { useLocation, useSearchParams } from 'react-router-dom';
import { Filter } from '../../components/moecules/Filter';
import { Header } from '../../components/organisms/Header';
import { formatQueryString } from '../../util/formatQueryString';
import { SearchData, SearchList } from '../../components/organisms/SearchList';
import { LodaingSpinner } from '../../components/atoms/LoadingSpinner';

export const Search = () => {
  const location = useLocation();
  const [params, setPrams] = useSearchParams();

  const { data, isFetching } = useQuery(['search', location], () =>
    api<SearchData>(`/search${location.search}&page=1&sort=newRoom`).then(
      (res) => res.data
    )
  );

  const handleFilterChange = (filter: string) => {
    setPrams(formatQueryString(location.search, 'sort', filter));
  };

  const handlePageChange = (page: number) => {
    setPrams(formatQueryString(location.search, 'page', page.toString()));
  };

  return (
    <Container>
      <Header />
      <Content>
        {isFetching ? (
          <LodaingSpinner />
        ) : data?.data.length ? (
          <>
            <SearchResult>
              '{<span className="keyword">{params.get('keyword')}</span>}
              '에 대한 검색결과
            </SearchResult>
            <FilterContainer>
              <p className="counts">총 {data.page_info.total_elements}건</p>
              <Filter
                currentSort={params.get('sort')}
                onChange={handleFilterChange}
              />
            </FilterContainer>
            <SearchList
              data={data.data}
              page_info={data.page_info}
              onChnagePage={handlePageChange}
            />
          </>
        ) : (
          <SearchResult>
            '{<span className="keyword">{params.get('keyword')}</span>}
            '에 대한 검색결과 없습니다.
          </SearchResult>
        )}
      </Content>
    </Container>
  );
};

const Container = styled.div`
  display: flex;
  align-items: center;
  flex-direction: column;
  font-family: Noto Sans KR;
`;

const Content = styled.div`
  display: flex;
  align-items: center;
  flex-direction: column;
  width: 100%;
  padding: 50px 0;
  max-width: 1050px;
`;

const SearchResult = styled.h2`
  text-align: center;
  font-size: 25px;
  font-weight: 500;
  padding-bottom: 20px;

  .keyword {
    color: #4fafb1;
  }
`;

const FilterContainer = styled.div`
  display: flex;
  justify-content: space-between;
  width: 100%;
  margin-top: 30px;

  .counts {
    padding: 0 20px;
    font-size: 14px;
    font-weight: 400;
  }
`;
