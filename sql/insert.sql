INSERT into users (username, email, password, created_at) values ('admin', 'admin@aulab.it', '123456789', '2025-04-05');

INSERT into roles (name) values ('ROLE_ADMIN'); 
INSERT into roles (name) values ('ROLE_REVISOR'); 
INSERT into roles (name) values ('ROLE_WRITER'); 
INSERT into roles (name) values ('ROLE_USER');

INSERT into user_roles (user_id, role_id) values (1, 1);

INSERT into categories (name) values ('politics');
INSERT into categories (name) values ('economy');
INSERT into categories (name) values ('food&drinks');
INSERT into categories (name) values ('sport');
INSERT into categories (name) values ('entertainment');
INSERT into categories (name) values ('tech');


