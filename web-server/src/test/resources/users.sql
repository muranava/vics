INSERT INTO users (id, username, password_hash, role) VALUES
  ('a54a4e73-943d-41e0-ae05-f6b507ad777e', 'admin', '$2a$10$6qa.jBGm4VWmzh4.7sV6fuX/Bt60mMmDd0IXrko.F2yelKi56UlPq', 'ADMIN'),
  ('a93fba9a-6022-4857-843a-75fccb31e67e', 'user', '$2a$10$PxFJ8sRkU2C7NT3tp1jejuJ5ymSF9GTnlc2t3/W1JWIYpNSnW334a', 'USER');

INSERT INTO "privileges" (id, permission) VALUES
  ('3f89506c-fd00-4b1e-aefc-2186d075439d', 'READ_VOTER'),
  ('5044556a-e90b-4bd3-b1e1-27f43e9a405f', 'EDIT_VOTER');

INSERT INTO users_privileges (id, users_id, privileges_id) VALUES
  ('2db236ba-32f7-4d1f-92f0-3321d7828b9b', 'a93fba9a-6022-4857-843a-75fccb31e67e', '3f89506c-fd00-4b1e-aefc-2186d075439d'),
  ('1337d2ac-adc8-4d76-820a-480a215de47c', 'a93fba9a-6022-4857-843a-75fccb31e67e', '5044556a-e90b-4bd3-b1e1-27f43e9a405f');
