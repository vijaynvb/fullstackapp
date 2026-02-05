export enum TaskStatus {
  TO_DO = 'TO_DO',
  IN_PROGRESS = 'IN_PROGRESS',
  BLOCKED = 'BLOCKED',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED',
}

export enum TaskPriority {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH',
  CRITICAL = 'CRITICAL',
}

export enum UserRole {
  USER = 'USER',
  MANAGER = 'MANAGER',
  ADMIN = 'ADMIN',
}

export enum AuthType {
  INTERNAL = 'INTERNAL',
  SSO = 'SSO',
}

export interface User {
  id: string
  username: string
  email: string
  firstName: string
  lastName: string
  role: UserRole
  active: boolean
}

export interface Task {
  id: string
  title: string
  description?: string
  status: TaskStatus
  priority: TaskPriority
  dueDate?: string
  assignee?: User
  assigneeId?: string
  createdBy: User
  createdById: string
  tags?: string[]
  overdue: boolean
  createdAt: string
  updatedAt: string
}

export interface Comment {
  id: string
  taskId: string
  text: string
  author: User
  createdAt: string
  updatedAt?: string
}

export interface TaskDetail extends Task {
  comments: Comment[]
  history: TaskHistory[]
}

export interface TaskHistory {
  id: string
  taskId: string
  action: string
  field?: string
  oldValue?: string
  newValue?: string
  performedBy: User
  performedAt: string
}

export interface CreateTaskRequest {
  title: string
  description?: string
  status?: TaskStatus
  priority?: TaskPriority
  dueDate?: string
  assigneeId?: string
  tags?: string[]
}

export interface UpdateTaskRequest {
  title?: string
  description?: string
  status?: TaskStatus
  priority?: TaskPriority
  dueDate?: string
  assigneeId?: string
  tags?: string[]
}

export interface TaskPageResponse {
  content: Task[]
  page: number
  size: number
  totalElements: number
  totalPages: number
}

export interface LoginRequest {
  type: AuthType
  username?: string
  password?: string
  ssoToken?: string
  rememberMe?: boolean
}

export interface SignupRequest {
  username: string
  email: string
  firstName: string
  lastName: string
  password: string
}

export interface AuthResponse {
  user: User
  token: string
  expiresAt: string
}

export interface ErrorResponse {
  error: string
  message: string
  field?: string
  timestamp: string
}
