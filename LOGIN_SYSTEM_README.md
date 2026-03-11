# Restaurant Billing System - Login Authentication

This document explains the complete login system implementation for the Restaurant Billing System.

## 🚀 Features

### ✅ Session-Based Authentication (No JWT Tokens)
- **Username/Password Login** - Simple and secure authentication
- **PostgreSQL Storage** - User credentials stored in database
- **Session Management** - Server-side session tracking
- **Auto-Redirect** - Login page redirects to dashboard after successful authentication
- **Protected Routes** - All dashboard pages require authentication
- **Logout Functionality** - Secure session termination

### ✅ Frontend Features
- **Beautiful Login Page** - Modern, responsive design
- **Form Validation** - Real-time input validation
- **Error Handling** - User-friendly error messages
- **Loading States** - Visual feedback during authentication
- **User Profile** - Display user info in header with logout option
- **Auto-Logout** - Session timeout handling

### ✅ Backend Features
- **Secure Password Storage** - BCrypt encryption
- **Custom Exceptions** - Comprehensive error handling
- **Session Validation** - Active session checking
- **Default Admin User** - Auto-created on first startup

## 📁 Project Structure

### Backend (Spring Boot)
```
src/main/java/com/restaurant/billing/
├── entity/User.java                    # User entity with authentication fields
├── repository/UserRepository.java      # User data access operations
├── dto/LoginRequest.java              # Login request DTO
├── dto/LoginResponse.java             # Login response DTO
├── service/AuthService.java           # Authentication business logic
├── controller/AuthController.java      # Authentication REST endpoints
├── config/PasswordEncoderConfig.java  # BCrypt password encoder
└── config/DataInitializer.java        # Default admin user creation
```

### Frontend (React)
```
src/
├── pages/Login.jsx                     # Login page component
├── components/ProtectedRoute.jsx       # Route protection wrapper
├── services/authService.js             # Authentication API calls
└── components/Header.jsx               # Updated with user menu and logout
```

## 🔐 Default Credentials

### Admin User (Auto-Created)
- **Username**: `admin`
- **Password**: `admin123`
- **Email**: `admin@restaurant.com`
- **Role**: `ADMIN`

## 🚀 Getting Started

### 1. Backend Setup

1. **Start PostgreSQL Database**
2. **Run Spring Boot Application**
   ```bash
   cd E:\Alvin_Sir\JAVAPROJECT\restaurant-billing-system
   mvn spring-boot:run
   ```
3. **Verify Database Tables**
   - `users` table will be created automatically
   - Default admin user will be created on first startup

### 2. Frontend Setup

1. **Start React Development Server**
   ```bash
   cd E:\Alvin_Sir\JAVAPROJECT\restaurant_frontend\frontend
   npm run dev
   ```
2. **Open Browser**
   - Navigate to: `http://localhost:5173`
   - You will be automatically redirected to login page

### 3. Login Process

1. **Enter Credentials**
   - Username: `admin`
   - Password: `admin123`
2. **Click "Sign in"**
3. **Redirect to Dashboard**
   - Successful login redirects to `/dashboard`
   - User info stored in session and localStorage
4. **Access Protected Routes**
   - All routes now accessible: `/orders`, `/menu`, `/billing`, `/settings`

## 📡 API Endpoints

### Authentication Endpoints

#### POST `/api/auth/login`
**Description**: Authenticate user and create session
```json
Request:
{
  "username": "admin",
  "password": "admin123"
}

Response:
{
  "id": 1,
  "username": "admin",
  "email": "admin@restaurant.com",
  "fullName": "Restaurant Administrator",
  "role": "ADMIN",
  "loginTime": "2024-03-09T12:00:00",
  "message": "Login successful"
}
```

#### POST `/api/auth/logout`
**Description**: Invalidate user session
```json
Response: "Logged out successfully"
```

#### GET `/api/auth/current-user`
**Description**: Get current authenticated user
```json
Response:
{
  "id": 1,
  "username": "admin",
  "email": "admin@restaurant.com",
  "fullName": "Restaurant Administrator",
  "role": "ADMIN",
  "loginTime": "2024-03-09T12:00:00"
}
```

#### GET `/api/auth/check-session`
**Description**: Check if session is active
```json
Response: true/false
```

#### POST `/api/auth/create-admin`
**Description**: Create new admin user (for testing)
```
Parameters:
- username: String
- email: String
- password: String
- fullName: String
```

## 🛡️ Security Features

