import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { authApi } from '../api/authApi'
import { AuthType } from '../api/types'
import './LoginPage.css'

const loginSchema = z.object({
  type: z.nativeEnum(AuthType),
  username: z.string().min(1, 'Username is required'),
  password: z.string().min(1, 'Password is required'),
  ssoToken: z.string().optional(),
  rememberMe: z.boolean().default(false),
})

type LoginFormData = z.infer<typeof loginSchema>

export const LoginPage = () => {
  const navigate = useNavigate()
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      type: AuthType.INTERNAL,
      rememberMe: false,
    },
  })

  const onSubmit = async (data: LoginFormData) => {
    setError(null)
    setLoading(true)

    try {
      // Ensure type is always included
      const loginData = {
        ...data,
        type: AuthType.INTERNAL,
      }
      
      console.log('Attempting login with:', { username: loginData.username, type: loginData.type })
      
      const response = await authApi.login(loginData)
      
      // Validate response has required fields
      if (!response.user) {
        throw new Error('Login response missing user data')
      }
      if (!response.user.id) {
        console.error('Login response user missing ID:', response.user)
        throw new Error('Login response user missing ID field')
      }
      
      // Store auth data
      localStorage.setItem('authToken', response.token)
      localStorage.setItem('user', JSON.stringify(response.user))
      
      // Verify storage
      const storedUser = JSON.parse(localStorage.getItem('user') || '{}')
      console.log('✓ Login successful - User stored:', {
        id: storedUser.id,
        username: storedUser.username,
        role: storedUser.role
      })
      
      if (!storedUser.id) {
        console.error('✗ Failed to store user ID in localStorage!')
        throw new Error('Failed to store user data')
      }
      
      navigate('/dashboard')
    } catch (err: any) {
      console.error('Login error:', err)
      const errorMessage = err.response?.data?.message 
        || err.response?.data?.error 
        || err.message 
        || 'Login failed. Please check your credentials.'
      setError(errorMessage)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="login-page">
      <div className="login-container">
        <h1>To-Do Application</h1>
        <h2>Sign In</h2>
        {error && <div className="error-message">{error}</div>}
        <form onSubmit={handleSubmit(onSubmit)}>
          <input type="hidden" {...register('type')} value={AuthType.INTERNAL} />
          <div className="form-group">
            <label className="form-label">Username</label>
            <input
              type="text"
              className="form-input"
              {...register('username')}
            />
            {errors.username && (
              <span className="error-message">{errors.username.message}</span>
            )}
          </div>
          <div className="form-group">
            <label className="form-label">Password</label>
            <input
              type="password"
              className="form-input"
              {...register('password')}
            />
            {errors.password && (
              <span className="error-message">{errors.password.message}</span>
            )}
          </div>
          <div className="form-group">
            <label>
              <input type="checkbox" {...register('rememberMe')} />
              Remember me
            </label>
          </div>
          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? 'Signing in...' : 'Sign In'}
          </button>
        </form>
      </div>
    </div>
  )
}
