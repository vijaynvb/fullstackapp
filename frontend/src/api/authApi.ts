import { apiClient } from './apiClient'
import { LoginRequest, SignupRequest, AuthResponse } from './types'

export const authApi = {
  signup: async (data: SignupRequest): Promise<AuthResponse> => {
    const response = await apiClient.post<AuthResponse>('/auth/signup', data)
    return response.data
  },

  login: async (credentials: LoginRequest): Promise<AuthResponse> => {
    const response = await apiClient.post<AuthResponse>('/auth/login', credentials)
    return response.data
  },

  logout: async (): Promise<void> => {
    await apiClient.post('/auth/logout')
  },

  refreshSession: async (): Promise<AuthResponse> => {
    const response = await apiClient.post<AuthResponse>('/auth/refresh')
    return response.data
  },

  requestPasswordReset: async (email: string): Promise<void> => {
    await apiClient.post('/auth/password/reset', { email })
  },

  confirmPasswordReset: async (token: string, newPassword: string): Promise<void> => {
    await apiClient.post('/auth/password/reset/confirm', { token, newPassword })
  },
}
