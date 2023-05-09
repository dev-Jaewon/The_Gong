import { render } from '@testing-library/react';
import { Button, ButtonProps } from '.';
import { DEFAULT_BUTTON_HEIGHT, DEFAULT_BUTTON_WIDTH } from '../../../constans';

describe('<Button />', () => {
  const ins = (props: ButtonProps = {}) => {
    const { getByRole, queryByLabelText } = render(<Button {...props} />);

    const button = getByRole('button') as HTMLInputElement;
    const spinner = queryByLabelText('spinner');

    return { button, spinner };
  };

  test('render', () => {
    const { button } = ins({ children: '버튼' });
    expect(button).toBeInTheDocument();
    expect(button.textContent).toBe('버튼');
  });

  test('props.fill = true', () => {
    const { button } = ins({ fillColor: true });
    expect(button).toHaveStyle(`color: white; background-color: #4fafb1;`);
  });

  test('props.fill = false', () => {
    const { button } = ins({ fillColor: false });
    expect(button).toHaveStyle(`color: black; background-color: ButtonFace;`);
  });

  test('not props.width', () => {
    const { button } = ins();
    expect(button).toHaveStyle(`width: ${DEFAULT_BUTTON_WIDTH}`);
  });

  test('props.width = 100px', () => {
    const { button } = ins({ width: '100px' });
    expect(button).toHaveStyle(`width: 100px`);
  });

  test('not props.height', () => {
    const { button } = ins();
    expect(button).toHaveStyle(`height: ${DEFAULT_BUTTON_HEIGHT}`);
  });

  test('props.height = 100px', () => {
    const { button } = ins({ height: '100px' });
    expect(button).toHaveStyle(`height: 100px`);
  });

  test('props.outline = true', () => {
    const { button } = ins({ outline: true });
    expect(button).toHaveStyle(`outline: 1px solid #4fafb1;`);
  });

  test('props.outline = false', () => {
    const { button } = ins({ outline: false });
    expect(button).toHaveStyle(`outline: unset;`);
  });

  test('props.isLoading = true', () => {
    const { spinner } = ins({ isLoading: true });
    expect(spinner).toBeInTheDocument();
  });

  test('props.isLoading = false', () => {
    const { spinner } = ins({ isLoading: false });
    expect(spinner).not.toBeInTheDocument();
  });
});
