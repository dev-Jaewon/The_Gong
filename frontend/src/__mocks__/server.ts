import { setupServer } from 'msw/node';
import { handlers } from './api';

exports.server = setupServer(...handlers);
