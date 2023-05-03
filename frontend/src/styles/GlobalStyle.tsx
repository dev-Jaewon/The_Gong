import { createGlobalStyle } from 'styled-components';
import reset from 'styled-reset';
import '../assets/font/notoSans.css';

export const GlobalStyles = createGlobalStyle` 
  ${reset}

    * {
        box-sizing: border-box;
    }

    a {
        text-decoration: none;
        color: inherit;
    }

    input { 
      -moz-user-select: auto;
      -webkit-user-select: auto;
      -ms-user-select: auto;
      user-select: auto;

      &:focus {
         outline: none;
      }
    }

    button {
      border: none;
      background: none;
      padding: 0;
      cursor: pointer;
    }
`;
