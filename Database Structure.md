# Task Management Application - Database Setup

## Tables
1. **Users** - Stores user information such as username, password, and email.
2. **Tasks** - Defines tasks, including title, description, status, and timestamps.

### Relationships:
- **One-to-Many**: A user can have multiple tasks.
- **Many-to-One**: Each task belongs to one user but can be shared with multiple users.

> **Note**: You don't need a third table for shared tasks. Use annotations within your application to define the relationships between tables.

---

## Terminal CLI Commands (PostgreSQL)

### Access PostgreSQL:
    psql -U beatricepitigoi

### Create new user
    CREATE USER beatricepitigoi WITH PASSWORD 'password';
    GRANT ALL PRIVILEGES ON DATABASE task_manager TO task_user;

### Connect to your db
    psql -U beatricepitigoi -d dbBPTG

 ## TABLES 

      CREATE TABLE users (
      id SERIAL PRIMARY KEY,
      username VARCHAR(50) UNIQUE NOT NULL,
      password VARCHAR(255) NOT NULL,
      email VARCHAR(100) UNIQUE
      );

### Explanation:

- **id**: Primary key.
- **username**: Must be unique and non-nullable.
- **password**: Encrypted user password.
- **email**: Optional but unique for each user.


      CREATE TABLE tasks (
      id SERIAL PRIMARY KEY,
      title VARCHAR(100) NOT NULL,
      description TEXT,
      status VARCHAR(20) NOT NULL,
      owner_id INT REFERENCES users(id) ON DELETE CASCADE,
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
      );

### Explanation:
- **id**: Primary key.
- **title**: Task title, non-nullable.
- **description**: Task description.
- **status**: Status of the task, non-nullable (e.g., pending, completed).
- **owner_id**: Foreign key to `users(id)`. If the user is deleted, their tasks are also deleted (ON DELETE CASCADE).
- **created_at**: Automatically set to the current timestamp when the task is created.
- **updated_at**: Automatically set to the current timestamp when the task is created or updated.

      CREATE OR REPLACE FUNCTION update_updated_at_column()
      RETURNS TRIGGER AS $$
      BEGIN
      NEW.updated_at = NOW();
      RETURN NEW;
      END;
      $$ LANGUAGE plpgsql;

      CREATE TRIGGER update_task_updated_at
      BEFORE UPDATE ON tasks
      FOR EACH ROW
      EXECUTE FUNCTION update_updated_at_column();

### Explanation:

- **Trigger**: Automatically updates the `updated_at` field before every update on the `tasks` table.
- **Note**: This ensures that the `updated_at` timestamp is automatically updated when a task is modified.


## Deprecated/Optional Table
     CREATE TABLE tasksShared (
     task_id INT REFERENCES tasks(id) ON DELETE CASCADE,
     user_id INT REFERENCES users(id) ON DELETE CASCADE,
     PRIMARY KEY (task_id, user_id)
     );  

### Explanation:

- **task_id**: Foreign key to `tasks(id)`.
- **user_id**: Foreign key to `users(id)`.
- **Primary Key**: Composite key of `task_id` and `user_id`.

This table can be used to track tasks shared between multiple users, but it is currently not in use.


## Nice-to-Have Features:

- **Auto-update Timestamps**: The trigger defined above automatically updates the `updated_at` timestamp on task updates, ensuring data consistency.
- **Further Optimizations**: The application could be extended to include advanced task-sharing functionalities or other features as needed.

---

## TODO:

- **Test the Trigger**: Verify that the trigger for updating the `updated_at` column works as expected.
- **Implement Additional Features**: If necessary, implement further functionalities such as task sharing between users, task reminders, or other user-specific features.

---

## Key Enhancements:

- **Formatted SQL Code**: All SQL commands are clearly presented in code blocks for easy execution.
- **Explanations**: Each table and relationship is explained in detail to make the schema easier to understand.
- **Instructions**: Clear steps for creating a database, user, and connecting to the database.
- **Deprecated Table**: A note explains the unused `tasksShared` table and why it's not currently needed.
- **Nice-to-Have Features**: Suggestions for further improvements, such as auto-updating timestamps, are clearly outlined.





