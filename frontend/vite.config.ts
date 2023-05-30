import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { viteCommonjs } from '@originjs/vite-plugin-commonjs';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react(), viteCommonjs()],
  server: {
    port: 3000,
  },
  build: {
    chunkSizeWarningLimit: 1000, // 크기 제한 값 설정 
  },
});
