CREATE TABLE "users"
(
  id          uuid        NOT NULL PRIMARY KEY,
  created_at  timestamptz NOT NULL,
  modified_at timestamptz NOT NULL,
  version     bigint      NOT NULL,
  data        jsonb       NOT NULL
);

CREATE INDEX ix_user_id_lookup ON "users" (lower(data->>'id'));

CREATE TABLE "cars"
(
  id          uuid        NOT NULL PRIMARY KEY,
  created_at  timestamptz NOT NULL,
  modified_at timestamptz NOT NULL,
  version     bigint      NOT NULL,
  data        jsonb       NOT NULL
);

-- CREATE INDEX ix_car_regnr_lookup ON "cars" (lower(data->>'regNr'));
