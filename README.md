# To-Do Application - Full Stack Scaffolding

This is a full-stack application scaffolding generated from the OpenAPI specification. The project includes:

- **Backend**: Java Spring Boot application with Clean Architecture
- **Frontend**: React + TypeScript application

## Project Structure

```
fullstackapp/
├── backend/          # Spring Boot backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/todoapp/
│   │   │   │   ├── controller/    # REST controllers
│   │   │   │   ├── service/       # Business logic services
│   │   │   │   ├── repository/    # Data access layer
│   │   │   │   ├── domain/        # Domain entities and enums
│   │   │   │   ├── dto/           # Data Transfer Objects
│   │   │   │   ├── mapper/        # MapStruct mappers
│   │   │   │   ├── exception/     # Custom exceptions
│   │   │   │   └── config/        # Configuration classes
│   │   │   └── resources/
│   │   │       └── application.yml
│   │   └── test/                  # Unit tests
│   └── pom.xml
│
└── frontend/        # React frontend
    ├── src/
    │   ├── api/           # API client and types
    │   ├── components/    # React components
    │   ├── pages/         # Page components
    │   └── App.tsx
    ├── package.json
    └── vite.config.ts
```

## Prerequisites

### Backend
- Java 17 or higher
- Maven 3.6+

### Frontend
- Node.js 18+ 
- npm or yarn

## Getting Started

### Backend Setup

1. Navigate to backend directory:
```bash
cd backend
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

- API Documentation: `http://localhost:8080/swagger-ui.html`
- H2 Console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:tododb`)

### Frontend Setup

1. Navigate to frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start development server:
```bash
npm run dev
```

The frontend will start on `http://localhost:3000`

## API Endpoints

### Authentication
- `POST /api/v1/auth/login` - User login
- `POST /api/v1/auth/logout` - User logout
- `POST /api/v1/auth/refresh` - Refresh session
- `POST /api/v1/auth/password/reset` - Request password reset
- `POST /api/v1/auth/password/reset/confirm` - Confirm password reset

### Tasks
- `GET /api/v1/tasks` - List tasks (with filters)
- `GET /api/v1/tasks/{id}` - Get task details
- `POST /api/v1/tasks` - Create task
- `PUT /api/v1/tasks/{id}` - Update task
- `DELETE /api/v1/tasks/{id}` - Delete task
- `POST /api/v1/tasks/{id}/assign` - Assign task
- `PUT /api/v1/tasks/{id}/status` - Update task status

### Users (Admin only)
- `GET /api/v1/users` - List users
- `GET /api/v1/users/{id}` - Get user
- `POST /api/v1/users` - Create user
- `PUT /api/v1/users/{id}` - Update user
- `DELETE /api/v1/users/{id}` - Delete user

## Implementation Roadmap

### Backend TODOs

#### High Priority
- [ ] **Session Management**: Implement proper session management with Spring Session
- [ ] **Authentication**: Complete SSO integration
- [ ] **Authorization**: Implement role-based access control filters
- [ ] **Password Reset**: Complete password reset flow with email tokens
- [ ] **Task State Machine**: Implement state transition validation
- [ ] **Notifications**: Implement email notification service
- [ ] **Task History**: Implement audit trail logging
- [ ] **Tag Management**: Complete tag creation and association logic
- [ ] **Comments**: Implement comment CRUD operations
- [ ] **Dashboard Service**: Implement dashboard metrics aggregation
- [ ] **Report Service**: Implement report generation (CSV, PDF export)

#### Medium Priority
- [ ] **Search**: Implement full-text search functionality
- [ ] **Filtering**: Complete advanced filtering (tags, date ranges)
- [ ] **Pagination**: Optimize pagination queries
- [ ] **Caching**: Add Redis caching for frequently accessed data
- [ ] **Validation**: Add custom validators for business rules
- [ ] **Error Handling**: Enhance error messages and logging
- [ ] **Unit Tests**: Complete unit test coverage (target: 80%)
- [ ] **Integration Tests**: Add integration tests for API endpoints

#### Low Priority
- [ ] **Scheduled Tasks**: Implement overdue task checker job
- [ ] **Reminder Service**: Implement due date reminder scheduler
- [ ] **Export Service**: Implement CSV/PDF export functionality
- [ ] **Performance**: Optimize database queries (N+1 problem)
- [ ] **Documentation**: Add JavaDoc comments
- [ ] **API Versioning**: Implement API versioning strategy

### Frontend TODOs

#### High Priority
- [ ] **Authentication**: Complete login/logout flow with proper session handling
- [ ] **Protected Routes**: Implement proper authentication check
- [ ] **Task Forms**: Complete task creation and editing forms
- [ ] **Task List**: Implement filtering, sorting, and pagination UI
- [ ] **Task Detail**: Complete task detail view with comments
- [ ] **Comment System**: Implement add/edit/delete comments
- [ ] **User Management**: Create user management pages (Admin)
- [ ] **Dashboard**: Complete dashboard with real metrics
- [ ] **Error Handling**: Add global error boundary and error messages
- [ ] **Loading States**: Add loading indicators for async operations

#### Medium Priority
- [ ] **Form Validation**: Enhance form validation with better UX
- [ ] **Date Pickers**: Add date/time picker components
- [ ] **Tag Management**: Implement tag selection and creation UI
- [ ] **Search**: Add search functionality UI
- [ ] **Notifications**: Add toast notifications for success/error
- [ ] **Responsive Design**: Ensure mobile responsiveness
- [ ] **Accessibility**: Add ARIA labels and keyboard navigation
- [ ] **State Management**: Consider Redux for complex state
- [ ] **Unit Tests**: Add component unit tests
- [ ] **E2E Tests**: Add end-to-end tests with Cypress/Playwright

#### Low Priority
- [ ] **Theme Support**: Implement dark/light theme toggle
- [ ] **Internationalization**: Add i18n support
- [ ] **Performance**: Optimize bundle size and lazy loading
- [ ] **PWA**: Add Progressive Web App features
- [ ] **Offline Support**: Implement offline functionality
- [ ] **Analytics**: Add usage analytics

## Development Commands

### Backend
```bash
# Run application
mvn spring-boot:run

# Run tests
mvn test

# Build JAR
mvn clean package

# Run with profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Frontend
```bash
# Development server
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview

# Lint code
npm run lint
```

## Database

The application uses H2 in-memory database for MVP. To access the H2 console:

1. Start the backend application
2. Navigate to `http://localhost:8080/h2-console`
3. Use these credentials:
   - JDBC URL: `jdbc:h2:mem:tododb`
   - Username: `sa`
   - Password: (empty)

## Configuration

### Backend Configuration (`application.yml`)
- Server port: 8080
- API base path: `/api/v1`
- Database: H2 in-memory
- Swagger UI: `/swagger-ui.html`

### Frontend Configuration
- Development server: `http://localhost:3000`
- API proxy: `/api` → `http://localhost:8080`

## Next Steps

1. **Set up database**: Create initial schema and seed data
2. **Implement authentication**: Complete login/logout flow
3. **Add sample data**: Create test users and tasks
4. **Implement core features**: Start with task CRUD operations
5. **Add tests**: Write unit and integration tests
6. **Deploy**: Set up CI/CD pipeline

## Notes

- This is a scaffolding/prototype. Production-ready features need to be implemented.
- Security configurations are basic and need enhancement for production.
- Error handling is minimal and should be enhanced.
- Most service methods have TODO comments indicating what needs to be implemented.

## Support

For issues or questions, refer to:
- Backend: Spring Boot documentation
- Frontend: React documentation
- API: OpenAPI specification in `openapi.yaml`
