
-- test users (NOT used in production)

-- passwords are same as username
INSERT INTO users (id, username, password_hash, role) VALUES
  ('a54a4e73-943d-41e0-ae05-f6b507ad777e', 'admin', '$2a$10$EYR29apxaFOmIB11ygJ/KuioiNatkGY0fynYPB0cX8PntQO3Q7Wi2', 'ADMIN'),
  ('63f93970-d065-4fbb-8b9c-941e27ea53dc', 'covs', '$2a$10$sZ7dc4TLhm7ETGM6XVqDeeqHWPX2sNQsMSZt1fNeGPl0pPrdrcppC', 'USER'),
  ('196af608-6d7a-4981-a6a0-ed8999b3b89c', 'earlsdon', '$2a$10$vpBm0Xou2EPYYMdbFb7fD.jmCzEewljYW7YwpP8C2gaoHM8qhOOfu', 'USER');

INSERT INTO privileges (id, permission) VALUES
  ('3f89506c-fd00-4b1e-aefc-2186d075439d', 'READ_VOTER'),
  ('5044556a-e90b-4bd3-b1e1-27f43e9a405f', 'EDIT_VOTER');

INSERT INTO users_privileges (id, users_id, privileges_id) VALUES
  -- covs READ/WRITE
  ('2db236ba-32f7-4d1f-92f0-3321d7828b9b', '63f93970-d065-4fbb-8b9c-941e27ea53dc', '3f89506c-fd00-4b1e-aefc-2186d075439d'),
  ('1337d2ac-adc8-4d76-820a-480a215de47c', '63f93970-d065-4fbb-8b9c-941e27ea53dc', '5044556a-e90b-4bd3-b1e1-27f43e9a405f'),

  -- earlsdon READ ONLY
  ('656a26a9-9137-4054-bca7-33c0f2c4c74d', '196af608-6d7a-4981-a6a0-ed8999b3b89c', '3f89506c-fd00-4b1e-aefc-2186d075439d');

INSERT INTO users_constituencies (id, users_id, constituencies_id) VALUES
  -- covs -> covs const
  ('e9f21f52-471a-425d-9909-8a9435dfbe35', '63f93970-d065-4fbb-8b9c-941e27ea53dc', '0d338b99-3d15-44f7-904f-3ebc18a7ab4a');

INSERT INTO users_wards (id, users_id, wards_id) VALUES
  -- earlsdon -> earlsdon ward
  ('5ea2448a-034e-4703-b666-274f162ddf75', '196af608-6d7a-4981-a6a0-ed8999b3b89c', '585aedd2-6f46-4718-854b-1856a3b1f3c1');
