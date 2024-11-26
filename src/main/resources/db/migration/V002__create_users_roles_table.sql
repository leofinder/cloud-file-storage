CREATE TABLE users_roles (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, role)
);

CREATE INDEX users_roles_user_id_idx ON users_roles (user_id);
CREATE INDEX users_roles_role_idx ON users_roles (role);