### Password Security
- **BCrypt Encryption** - Passwords hashed with BCrypt
- **Secure Storage** - No plain text passwords in database
- **Strong Passwords** - Minimum 6 characters required

### Session Security
- **Server-Side Sessions** - Sessions stored securely on server
- **Session Validation** - Active session checking on protected routes
- **Automatic Logout** - Session invalidation on logout

### Input Validation
- **Frontend Validation** - Real-time form validation
- **Backend Validation** - Jakarta validation annotations
- **Custom Exceptions** - Comprehensive error handling

## 🔧 Configuration

### Database Schema (Users Table)
```sql
CREATE TABLE users (
    id BIGINT SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) DEFAULT 'ADMIN',
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);
```

### Security Configuration
- **CORS Enabled** - Cross-origin requests allowed for frontend
- **CSRF Disabled** - Not needed for stateless REST API
- **Session Cookies** - Secure session management
- **Password Encoder** - BCrypt with default strength

## 🔄 User Flow

### 1. First Time Access
```
User visits http://localhost:5173
    ↓
Redirected to /login (no active session)
    ↓
User enters credentials
    ↓
Backend validates credentials
    ↓
Session created and user info returned
    ↓
Redirected to /dashboard
```

### 2. Subsequent Access
```
User visits http://localhost:5173
    ↓
ProtectedRoute checks session
    ↓
Session valid → Dashboard loaded
Session invalid → Redirect to /login
```

### 3. Logout Process
```
User clicks logout in header
    ↓
Frontend calls logout API
    ↓
Backend invalidates session
    ↓
Local storage cleared
    ↓
Redirected to /login
```

## 🎨 Frontend Features

### Login Page
- **Modern Design** - Clean, professional interface
- **Responsive Layout** - Works on all screen sizes
- **Form Validation** - Real-time error messages
- **Password Toggle** - Show/hide password option
- **Loading States** - Visual feedback during login
- **Demo Credentials** - Built-in credential display

### Header Component
- **User Profile** - Display user name and role
- **Dropdown Menu** - User info and logout option
- **Logout Functionality** - Secure session termination
- **Avatar** - User initial display

### Protected Routes
- **Route Protection** - Authentication required for all pages
- **Session Validation** - Backend session checking
- **Loading States** - Authentication check in progress
- **Auto-Redirect** - Unauthenticated users sent to login

## 🚨 Error Handling

### Frontend Errors
- **Network Errors** - Connection issues handled gracefully
- **Validation Errors** - Form validation messages
- **Authentication Errors** - Invalid credentials handled
- **Session Errors** - Expired sessions handled

### Backend Errors
- **Invalid Credentials** - 400 Bad Request
- **User Not Found** - 404 Not Found
- **Validation Errors** - 422 Unprocessable Entity
- **Session Errors** - 401 Unauthorized
- **Server Errors** - 500 Internal Server Error

## 📝 Testing

### Test Login Process
1. **Start both applications**
2. **Visit frontend** - Should redirect to login
3. **Enter wrong credentials** - Should show error
4. **Enter correct credentials** - Should redirect to dashboard
5. **Access protected routes** - Should work normally
6. **Logout** - Should redirect to login

### Test Session Management
1. **Login successfully**
2. **Close browser tab**
3. **Reopen and visit site** - Should maintain session
4. **Logout** - Should clear session
5. **Try to access protected route** - Should redirect to login

## 🔧 Customization

### Add New Users
```java
// Via API
POST /api/auth/create-admin
{
  "username": "newuser",
  "email": "newuser@restaurant.com",
  "password": "password123",
  "fullName": "New User"
}

// Via Database
INSERT INTO users (username, email, password, full_name, role)
VALUES ('newuser', 'newuser@restaurant.com', '$2a$10$...', 'New User', 'ADMIN');
```

### Change Default Password
```java
// In DataInitializer.java
.password(passwordEncoder.encode("newpassword"))
```

### Customize Login Page
- Edit `src/pages/Login.jsx` for UI changes
- Modify validation rules in `LoginRequest.java`
- Update error messages in `CustomValidationMessages.java`

## 🎯 Benefits

1. **✅ Secure Authentication** - BCrypt password hashing
2. **✅ Session Management** - Server-side session control
3. **✅ User Experience** - Smooth login/logout flow
4. **✅ Error Handling** - Comprehensive error management
5. **✅ Scalability** - Easy to add new users and roles
6. **✅ Maintainability** - Clean, modular code structure

The login system is now fully functional and ready for production use! 🎉
