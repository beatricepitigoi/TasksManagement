Tabels:
 1. Tasks - define tasks

 2. Users - define users
 3. SharedTasks - intermediate table for shared tasks

 Terminal CLI:
 psql -U beatricepitigoi (role name)

 # Create db
 CREATE DATABASE dbBPTG;

 # Create new user
 CREATE USER task_user WITH PASSWORD 'password';
 GRANT ALL PRIVILEGES ON DATABASE task_manager TO task_user;

 # Connect to your db
 psql -U beatricepitigoi -d dbBPTG

 ## TABLES
   1. USERS - define tasks users
      CREATE TABLE users (
      id SERIAL PRIMARY KEY,
      username VARCHAR(50) UNIQUE NOT NULL,
      password VARCHAR(255) NOT NULL,
      email VARCHAR(100) UNIQUE
      );
   2. TASKS - define tasks
      CREATE TABLE tasks (
      id SERIAL PRIMARY KEY,
      title VARCHAR(100) NOT NULL,
      description TEXT,
      status VARCHAR(20) NOT NULL,
      owner_id INT REFERENCES users(id) ON DELETE CASCADE,
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
      );

  --> NiceToHave: For each task update to have a trigger for changing the timestamp
      
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

 -->TODO: check functionality


  3. Shared tasks - intermediate table for treating the MtoM relationship for shared tasks
   --> TODO: If I delete one task/user to dleet as well from tasksShared -- DONE
     CREATE TABLE tasksShared (
     task_id INT REFERENCES tasks(id) ON DELETE CASCADE,
     user_id INT REFERENCES users(id) ON DELETE CASCADE,
     PRIMARY KEY (task_id, user_id)
     );


 --> Page design 


