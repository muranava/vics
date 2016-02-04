DROP TABLE IF EXISTS wards;
DROP TABLE IF EXISTS electors;
DROP TABLE IF EXISTS electors_enriched;

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
