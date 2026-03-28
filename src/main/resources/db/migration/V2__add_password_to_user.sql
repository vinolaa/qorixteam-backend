ALTER TABLE app_users ADD COLUMN password VARCHAR(255);

UPDATE app_users SET password = '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG' WHERE password IS NULL;

ALTER TABLE app_users ALTER COLUMN password SET NOT NULL;