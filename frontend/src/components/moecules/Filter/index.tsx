import styled from 'styled-components';
import { FILTERS } from '../../../constans';

export type FilterProps = {
  currentSort: string | null;
  onChange: (v: string) => void;
};

export const Filter = ({ currentSort, onChange }: FilterProps) => {
  const isSet = (filter: string) =>
    currentSort === null && filter === 'newRoom'
      ? true
      : filter === currentSort;

  const handleFilterClick = (filter: string) => {
    onChange(filter);
  };

  return (
    <Container className="filters">
      {(Object.keys(FILTERS) as Array<keyof typeof FILTERS>).map(
        (filterKey, index) => (
          <FilterButton
            isSet={isSet(filterKey)}
            onClick={() => handleFilterClick(filterKey)}
            key={index}
          >
            {FILTERS[filterKey]}
          </FilterButton>
        )
      )}
    </Container>
  );
};

const Container = styled.div`
  gap: 20px;
  display: flex;
  font-size: 14px;

  span {
    display: flex;
    position: relative;
    cursor: pointer;

    &:not(:last-child) {
      &:after {
        content: '';
        position: absolute;
        top: 2px;
        right: -10px;
        width: 1px;
        height: 100%;
        background: rgb(153, 153, 153);
      }
    }
  }
`;

const FilterButton = styled.span<{ isSet: boolean }>`
  font-weight: 500;
  color: ${({ isSet }) => (isSet ? 'black' : 'rgb(153, 153, 153)')};
`;
