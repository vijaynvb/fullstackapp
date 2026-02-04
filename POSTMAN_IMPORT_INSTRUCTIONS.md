# Postman Collection Import Instructions

## Overview
This document provides step-by-step instructions for importing the To-Do Application API Postman Collection into Postman.

## Prerequisites
- Postman application installed (Desktop app or web version)
- Access to the API server (development or production)

## Import Steps

### Method 1: Import via File (Recommended)

1. **Open Postman**
   - Launch the Postman desktop application or open Postman web

2. **Import Collection**
   - Click the **Import** button in the top-left corner
   - Select **File** tab
   - Click **Upload Files** or drag and drop `todo-app-postman-collection.json`
   - Click **Import**

3. **Verify Import**
   - The collection "To-Do Application API" should appear in your Collections sidebar
   - Expand the collection to see all folders and requests

### Method 2: Import via Link (If hosted)

1. **Open Postman**
   - Launch Postman

2. **Import Collection**
   - Click the **Import** button
   - Select **Link** tab
   - Paste the URL to the collection JSON file
   - Click **Import**

### Method 3: Import via Raw Text

1. **Open Postman**
   - Launch Postman

2. **Import Collection**
   - Click the **Import** button
   - Select **Raw text** tab
   - Copy and paste the entire contents of `todo-app-postman-collection.json`
   - Click **Import**

## Environment Setup

### Create Environment Variables

1. **Create New Environment**
   - Click the gear icon (⚙️) in the top-right corner
   - Click **Add** to create a new environment
   - Name it "To-Do App - Development" or "To-Do App - Production"

2. **Set Environment Variables**
   Add the following variables:

   | Variable Name | Initial Value | Current Value |
   |--------------|---------------|---------------|
   | `baseURL` | `http://localhost:8080/api/v1` | `http://localhost:8080/api/v1` |
   | `authToken` | (leave empty) | (leave empty) |
   | `userId` | `user-123` | `user-123` |
   | `taskId` | `task-123` | `task-123` |
   | `commentId` | `comment-123` | `comment-123` |

3. **Select Environment**
   - Use the environment dropdown in the top-right corner
   - Select your newly created environment

### Alternative: Use Collection Variables

The collection already includes default variables. You can modify them:
- Right-click on the collection → **Edit**
- Go to the **Variables** tab
- Update values as needed

## Authentication Setup

### Cookie-Based Authentication

The API uses cookie-based authentication (JSESSIONID). Postman will automatically handle cookies if:

1. **Enable Cookie Management**
   - Go to **Settings** (⚙️) → **General**
   - Ensure **Send cookies** is enabled

2. **Login First**
   - Execute the **Login - Username/Password** request from the Authentication folder
   - Postman will automatically store the session cookie
   - Subsequent requests will use the cookie automatically

### Token-Based Authentication (If Applicable)

If your API uses token-based auth:

1. **Login and Capture Token**
   - Execute the **Login - Username/Password** request
   - The test script will automatically save the token to `authToken` variable

2. **Add Authorization Header** (if needed)
   - You may need to add a pre-request script to set Authorization header:
   ```javascript
   pm.request.headers.add({
       key: 'Authorization',
       value: 'Bearer ' + pm.environment.get('authToken')
   });
   ```

## Collection Structure

The collection is organized into the following folders:

1. **Authentication** - Login, logout, password reset
2. **Users** - User management (Admin only)
3. **Tasks** - Task CRUD operations
4. **Comments** - Task comments and collaboration
5. **Tags** - Tag management
6. **Dashboard** - Dashboard metrics and widgets
7. **Reports** - Reporting and analytics (Manager/Admin only)
8. **Notifications** - Notification preferences
9. **Health** - System health checks

## Using the Collection

### Running Requests

1. **Select a Request**
   - Navigate to a folder and select a request

2. **Update Variables** (if needed)
   - Replace placeholder values like `{{userId}}` with actual IDs
   - Or update environment variables

3. **Send Request**
   - Click **Send** button
   - View response in the bottom panel

### Running Tests

Many requests include test scripts. To run tests:

1. **Send Request**
   - Execute any request

2. **View Test Results**
   - Check the **Test Results** tab in the response panel
   - Green checkmarks indicate passed tests

### Negative Test Cases

The collection includes negative test cases (marked with "Negative" in the name) to test error handling:
- Invalid credentials
- Missing required fields
- Invalid data formats
- Unauthorized access attempts
- Not found scenarios

## Tips and Best Practices

1. **Start with Authentication**
   - Always login first to establish a session
   - Use the **Login - Username/Password** request

2. **Update Variables**
   - After creating resources (users, tasks), update environment variables with actual IDs
   - This allows subsequent requests to reference created resources

3. **Use Collection Runner**
   - Select the collection → Click **Run**
   - Configure iterations and delays
   - Run multiple requests in sequence

4. **Export/Backup**
   - Regularly export your collection to backup changes
   - File → Export → Select collection → Export

5. **Share Collection**
   - Use Postman's sharing features to collaborate
   - Publish to Postman API Network (optional)

## Troubleshooting

### Common Issues

1. **401 Unauthorized**
   - Ensure you've logged in first
   - Check that cookies are enabled
   - Verify session hasn't expired

2. **404 Not Found**
   - Verify the `baseURL` variable is correct
   - Check that the API server is running
   - Ensure endpoint paths match your API version

3. **403 Forbidden**
   - Verify your user has the required permissions
   - Check user role (USER, MANAGER, ADMIN)

4. **Variables Not Resolving**
   - Ensure environment is selected
   - Check variable names match exactly (case-sensitive)
   - Verify variable scope (collection vs environment)

5. **Cookies Not Working**
   - Enable cookie management in settings
   - Clear cookies and login again
   - Check if API uses different cookie names

## Environment Configurations

### Development Environment
```
baseURL: http://localhost:8080/api/v1
```

### Production Environment
```
baseURL: https://api.todoapp.company.com/api/v1
```

Create separate environments for each and switch as needed.

## Additional Resources

- [Postman Documentation](https://learning.postman.com/docs/)
- [Postman Collection Format](https://schema.getpostman.com/json/collection/v2.1.0/docs/index.html)
- [OpenAPI Specification](https://swagger.io/specification/)

## Support

For API support, contact: api-support@company.com
