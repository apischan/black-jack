----------------------------------------------------------------------------------------

-- player ddl
CREATE TABLE user
(
  id integer NOT NULL,
  username character(100) NOT NULL,
  balance bigint,
  CONSTRAINT user_pkey PRIMARY KEY (id)
);

CREATE SEQUENCE user_id_seq START WITH 2 INCREMENT BY 1;

-- ALTER TABLE user ALTER COLUMN id SET DEFAULT (VALUES NEXT VALUE FOR user_id_seq);

----------------------------------------------------------------------------------------

-- round ddl
CREATE TABLE round
(
  id integer NOT NULL,
  user_id integer NOT NULL,
  bet bigint,
  status character(20),
  CONSTRAINT round_pkey PRIMARY KEY (id)
);

CREATE SEQUENCE round_id_seq START WITH 2 INCREMENT BY 1;

----------------------------------------------------------------------------------------

--bet_log ddl
--CREATE TABLE bet_log
--(
--  id integer NOT NULL,
--  amount bigint,
--  game_id integer,
--  CONSTRAINT bet_log_pkey PRIMARY KEY (id)
--);

---------------------------------------------------------------------------------------

--card_log ddl
CREATE TABLE card_log
(
  id integer NOT NULL,
  game_id integer,
  player_role character(20),
  rank character(20),
  suit character(20),
  CONSTRAINT card_log_pkey PRIMARY KEY (id)
);

CREATE SEQUENCE card_log_id_seq START WITH 2 INCREMENT BY 1;

---------------------------------------------------------------------------------------