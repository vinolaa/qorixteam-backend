-- Script inicial de criação das tabelas do Virtual Office Chat

-- 1. Criação da tabela de Usuários
CREATE TABLE app_users (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 2. Criação da tabela de Salas/Grupos
CREATE TABLE chat_rooms (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_private BOOLEAN NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 3. Criação da tabela de relacionamento N:N (Usuários <-> Salas)
CREATE TABLE chat_room_members (
   room_id UUID NOT NULL,
   user_id UUID NOT NULL,
   PRIMARY KEY (room_id, user_id),
   CONSTRAINT fk_room FOREIGN KEY (room_id) REFERENCES chat_rooms (id) ON DELETE CASCADE,
   CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES app_users (id) ON DELETE CASCADE
);

-- 4. Criação da tabela de Mensagens
CREATE TABLE chat_messages (
   id UUID PRIMARY KEY,
   sender_id UUID NOT NULL,
   room_id UUID NOT NULL,
   content TEXT NOT NULL,
   timestamp TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
   CONSTRAINT fk_message_sender FOREIGN KEY (sender_id) REFERENCES app_users (id),
   CONSTRAINT fk_message_room FOREIGN KEY (room_id) REFERENCES chat_rooms (id) ON DELETE CASCADE
);

-- Índices de mercado para otimizar a busca de histórico de mensagens em salas
CREATE INDEX idx_chat_messages_room_timestamp ON chat_messages (room_id, timestamp);