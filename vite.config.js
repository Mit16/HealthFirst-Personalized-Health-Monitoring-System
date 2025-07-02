import { defineConfig } from 'vite'
import tailwindcss from '@tailwindcss/vite'
import react from '@vitejs/plugin-react'


// https://vite.dev/config/
export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    historyApiFallback: true, // <-- add this
    proxy: {
      // '/auth': 'http://localhost:8081',
      '/user': 'http://localhost:8082',
      '/metrics': 'http://localhost:8083',
      '/api': 'http://localhost:8084' // prediction-service
    }
  },
})
