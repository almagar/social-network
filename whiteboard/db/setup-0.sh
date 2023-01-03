#!/bin/bash

set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE "$POSTGRES_DATABASE";
    GRANT ALL PRIVILEGES ON DATABASE "$POSTGRES_DATABASE" TO "$POSTGRES_USER";
EOSQL


psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DATABASE" <<-EOSQL
    CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
EOSQL

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DATABASE" <<-EOSQL
    CREATE TABLE IF NOT EXISTS Whiteboard
    (
        id uuid,
        chatId uuid NOT NULL UNIQUE,
        chatName varchar(64) NOT NULL,
        PRIMARY KEY (id)
    );
EOSQL