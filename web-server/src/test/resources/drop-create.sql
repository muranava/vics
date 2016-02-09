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
  id           UUID PRIMARY KEY NOT NULL,
  constituency UUID REFERENCES constituencies(id),
  name    TEXT             NOT NULL,
  code    TEXT             NOT NULL
);

CREATE TABLE electors (
  id               UUID NOT NULL PRIMARY KEY,
  ward_code        TEXT NOT NULL,
  polling_district TEXT NOT NULL,
  elector_id       TEXT NOT NULL,
  elector_suffix   TEXT,
  ern              TEXT NOT NULL,
  title            TEXT,
  first_name       TEXT,
  last_name        TEXT,
  initial          TEXT,
  dob              DATE,
  flag             TEXT,
  modified         TIMESTAMP,
  created          TIMESTAMP DEFAULT now()
);

-- temporary table that is used to populate pdfs for testing
CREATE TABLE electors_enriched (
  id               UUID NOT NULL PRIMARY KEY,
  ward_code        TEXT NOT NULL,
  polling_district TEXT NOT NULL,
  elector_id       TEXT NOT NULL,
  elector_suffix   TEXT,
  ern              TEXT NOT NULL,
  title            TEXT,
  first_name       TEXT,
  last_name        TEXT,
  initial          TEXT,
  dob              DATE,
  flag             TEXT,
  modified         TIMESTAMP,
  created          TIMESTAMP DEFAULT now(),
  house            TEXT,
  street           TEXT,
  post_code        TEXT,
  upid             TEXT
);

CREATE TABLE users
(
  id            UUID PRIMARY KEY,
  username      TEXT NOT NULL UNIQUE,
  password_hash TEXT NOT NULL,
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

-- CREATE TABLE users_constituencies (
--   id                UUID PRIMARY KEY,
--   users_id          UUID REFERENCES users (id)                      NOT NULL,
--   constituencies_id UUID REFERENCES constituencies (id)             NOT NULL
-- );

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
