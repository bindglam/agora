# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Agora is a web forum application with a Spring Boot (Java 25) backend and React 19 frontend. It uses SQLite for persistence and features a permission system, content moderation, and multiple boards.

## Common Commands

### Backend (Spring Boot + Gradle)

```bash
# From /backend directory

# Build the project
./gradlew build

# Run the application (starts on port 8080)
./gradlew bootRun

# Run tests
./gradlew test

# Run a specific test class
./gradlew test --tests PostTests
```

### Frontend (React + Vite)

```bash
# From /frontend directory

# Install dependencies
npm install

# Start development server (proxies /api to localhost:8080)
npm run dev

# Build for production
npm run build

# Run linter
npm run lint

# Preview production build
npm run preview
```

### Environment Setup

Frontend requires `VITE_API_URL` environment variable:
- Development: Set in `.env.development` (usually empty string for proxy)
- Production: Set in `.env.production`

## Architecture

### Backend Structure

The backend follows a **Controller-Service-Repository** pattern with **DTO separation**:

```
com.bindglam.agora/
├── post/
│   ├── controller/PostController.java    # REST endpoints
│   ├── service/PostService.java           # Business logic interface
│   ├── service/PostServiceImpl.java       # Implementation
│   ├── repository/PostRepository.java    # JPA repository
│   ├── Post.java                          # JPA entity
│   ├── PostDTO.java                       # Data transfer object
│   ├── PostRequests.java                  # Request/Response records
│   ├── PostVisibility.java                # Enum (PUBLIC, PRIVATE, NOT_LISTED)
│   └── censor/                            # Content moderation
│       ├── CensorServer.java             # Async censor interface
│       └── LocalCensorServer.java        # Local implementation
├── board/
│   ├── controller/BoardController.java
│   ├── Board.java                         # Record with id + properties
│   └── BoardProperties.java              # Display name, description, permissions
├── user/
│   ├── controller/UserController.java
│   ├── service/UserService.java
│   ├── User.java                          # JPA entity
│   ├── UserDTO.java
│   └── UserPermission.java               # Enum with parent inheritance
├── registry/
│   ├── Registry.java                      # Generic registry interface
│   ├── MappedRegistry.java               # Map-based implementation
│   └── BuiltInRegistries.java            # Static boards registry
├── utils/
│   └── Identified.java                   # Interface requiring id() method
└── config/
    └── CORSConfig.java                   # Allows all origins
```

### Key Backend Patterns

**Identified Interface**: All entities (User, Post) implement `Identified` which requires `String id()`. This provides a unified ID access pattern across the codebase.

**Registry System**: Used for static/in-memory resources. `BuiltInRegistries.BOARDS` holds all board definitions. To add a new board, modify `BuiltInRegistries.java`.

**Permission System**: `UserPermission` enum uses parent inheritance (e.g., `ADMIN` has parent `DEFAULT`). The `check(UserPermission required)` method allows hierarchical permission checking.

**DTO Pattern**: Entities (`User`, `Post`) are converted to DTOs (`UserDTO`, `PostDTO`) before leaving the service layer. DTOs include `toEntity()` method for conversion back.

**Censor System**: Content moderation uses `CompletableFuture` for async checking. `LocalCensorServer` provides local implementation; can be extended for remote services.

**Request/Response Records**: Defined as inner classes (e.g., `PostRequests.Create`, `PostRequests.Create.Response`).

### Frontend Structure

```
frontend/src/
├── api/
│   ├── board.jsx                          # Board API calls
│   ├── post.jsx                           # Post API calls
│   └── user.jsx                           # User API + cookie utilities
├── components/
│   ├── Navbar.jsx
│   ├── Footer.jsx
│   ├── UserInfoModal.jsx                  # User registration modal
│   └── WritePostModal.jsx                 # Post creation modal
├── pages/
│   ├── Home.jsx                           # Board listing
│   ├── Board.jsx                          # Post listing + pagination
│   └── Post.jsx                           # Single post view
├── constants.jsx                          # API_BASE_URL
├── utils.jsx                              # Cookie utilities
├── App.jsx                                # Routing + auth state
└── main.jsx
```

### API Design

All backend APIs are versioned under `/api/v1/`:

- `/board-api/boards` - GET all boards
- `/board-api/board/{boardId}` - GET single board
- `/post-api/post/{postId}?user={username}` - GET single post
- `/post-api/posts/{boardId}?user={username}&page={page}&size={size}` - GET paginated posts
- `/post-api/total-posts/{boardId}?user={username}` - GET total post count
- `/post-api/post` - POST create post
- `/user-api/user/{username}` - GET user
- `/user-api/user` - POST create user

**Note**: Most GET endpoints require a `user` query parameter for permission checking.

### Frontend Data Flow

1. User authentication via cookies (`username` stored in cookie)
2. `getCurrentUser()` checks if user exists; if not, shows `UserInfoModal`
3. Pages fetch data via `api/*` modules on mount
4. Error handling via try/catch with `alert()` for user feedback
5. Pagination using `page`, `pageSize`, and `totalCount` state

### Database

- **Database**: SQLite (`backend/database.db` - ignored by git)
- **JPA**: Spring Data JPA with Hibernate
- **Dialect**: SQLiteDialect (`org.hibernate.community.dialect.SQLiteDialect`)
- **Auto-DDL**: `update` (schema updates on startup)

### Testing

Backend uses JUnit 5 with `@SpringBootTest`. Test files are in `backend/src/test/java/com/bindglam/agora/`. Tests can use autowired repositories to create test data.

### React Compiler

The frontend uses the React Compiler for automatic memoization. This affects performance and build times. See React Compiler documentation for implications.

### Frontend Config

Vite dev server proxies `/api` to `http://localhost:8080`. Production builds need `VITE_API_URL` set to the actual backend URL.

### Content Moderation

Post creation goes through `CensorServer.tryCensor()`. If content fails moderation, the post is not saved and the response includes the rejection reason. Currently uses `LocalCensorServer` which can be extended.

### Board Permissions

Each board has a `BoardProperties.Permissions` configuration that specifies required permission level for posting. The "free" board allows `DEFAULT` users to post, while "announcements" requires `ADMIN`.

### Post Visibility

Posts have `PostVisibility` enum values:
- `PUBLIC` - Visible to all users
- `PRIVATE` - Only visible to admin
- `NOT_LISTED` - Accessible by ID but not in listings
