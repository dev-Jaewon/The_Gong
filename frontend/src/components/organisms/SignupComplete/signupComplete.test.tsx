import { fireEvent, render, screen } from '@testing-library/react';
import { SignupComplete } from '.';

const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockNavigate,
}));

describe('<signupComplete />', () => {
  beforeEach(() => {
    render(<SignupComplete />);
  });

  test('render', () => {
    const { getByText, getByRole } = screen;

    expect(getByRole('button')).toBeInTheDocument();
    expect(getByText(/회원가입이 완료 되었습니다./)).toBeInTheDocument();
    expect(getByText(/님의 회원가입을 축하합니다./)).toBeInTheDocument();
    expect(
      getByText(/알차고 실속있는 서비스로 찾아뵙겠습니다./)
    ).toBeInTheDocument();
  });

  test('change route path', () => {
    const { getByRole } = screen;

    fireEvent.click(getByRole('button'));
    expect(mockNavigate).toBeCalled();
  });
});
