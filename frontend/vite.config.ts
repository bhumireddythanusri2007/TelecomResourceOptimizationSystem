import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/dashboard': 'http://localhost:8080',
      '/network-cells': 'http://localhost:8080',
      '/devices': 'http://localhost:8080',
      '/subscribers': 'http://localhost:8080',
      '/traffic-flows': 'http://localhost:8080',
      '/resource-allocation': 'http://localhost:8080',
      '/network-slices': 'http://localhost:8080',
      '/edge-servers': 'http://localhost:8080',
      '/optimization': 'http://localhost:8080',
      '/kpi-monitoring': 'http://localhost:8080',
      '/users': 'http://localhost:8080',
      '/alerts': 'http://localhost:8080',
      '/reports': 'http://localhost:8080'
    }
  },
  build: {
    chunkSizeWarningLimit: 600,
    rollupOptions: {
      output: {
        manualChunks(id) {
          if (id.includes('node_modules/recharts')) return 'vendor-charts';
          if (id.includes('node_modules/lucide-react')) return 'vendor-icons';
          if (id.includes('node_modules/react') || id.includes('node_modules/react-dom') || id.includes('node_modules/scheduler')) {
            return 'vendor-react';
          }
        }
      }
    }
  }
});
