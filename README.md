User Management System (with JWT Auth + RBAC)
Objective:
Build a secured REST API for managing users with JWT authentication and role-based access control.
 
Requirements:
- Implement the following entities:
- User: id, username, password, email, roles
- Role: id, name (Enum: USER, ADMIN)
- Implement JWT-based authentication for securing the APIs.
- Implement Role-Based Access Control (RBAC) where:
- Only users with ADMIN role can:
- Create a new user (POST /users)
- Delete any user (DELETE /users/{id})
- Both USER and ADMIN roles can:
- View their own profile (GET /users/me)
- Update their own profile (PUT /users/me)
 
Endpoints:
POST /users → Create user (ADMIN only)
DELETE /users/{id} → Delete user (ADMIN only)
GET /users/me → Get current user profile (authenticated USER or ADMIN)
PUT /users/me → Update current user profile (authenticated USER or ADMIN)
 
Bonus:
Secure user passwords using BCryptPasswordEncoder.
Enforce security using JWT tokens and role checks with annotations (e.g., @PreAuthorize).
