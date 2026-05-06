import axios from 'axios';

// Create base axios instance
export const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add interceptor to include JWT token and Booth ID
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  const boothId = localStorage.getItem('boothId');
  
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  
  // Always send booth ID for audit logs
  config.headers['X-Booth-Id'] = boothId;
  
  return config;
});

// Response interceptor for error handling
api.interceptors.response.use(
  (response) => response,
  (error) => {
    // Provide a standardized error message string
    const message = error.response?.data?.message || 'An unexpected error occurred';
    return Promise.reject(new Error(message));
  }
);
