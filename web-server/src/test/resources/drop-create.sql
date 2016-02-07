DROP TABLE IF EXISTS wards;
DROP TABLE IF EXISTS electors;
DROP TABLE IF EXISTS electors_enriched;
DROP TABLE IF EXISTS users;

CREATE TABLE wards (
  id                UUID PRIMARY KEY NOT NULL,
  constituency_name TEXT             NOT NULL,
  constituency_code TEXT             NOT NULL,
  ward_name         TEXT             NOT NULL,
  ward_code         TEXT             NOT NULL
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
  id UUID PRIMARY KEY NOT NULL,
  username TEXT NOT NULL UNIQUE,
  password_hash TEXT NOT NULL,
  role TEXT NOT NULL
);

INSERT INTO public.users (id, username, password_hash, role) VALUES ('a54a4e73-943d-41e0-ae05-f6b507ad777e', 'admin', '$2a$10$6qa.jBGm4VWmzh4.7sV6fuX/Bt60mMmDd0IXrko.F2yelKi56UlPq', 'ADMIN');
INSERT INTO public.users (id, username, password_hash, role) VALUES ('a54a4e73-943d-41e0-ae05-f6b507ad222a', 'user', '$2a$10$l2SChE5U/Fc8t9DL0z/RkOST16PvVPUx9qF65VfvKt9s6L6zX2xbq', 'USER');

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
