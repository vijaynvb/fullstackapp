import { useQuery } from 'react-query'
import { Link } from 'react-router-dom'
import { taskApi } from '../api/taskApi'
import './DashboardPage.css'

export const DashboardPage = () => {
  const { data: tasks, isLoading } = useQuery('tasks', () =>
    taskApi.getTasks({ page: 0, size: 10 })
  )

  const user = JSON.parse(localStorage.getItem('user') || '{}')

  return (
    <div className="dashboard-page">
      <header className="dashboard-header">
        <h1>Dashboard</h1>
        <div className="user-info">
          <span>Welcome, {user.firstName} {user.lastName}</span>
          <Link to="/tasks/new" className="btn btn-primary">
            Create Task
          </Link>
        </div>
      </header>

      <div className="dashboard-content">
        <div className="dashboard-stats">
          <div className="stat-card">
            <h3>Total Tasks</h3>
            <p className="stat-number">{tasks?.totalElements || 0}</p>
          </div>
          <div className="stat-card">
            <h3>In Progress</h3>
            <p className="stat-number">
              {tasks?.content.filter(t => t.status === 'IN_PROGRESS').length || 0}
            </p>
          </div>
          <div className="stat-card">
            <h3>Completed</h3>
            <p className="stat-number">
              {tasks?.content.filter(t => t.status === 'COMPLETED').length || 0}
            </p>
          </div>
          <div className="stat-card">
            <h3>Overdue</h3>
            <p className="stat-number">
              {tasks?.content.filter(t => t.overdue).length || 0}
            </p>
          </div>
        </div>

        <div className="card">
          <h2>Recent Tasks</h2>
          {isLoading ? (
            <p>Loading...</p>
          ) : (
            <ul className="task-list">
              {tasks?.content.map((task) => (
                <li key={task.id} className="task-item">
                  <Link to={`/tasks/${task.id}`}>
                    <h4>{task.title}</h4>
                    <span className={`status-badge status-${task.status.toLowerCase()}`}>
                      {task.status}
                    </span>
                  </Link>
                </li>
              ))}
            </ul>
          )}
          <Link to="/tasks" className="btn btn-secondary">
            View All Tasks
          </Link>
        </div>
      </div>
    </div>
  )
}
