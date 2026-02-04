import axios from 'axios'
import { getCurrentUserId } from '../utils/auth'

// Use proxy in development (via Vite), direct URL in production
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 
  (import.meta.env.DEV ? '/api/v1' : 'http://localhost:8080/api/v1')

export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true, // For cookie-based authentication
})

// Request interceptor
apiClient.interceptors.request.use(
  (config) => {
    // Add auth token if available
    const token = localStorage.getItem('authToken')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    
    // Add user ID from localStorage for authentication
    // This is REQUIRED for backend authentication
    const userId = getCurrentUserId()
    
    if (userId) {
      // Set the header - ensure it's a string
      config.headers['X-User-Id'] = String(userId).trim()
      console.log('[API Client] ✓ Added X-User-Id header:', userId)
    } else {
      console.warn('[API Client] ⚠ No user ID found in localStorage - authentication will fail')
      console.warn('[API Client] Make sure you are logged in')
      console.warn('[API Client] Current localStorage user:', localStorage.getItem('user'))
    }
    
    // Log request headers in development (always log for debugging)
    console.log(`[API Request] ${config.method?.toUpperCase()} ${config.baseURL}${config.url}`, {
      hasData: !!config.data,
      headers: {
        'X-User-Id': config.headers['X-User-Id'] || 'MISSING',
        'Authorization': config.headers['Authorization'] ? 'Bearer ***' : 'MISSING'
      }
    })
    
    // Validate that user ID is set for authenticated endpoints (not auth endpoints)
    const isAuthEndpoint = config.url?.includes('/auth/') || config.url?.includes('/login')
    if (!isAuthEndpoint && !config.headers['X-User-Id']) {
      console.error('[API Client] ⚠ WARNING: X-User-Id header is missing for authenticated endpoint!')
      console.error('[API Client] Request will likely fail with 403 Forbidden')
    }
    
    return config
  },
  (error) => {
    console.error('[API Client] Request interceptor error:', error)
    return Promise.reject(error)
  }
)

// Response interceptor
apiClient.interceptors.response.use(
  (response) => {
    // Log response in development
    if (import.meta.env.DEV) {
      console.log(`[API Response] ${response.config.method?.toUpperCase()} ${response.config.url}`, response.status, response.data)
    }
    return response
  },
  (error) => {
    // Log error in development
    if (import.meta.env.DEV) {
      console.error(`[API Error] ${error.config?.method?.toUpperCase()} ${error.config?.url}`, {
        status: error.response?.status,
        data: error.response?.data,
        message: error.message
      })
    }
    
    if (error.response?.status === 401) {
      // Clear auth data on 401 (Unauthorized)
      console.warn('[API Client] 401 Unauthorized - clearing auth data')
      localStorage.removeItem('authToken')
      localStorage.removeItem('user')
      // Only redirect if not already on login page
      if (window.location.pathname !== '/login') {
        window.location.href = '/login'
      }
    } else if (error.response?.status === 403) {
      // 403 Forbidden - usually means authentication failed
      console.error('[API Client] 403 Forbidden - authentication issue')
      console.error('[API Client] Current user ID:', getCurrentUserId())
      console.error('[API Client] Error details:', error.response?.data)
      
      // Check if user ID is missing
      const userId = getCurrentUserId()
      if (!userId) {
        console.error('[API Client] User ID is missing! Redirecting to login...')
        localStorage.removeItem('authToken')
        localStorage.removeItem('user')
        if (window.location.pathname !== '/login') {
          window.location.href = '/login'
        }
      }
    }
    return Promise.reject(error)
  }
)
