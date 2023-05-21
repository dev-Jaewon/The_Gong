import styled from 'styled-components';

export type SkeletonType = {
  width?: string;
  height?: string;
  margin?: string;
};

export const Skeleton = (props: SkeletonType) => {
  return <SkeletonContainer {...props} aria-label="로딩상태" />;
};

const SkeletonContainer = styled.div<SkeletonType>`
  margin: ${({ margin }) => (margin ? margin : 'unset')};
  height: ${({ height }) => (height ? `${height}` : '100%')};
  width: ${({ width }) => (width ? `${width}` : '100%')};
  border-radius: 4px;
  background-color: #e4e4e4;
  animation: loading 1s infinite;

  @keyframes loading {
    0% {
      opacity: 0.5;
    }
    50% {
      opacity: 1;
    }
    100% {
      opacity: 0.5;
    }
  }
`;
