import { fireEvent, render } from '@testing-library/react';
import { Input, InputProps } from '.';
import userEvent from '@testing-library/user-event';

describe('<Input />', () => {
  const ins = (props: InputProps = {}) => {
    const { getByRole } = render(<Input {...props} />);

    const inputbox = getByRole('textbox') as HTMLInputElement;

    return { inputbox };
  };

  test('render', () => {
    const { inputbox } = ins({ type: 'text' });
    expect(inputbox).toBeInTheDocument();
  });

  test('onFocus', async () => {
    const user = userEvent.setup();
    const { inputbox } = ins({ type: 'text' });

    expect(inputbox).toHaveStyle(`border: 1px solid  #D3D3D3`);

    await user.click(inputbox);

    expect(inputbox).toHaveStyle(`border: 1px solid #4fafb1`);
  });

  test('props.onChange', () => {
    const onChange = jest.fn();
    const { inputbox } = ins({ type: 'text', onChange });
    const testContent = 'input value test';

    fireEvent.change(inputbox, { target: { value: testContent } });

    expect(inputbox.value).toBe(testContent);
  });

  test('props.isValid: true ', () => {
    const { inputbox } = ins({ type: 'text', isValid: true });
    expect(inputbox).toHaveStyle(`border: 1px solid  #D3D3D3`);
  });

  test('props.isValid: false ', () => {
    const { inputbox } = ins({ type: 'text', isValid: false });
    expect(inputbox).toHaveStyle(`border: 1px solid  red`);
  });
});
