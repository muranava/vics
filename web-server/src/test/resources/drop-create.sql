DROP TABLE IF EXISTS electors;
DROP TABLE IF EXISTS electors_enriched;
DROP TABLE IF EXISTS users_privileges;
DROP TABLE IF EXISTS users_constituencies;
DROP TABLE IF EXISTS users_wards;
DROP TABLE IF EXISTS privileges;
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
  write_access  TEXT NOT NULL DEFAULT FALSE,
  role          TEXT NOT NULL
);

CREATE TABLE privileges
(
  id         UUID NOT NULL PRIMARY KEY,
  permission TEXT CHECK (permission IN ('READ_VOTER', 'EDIT_VOTER'))
);

CREATE TABLE users_privileges
(
  id            UUID PRIMARY KEY,
  users_id      UUID REFERENCES users (id)      NOT NULL,
  privileges_id UUID REFERENCES privileges (id) NOT NULL
);

CREATE TABLE users_wards (
  id       UUID PRIMARY KEY,
  users_id UUID REFERENCES users (id)           NOT NULL,
  wards_id UUID REFERENCES wards (id)           NOT NULL
);

CREATE TABLE users_constituencies (
  id                UUID PRIMARY KEY,
  users_id          UUID REFERENCES users (id)                      NOT NULL,
  constituencies_id UUID REFERENCES constituencies (id)             NOT NULL
);

-- Trigger to update the modified date on update
-- CREATE OR REPLACE FUNCTION update_modified_column()
--   RETURNS TRIGGER AS $$
-- BEGIN
--   NEW.modified = now();
--   RETURN NEW;
-- END;
-- $$ LANGUAGE 'plpgsql';
--
-- CREATE TRIGGER update_voters_modtime BEFORE UPDATE ON voters FOR EACH ROW EXECUTE PROCEDURE update_modified_column();
