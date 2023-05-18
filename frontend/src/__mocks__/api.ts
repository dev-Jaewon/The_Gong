import { rest } from 'msw';
import { searchData } from './data/search';

export const handlers = [
  rest.get('/sample', (_, res, ctx) => {
    return res(ctx.delay(500), ctx.status(200), ctx.json({ result: true }));
  }),

  rest.post('/sample', async (req, res, ctx) => {
    const data = await req.json();
    return res(ctx.status(200), ctx.json({ result: data }));
  }),

  rest.post('/signup', async (req, res, ctx) => {
    const data = await req.json();
    return res(ctx.delay(1500), ctx.json({ result: data }));
  }),

  rest.get('/search?:query', async (req, res, ctx) => {
    return res(ctx.json(searchData));
  }),
];
