import { useQuery, useMutation, useQueryClient } from 'react-query'
import { useParams, useNavigate } from 'react-router-dom'
import { taskApi } from '../api/taskApi'
import { TaskStatus } from '../api/types'
import './TaskDetailPage.css'

export const TaskDetailPage = () => {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const queryClient = useQueryClient()

  const { data: task, isLoading } = useQuery(
    ['task', id],
    () => taskApi.getTaskById(id!),
    { enabled: !!id }
  )

  const updateStatusMutation = useMutation(
    ({ status }: { status: TaskStatus }) =>
      taskApi.updateTaskStatus(id!, status, true),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['task', id])
        queryClient.invalidateQueries('tasks')
      },
    }
  )

  const deleteMutation = useMutation(() => taskApi.deleteTask(id!), {
    onSuccess: () => {
      queryClient.invalidateQueries('tasks')
      navigate('/tasks')
    },
  })

  if (isLoading) {
    return <div className="container">Loading...</div>
  }

  if (!task) {
    return <div className="container">Task not found</div>
  }

  return (
    <div className="task-detail-page">
      <div className="container">
        <div className="page-header">
          <h1>{task.title}</h1>
          <div>
            <button
              className="btn btn-danger"
              onClick={() => {
                if (window.confirm('Are you sure you want to delete this task?')) {
                  deleteMutation.mutate()
                }
              }}
            >
              Delete
            </button>
          </div>
        </div>

        <div className="card">
          <div className="task-info">
            <div className="info-row">
              <strong>Status:</strong>
              <select
                value={task.status}
                onChange={(e) =>
                  updateStatusMutation.mutate({ status: e.target.value as TaskStatus })
                }
                className="form-input"
              >
                {Object.values(TaskStatus).map((status) => (
                  <option key={status} value={status}>
                    {status}
                  </option>
                ))}
              </select>
            </div>
            <div className="info-row">
              <strong>Priority:</strong>
              <span>{task.priority}</span>
            </div>
            <div className="info-row">
              <strong>Assignee:</strong>
              <span>{task.assignee?.firstName || 'Unassigned'}</span>
            </div>
            <div className="info-row">
              <strong>Due Date:</strong>
              <span>{task.dueDate ? new Date(task.dueDate).toLocaleString() : 'Not set'}</span>
            </div>
          </div>

          {task.description && (
            <div className="task-description">
              <h3>Description</h3>
              <p>{task.description}</p>
            </div>
          )}

          <div className="task-comments">
            <h3>Comments ({task.comments?.length || 0})</h3>
            {task.comments?.map((comment) => (
              <div key={comment.id} className="comment">
                <div className="comment-header">
                  <strong>{comment.author.firstName} {comment.author.lastName}</strong>
                  <span>{new Date(comment.createdAt).toLocaleString()}</span>
                </div>
                <p>{comment.text}</p>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}
