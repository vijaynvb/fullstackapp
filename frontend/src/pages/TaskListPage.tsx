import { useQuery } from 'react-query'
import { Link } from 'react-router-dom'
import { taskApi } from '../api/taskApi'
import './TaskListPage.css'

export const TaskListPage = () => {
  const { data: tasks, isLoading } = useQuery('tasks', () =>
    taskApi.getTasks({ page: 0, size: 25 })
  )

  return (
    <div className="task-list-page">
      <div className="container">
        <div className="page-header">
          <h1>Tasks</h1>
          <Link to="/tasks/new" className="btn btn-primary">
            Create Task
          </Link>
        </div>

        {isLoading ? (
          <div className="card">Loading...</div>
        ) : (
          <div className="card">
            <table className="task-table">
              <thead>
                <tr>
                  <th>Title</th>
                  <th>Status</th>
                  <th>Priority</th>
                  <th>Assignee</th>
                  <th>Due Date</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {tasks?.content.map((task) => (
                  <tr key={task.id}>
                    <td>
                      <Link to={`/tasks/${task.id}`}>{task.title}</Link>
                    </td>
                    <td>
                      <span className={`status-badge status-${task.status.toLowerCase()}`}>
                        {task.status}
                      </span>
                    </td>
                    <td>
                      <span className={`priority-badge priority-${task.priority.toLowerCase()}`}>
                        {task.priority}
                      </span>
                    </td>
                    <td>{task.assignee?.firstName || 'Unassigned'}</td>
                    <td>{task.dueDate ? new Date(task.dueDate).toLocaleDateString() : '-'}</td>
                    <td>
                      <Link to={`/tasks/${task.id}`} className="btn btn-secondary btn-sm">
                        View
                      </Link>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  )
}
