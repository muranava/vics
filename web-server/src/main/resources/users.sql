

-- passwords are same as username
INSERT INTO users (id, username, password_hash, role) VALUES
  ('a54a4e73-943d-41e0-ae05-f6b507ad777e', 'admin', '$2a$10$0qF8dDUyjN8Pc.AkS4twGuG/9siJzjNdIhMCphdY.Su7/XZLmDbW.', 'ADMIN'),
  ('81ac0293-727f-4a56-a079-54b6c0807818', 'covs', '$2a$10$XI09e.gCergYS8M/VUl6AesVEsBVzaw9NegWxYefCrJn0yfLlgnLq', 'USER'),
  ('757c4eb8-bf34-470f-8b8a-d46bd022211b', 'earlsdon', '$2a$10$w7EQKkpvv2PDLNQsAeQo2OhozUofUGLqPOKEAkjjTJMuimxjO1kO2', 'USER');

INSERT INTO privileges (id, permission) VALUES
  ('3f89506c-fd00-4b1e-aefc-2186d075439d', 'READ_VOTER'),
  ('5044556a-e90b-4bd3-b1e1-27f43e9a405f', 'EDIT_VOTER');

INSERT INTO users_privileges (id, users_id, privileges_id) VALUES
  -- covs READ/WRITE
  ('2db236ba-32f7-4d1f-92f0-3321d7828b9b', '81ac0293-727f-4a56-a079-54b6c0807818', '3f89506c-fd00-4b1e-aefc-2186d075439d'),
  ('1337d2ac-adc8-4d76-820a-480a215de47c', '81ac0293-727f-4a56-a079-54b6c0807818', '5044556a-e90b-4bd3-b1e1-27f43e9a405f'),

  -- earlsdon READ ONLY
  ('656a26a9-9137-4054-bca7-33c0f2c4c74d', '757c4eb8-bf34-470f-8b8a-d46bd022211b', '3f89506c-fd00-4b1e-aefc-2186d075439d');

INSERT INTO users_constituencies (id, users_id, constituencies_id) VALUES
  -- covs -> covs const
  ('e9f21f52-471a-425d-9909-8a9435dfbe35', '81ac0293-727f-4a56-a079-54b6c0807818', 'b4aac137-2cc5-469f-b02e-b8bdb0fdde9c');

INSERT INTO users_wards (id, users_id, wards_id) VALUES
  -- earlsdon -> earlsdon ward
  ('5ea2448a-034e-4703-b666-274f162ddf75', '757c4eb8-bf34-470f-8b8a-d46bd022211b', '0f5f7b6a-7b35-430a-81c1-53736cdd95a1');