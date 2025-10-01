-- replication 계정 생성
CREATE ROLE replica_user REPLICATION LOGIN PASSWORD 'replica_password';

-- 일반 개발용 계정
CREATE ROLE dev_user LOGIN PASSWORD 'dev_password';
CREATE DATABASE messagesystem OWNER dev_user;