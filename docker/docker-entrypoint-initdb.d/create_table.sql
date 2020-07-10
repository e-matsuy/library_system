CREATE TABLE categories (
  id serial primary key, 
  name text unique NOT NULL,
  created_at timestamp NOT NULL DEFAULT current_timestamp,
  updated_at timestamp NOT NULL DEFAULT current_timestamp,
  deleted_at timestamp
);

CREATE TABLE publishers (
  id serial primary key, 
  name text unique NOT NULL,
  created_at timestamp NOT NULL DEFAULT current_timestamp,
  updated_at timestamp NOT NULL DEFAULT current_timestamp,
  deleted_at timestamp
);

CREATE TABLE roles (
  id serial primary key, 
  name text unique NOT NULL,
  created_at timestamp NOT NULL DEFAULT current_timestamp,
  updated_at timestamp NOT NULL DEFAULT current_timestamp,
  deleted_at timestamp
);

CREATE TABLE book_master (
  id serial primary key, 
  title text NOT NULL,
  author text NOT NULL,
  publisher integer NOT NULL,
  category integer NOT NULL,
  isbn varchar(20) NOT NULL,
  created_at timestamp NOT NULL DEFAULT current_timestamp,
  updated_at timestamp NOT NULL DEFAULT current_timestamp,
  deleted_at timestamp,
  FOREIGN KEY (publisher) REFERENCES publishers(id),
  FOREIGN KEY (category) REFERENCES categories(id)
);

CREATE TABLE users (
  id serial primary key, 
  employee_number text NOT NULL,
  name text NOT NULL,
  name_kana text NOT NULL,
  mail_address text NOT NULL,
  date_of_hire date NOT NULL,
  role integer NOT NULL,
  password text NOT NULL,
  last_login_date timestamp,
  created_at timestamp NOT NULL DEFAULT current_timestamp,
  updated_at timestamp NOT NULL DEFAULT current_timestamp,
  deleted_at timestamp,
  FOREIGN KEY (role) REFERENCES roles(id)
);

CREATE TABLE books (
  id serial primary key, 
  book integer NOT NULL,
  bought_date date NOT NULL,
  purchaser integer NOT NULL,
  last_use_log integer,
  created_at timestamp NOT NULL DEFAULT current_timestamp,
  updated_at timestamp NOT NULL DEFAULT current_timestamp,
  deleted_at timestamp,
  FOREIGN KEY (book) REFERENCES book_master(id),
  FOREIGN KEY (purchaser) REFERENCES users(id)
);

CREATE TABLE tokens (
  id serial primary key, 
  user_id integer NOT NULL,
  token text NOT NULL,
  token_expiration timestamp NOT NULL,
  created_at timestamp NOT NULL DEFAULT current_timestamp,
  updated_at timestamp NOT NULL DEFAULT current_timestamp,
  deleted_at timestamp,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE lending_records (
  id serial primary key, 
  book integer NOT NULL,
  user_id integer NOT NULL,
  checkout_date timestamp NOT NULL,
  return_schedule date NOT NULL,
  returned_date timestamp,
  created_at timestamp NOT NULL DEFAULT current_timestamp,
  updated_at timestamp NOT NULL DEFAULT current_timestamp,
  deleted_at timestamp,
  FOREIGN KEY (book) REFERENCES books(id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

/* postgreSQL で *.updated_at を自動更新する関数*/
create function set_update_time() returns opaque as '
  begin
    new.updated_at := ''now'';
    return new;
  end;
' language 'plpgsql';

/* 更新の際にset_update_time()を呼び出すトリガーをテーブルごとに設定する */
create trigger update_tri_catecories before update on categories for each row
  execute procedure set_update_time();
create trigger update_tri_publishers before update on publishers for each row
  execute procedure set_update_time();
create trigger update_tri_roles before update on roles for each row
  execute procedure set_update_time();
create trigger update_tri_book_master before update on book_master for each row
  execute procedure set_update_time();
create trigger update_tri_users before update on users for each row
  execute procedure set_update_time();
create trigger update_tri_books before update on books for each row
  execute procedure set_update_time();
create trigger update_tri_tokens before update on tokens for each row
  execute procedure set_update_time();
create trigger update_tri_lending_records before update on lending_records for each row
  execute procedure set_update_time();
