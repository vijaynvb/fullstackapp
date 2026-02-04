/**
 * Authentication utility functions
 */

export interface StoredUser {
  id: string
  username: string
  email: string
  firstName: string
  lastName: string
  role: string
  active: boolean
}

/**
 * Get the current user from localStorage
 */
export const getCurrentUser = (): StoredUser | null => {
  try {
    const userStr = localStorage.getItem('user')
    if (!userStr) {
      return null
    }
    const user = JSON.parse(userStr) as StoredUser
    return user
  } catch (error) {
    console.error('Failed to get current user:', error)
    return null
  }
}

/**
 * Get the current user ID from localStorage
 */
export const getCurrentUserId = (): string | null => {
  const user = getCurrentUser()
  return user?.id || null
}

/**
 * Check if user is authenticated
 */
export const isAuthenticated = (): boolean => {
  const token = localStorage.getItem('authToken')
  const user = getCurrentUser()
  return !!(token && user && user.id)
}

/**
 * Clear authentication data
 */
export const clearAuth = (): void => {
  localStorage.removeItem('authToken')
  localStorage.removeItem('user')
}
