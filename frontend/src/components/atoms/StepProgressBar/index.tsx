import styled from 'styled-components';

export type StepProgressBar = {
  steps: Array<string>;
  currentStep: number;
};

export const StepProgressBar = ({ steps, currentStep }: StepProgressBar) => {
  return (
    <Container>
      <List>
        {steps.map((step, index) => (
          <ListItem index={index} currentIndex={currentStep} key={index}>
            <p className="circle">{index + 1}</p>
            <p>{step}</p>
          </ListItem>
        ))}
      </List>
    </Container>
  );
};

const Container = styled.div`
  width: 100%;
`;

const List = styled.ul`
  display: flex;
  justify-content: space-between;
`;

const ListItem = styled.li<{ index: number; currentIndex: number }>`
  list-style-type: none;
  width: 100%;
  font-size: 12px;
  position: relative;
  text-align: center;
  text-transform: uppercase;
  color: #7d7d7d;

  .circle {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 30px;
    height: 30px;
    margin: 0 auto 10px auto;
    border-radius: 50%;
    background-color: white;
    border: ${({ index, currentIndex }) =>
      index < currentIndex ? '2px solid #55b776' : '2px solid #7d7d7d'};
  }

  &:after {
    content: '';
    width: 100%;
    height: 2px;
    position: absolute;
    top: 15px;
    left: -50%;
    z-index: -1;
    background-color: ${({ index, currentIndex }) =>
      index <= currentIndex ? '#55b776' : '#7d7d7d'};
  }

  &:first-child:after {
    content: none;
  }
`;
