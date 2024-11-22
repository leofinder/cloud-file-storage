CREATE TABLE users_roles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    role_id INT REFERENCES roles(id) ON DELETE CASCADE,
    CONSTRAINT users_roles_unique_constraint UNIQUE (user_id, role_id)
);

CREATE INDEX users_roles_user_id_idx ON users_roles (user_id);

CREATE INDEX users_roles_role_id_idx ON users_roles (role_id);