DROP TABLE IF EXISTS password_reset_tokens;
DROP TABLE IF EXISTS record_contact_log;
DROP TABLE IF EXISTS users_privileges;
DROP TABLE IF EXISTS users_constituencies;
DROP TABLE IF EXISTS privileges;
DROP TABLE IF EXISTS users_wards;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS wards;
DROP TABLE IF EXISTS constituencies;

CREATE TABLE constituencies
(
  id   UUID PRIMARY KEY,
  name TEXT NOT NULL UNIQUE,
  code TEXT NOT NULL UNIQUE
);

CREATE TABLE wards (
  id              UUID PRIMARY KEY NOT NULL,
  constituency_id UUID REFERENCES constituencies (id),
  name            TEXT             NOT NULL,
  code            TEXT             NOT NULL
);

CREATE TABLE users
(
  id            UUID PRIMARY KEY,
  username      TEXT NOT NULL UNIQUE,
  password_hash TEXT NOT NULL,
  first_name    TEXT,
  last_name     TEXT,
  write_access  BOOL NOT NULL DEFAULT FALSE,
  role          TEXT NOT NULL
);

CREATE TABLE privileges
(
  id         UUID NOT NULL PRIMARY KEY,
  permission TEXT CHECK (permission IN ('READ_VOTER', 'EDIT_VOTER'))
);

CREATE TABLE users_privileges
(
  users_id      UUID REFERENCES users (id)      NOT NULL,
  privileges_id UUID REFERENCES privileges (id) NOT NULL,
  PRIMARY KEY (users_id, privileges_id)
);

CREATE TABLE users_wards (
  users_id UUID REFERENCES users (id)           NOT NULL,
  wards_id UUID REFERENCES wards (id)           NOT NULL,
  PRIMARY KEY (users_id, wards_id)
);

CREATE TABLE users_constituencies (
  users_id          UUID REFERENCES users (id)                      NOT NULL,
  constituencies_id UUID REFERENCES constituencies (id)             NOT NULL,
  PRIMARY KEY (users_id, constituencies_id)
);

CREATE TABLE record_contact_log (
  id        UUID PRIMARY KEY,
  users_id  UUID REFERENCES users (id)                   NOT NULL,
  wards_id  UUID REFERENCES wards (id)                   NOT NULL,
  ern       TEXT                                         NOT NULL,
  added     TIMESTAMP WITHOUT TIME ZONE                  NOT NULL,
  operation TEXT CHECK (operation IN ('UNDO', 'CREATE')) NOT NULL DEFAULT 'CREATE'
);

CREATE TABLE password_reset_tokens (
  id       UUID PRIMARY KEY,
  users_id UUID REFERENCES users (id)                   NOT NULL,
  token    TEXT                                         NOT NULL,
  expires  TIMESTAMP WITHOUT TIME ZONE                  NOT NULL
);
