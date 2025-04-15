### 📖 Blog Platform on Spring MVC

A simple web application for blogging with posts, comments, likes, and image uploads.

## 🚀 Full Workflow

### 1. Running the Application

Requirements:
JDK 21+  
Maven 3.9+  
H2 Database (embedded)  
**Build & Run:**
```
mvn clean package
mvn tomcat7:run
```
App will be available at:
🔗 http://localhost:8080/
### 2. Features

#### ✅ Post CRUD

Create, edit, delete
Pagination & search
#### ✅ Comments

Add, edit, delete
#### ✅ Likes

Increment/decrement counter
#### ✅ Images

Upload & display
#### ✅ Tags

Assign tags to posts
## 🛠 Technologies & Approaches

#### Backend

Spring MVC (web layer)  
H2 Database (embedded)  
Spring Data JDBC (DB access)  
Thymeleaf (templating)  
JUnit 5 + Mockito (testing)  
#### Frontend

HTML + CSS (basic UI)  
Thymeleaf (server-side rendering)  
Architecture  

MVC (Model-View-Controller)  
Repository Layer (PostRepository, JdbcNativeUserRepository)  
Service Layer (PostService)  
Controllers (PostController)  
## 🔮 Future Improvements

### Short-term

🔹 Add authentication (Spring Security)  
🔹 Implement REST API  
🔹 Improve UI (Bootstrap, JS)

### Long-term

🔸 Migrate to PostgreSQL  
🔸 Caching (Redis)  
🔸 Docker deployment
