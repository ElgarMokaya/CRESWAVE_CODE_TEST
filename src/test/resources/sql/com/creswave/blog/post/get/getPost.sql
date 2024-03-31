
-- RoleGroup  ======================================================================================

INSERT INTO role_group (id, created_at, updated_at, description, "name")
VALUES(1, current_timestamp, current_timestamp, 'Roles the Standard User has', 'Standard User Role Group');

INSERT INTO role_group (id, created_at, updated_at, description, "name")
VALUES(2, current_timestamp, current_timestamp, 'Roles the Admin has', 'Admin Role Group');

-- Role  ===========================================================================================

INSERT INTO "role" (id, created_at, updated_at, description, "name")
VALUES(1, current_timestamp, current_timestamp, 'Permission for standard user', 'STANDARD_USER');


INSERT INTO "role" (id, created_at, updated_at, description, "name")
VALUES(2, current_timestamp, current_timestamp, 'Permission to add Comments', 'DELETE_POST');

--  rolegroup_role =================================================================================

INSERT INTO rolegroup_role (rolegroup_id, role_id)
VALUES(1, 1);

INSERT INTO rolegroup_role (rolegroup_id, role_id)
VALUES(2, 1);

INSERT INTO rolegroup_role (rolegroup_id, role_id)
VALUES(2, 2);

-- _user ===========================================================================================

INSERT INTO "_user"
(id, created_at, updated_at, active, avatar_identifier, email, email_activation_code, first_name, 
forgotten_password_code, last_name, password_hash, username)
VALUES(1, '2024-03-30 15:33:30.586', '2024-03-30 15:33:30.586', true, NULL, 
'elgar.bosibori.mokaya@gmail.com', '537950017309985618', NULL, NULL, NULL, 
'$2a$10$nkms4OTDfYybwh2TGFUQze4eKrgybjLVJ4YUjv1ZoMGJY3OXAZahC', 'elation');


INSERT INTO "_user"
(id, created_at, updated_at, active, avatar_identifier, email, email_activation_code, first_name, 
forgotten_password_code, last_name, password_hash, username)
VALUES(2, '2024-03-30 15:33:30.586', '2024-03-30 15:33:30.586', true, NULL, 
'admin@blog.com', '537950017309985618', NULL, NULL, NULL, 
'$2a$10$nkms4OTDfYybwh2TGFUQze4eKrgybjLVJ4YUjv1ZoMGJY3OXAZahC', 'admin');


-- token ===========================================================================================

-- expires in 29 years time
INSERT INTO "token"
(id, created_at, updated_at, expired, revoked, "token", token_type, user_id)
VALUES(10001, '2024-03-30 16:02:31.031', '2024-03-30 16:02:31.031', false, false, 
'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGdhci5ib3NpYm9yaS5tb2theWFAZ21haWwuY29tIiwiaWF0IjoxNzExODAzNzUxLCJleHAiOjI2MTE4MDM3NTF9.6GHmSrGMUxcoTlgdyySuW--PO0bLU9H1CRzX0Z4RCaY', 
'BEARER', 1);

INSERT INTO "token"
(id, created_at, updated_at, expired, revoked, "token", token_type, user_id)
VALUES(2, '2024-03-30 16:02:31.031', '2024-03-30 16:02:31.031', false, false, 
'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBibG9nLmNvbSIsImlhdCI6MTcxMTg0NTIyNiwiZXhwIjoyNjExODQ1MjI2fQ.R3usP8H-iqNano5aUegCepTGZFZ40GcgcT55Zfzk4-k', 
'BEARER', 2);

-- user_role  ======================================================================================

INSERT INTO user_role
(user_id, role_id)
VALUES(1, 1);

INSERT INTO user_role
(user_id, role_id)
VALUES(2, 1);

INSERT INTO user_role
(user_id, role_id)
VALUES(2, 2);

-- post  ===========================================================================================

INSERT INTO post
(id, created_at, updated_at, "text", title, fk_user)
VALUES(1, '2024-03-30 16:16:59.417', '2024-03-30 16:16:59.418', 
'I want to learn Java Spring Boot.  Can anyone help?', 'Java Spring Boot', 1);


-- comment  ========================================================================================

INSERT INTO "comment"
(id, created_at, updated_at, "text", fk_post, fk_user)
VALUES(10000, '2024-03-30 23:13:22.643', '2024-03-30 23:13:22.643', 
'I can help my github username is: elgarmokaya', 1, 1);
