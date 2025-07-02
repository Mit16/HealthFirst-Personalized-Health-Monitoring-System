// src/api.js
import axios from "axios";

export const authApi = axios.create({
  baseURL: import.meta.env.VITE_API_AUTH,
});

export const userApi = axios.create({
  baseURL: import.meta.env.VITE_API_USER,
});

export const metricsApi = axios.create({
  baseURL: import.meta.env.VITE_API_METRICS,
});

export const predictionApi = axios.create({
  baseURL: import.meta.env.VITE_API_PREDICTION,
});

export const alertApi = axios.create({
  baseURL: import.meta.env.VITE_API_ALERT,
});

// Attach token to all clients
const attachAuthInterceptor = (instance) => {
  instance.interceptors.request.use((config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  });
};

// Attach to each client
[authApi, userApi, metricsApi, predictionApi, alertApi].forEach(attachAuthInterceptor);

export default metricsApi; // default export if someone uses just `api`
