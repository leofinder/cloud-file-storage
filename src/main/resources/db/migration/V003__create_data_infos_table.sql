CREATE TABLE data_infos (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    parent_path TEXT NOT NULL,
    path TEXT NOT NULL,
    is_folder BOOLEAN NOT NULL,
    size BIGINT,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT data_objects_unique_constraint UNIQUE (path, user_id)
);

CREATE INDEX data_infos_user_id_idx ON data_infos (user_id);
CREATE INDEX data_infos_user_id_name_idx ON data_infos (user_id, name);
CREATE INDEX data_infos_user_id_path_idx ON data_infos (user_id, parent_path);