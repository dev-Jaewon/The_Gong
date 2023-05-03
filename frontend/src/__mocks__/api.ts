import { rest } from 'msw';

export const handlers = [
  rest.get('/sample', (_, res, ctx) => {
    return res(ctx.delay(500), ctx.status(200), ctx.json({ result: true }));
  }),

  rest.post('/sample', async (req, res, ctx) => {
    const data = await req.json();
    return res(ctx.status(200), ctx.json({ result: data }));
  }),
];
