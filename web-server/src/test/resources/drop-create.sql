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
