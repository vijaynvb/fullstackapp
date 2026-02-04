import { apiClient } from './apiClient'
import {
  Task,
  TaskDetail,
  CreateTaskRequest,
  UpdateTaskRequest,
  TaskPageResponse,
  TaskStatus,
} from './types'

export const taskApi = {
  // Get all tasks with filters
  getTasks: async (params?: {
    status?: TaskStatus
    priority?: string
    assigneeId?: string
    createdById?: string
    tags?: string[]
    overdue?: boolean
    search?: string
    sortBy?: string
    sortOrder?: string
    page?: number
    size?: number
  }): Promise<TaskPageResponse> => {
    const response = await apiClient.get<TaskPageResponse>('/tasks', { params })
    return response.data
  },

  // Get task by ID
  getTaskById: async (taskId: string): Promise<TaskDetail> => {
    const response = await apiClient.get<TaskDetail>(`/tasks/${taskId}`)
    return response.data
  },

  // Create new task
  createTask: async (task: CreateTaskRequest): Promise<Task> => {
    const response = await apiClient.post<Task>('/tasks', task)
    return response.data
  },

  // Update task
  updateTask: async (taskId: string, task: UpdateTaskRequest): Promise<Task> => {
    const response = await apiClient.put<Task>(`/tasks/${taskId}`, task)
    return response.data
  },

  // Delete task
  deleteTask: async (taskId: string): Promise<void> => {
    await apiClient.delete(`/tasks/${taskId}`)
  },

  // Assign task
  assignTask: async (
    taskId: string,
    assigneeId: string,
    notifyAssignee: boolean = true
  ): Promise<Task> => {
    const response = await apiClient.post<Task>(`/tasks/${taskId}/assign`, {
      assigneeId,
      notifyAssignee,
    })
    return response.data
  },

  // Update task status
  updateTaskStatus: async (
    taskId: string,
    status: TaskStatus,
    notify: boolean = true
  ): Promise<Task> => {
    const response = await apiClient.put<Task>(`/tasks/${taskId}/status`, {
      status,
      notify,
    })
    return response.data
  },
}
