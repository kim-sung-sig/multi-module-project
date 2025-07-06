
CREATE TABLE chat_room (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE user_chat_room (
    id BIGSERIAL PRIMARY KEY,
    chat_room_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    room_name VARCHAR(255),
    joined_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_chat_room FOREIGN KEY (chat_room_id) REFERENCES chat_room(id),
    CONSTRAINT uq_user_chat_room UNIQUE (user_id, chat_room_id)
);

CREATE TABLE message (
    id BIGSERIAL PRIMARY KEY,
    room_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    details JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_message_chat_room FOREIGN KEY (room_id) REFERENCES chat_room(id)
);

CREATE TABLE message_read (
    id BIGSERIAL PRIMARY KEY,
    message_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    read_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_message_read_message FOREIGN KEY (message_id) REFERENCES message(id),
    CONSTRAINT uq_user_message_read UNIQUE (user_id, message_id)
);

CREATE INDEX idx_user_chat_room_chat_room_id ON user_chat_room(chat_room_id);
CREATE INDEX idx_message_room_id_created_at ON message(room_id, created_at DESC);
CREATE INDEX idx_message_read_user_id ON message_read(user_id);

