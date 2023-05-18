import styled from 'styled-components';
import { SearchListItem, SearchItem } from '../../moecules/SearchListItem';
import { Pagenation } from '../../moecules/Pagenation';

export type SearchData = {
  pageInfo: {
    page: number;
    size: number;
    total_elements: number;
    total_pages: number;
  };
  data: SearchItem[];
};

export type SearchListProps = SearchData & {
  onChnagePage: (page: number) => void;
};

export const SearchList = (props: SearchListProps) => {
  const handlePageClick = (page: number) => {
    props.onChnagePage(page);
  };

  return (
    <Container>
      {props.data.map((study) => (
        <SearchListItem {...study} key={study.room_id} />
      ))}

      <PageNationContainer>
        <Pagenation
          size={props.pageInfo.total_pages}
          onPageChange={handlePageClick}
          initialPage={1}
        />
      </PageNationContainer>
    </Container>
  );
};

const Container = styled.div`
  width: 100%;
  font-family: Noto Sans KR;
`;

const PageNationContainer = styled.div`
  padding-top: 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  &:before {
    content: '';
    width: 100%;
    height: 1px;
    background: #dddee3;
    margin-bottom: 40px;
  }
`;
