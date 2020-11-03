GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA diku_mod_users_poc TO folio_admin;

CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE address_type(
   id UUID PRIMARY KEY,
   address_type VARCHAR (30) UNIQUE NOT NULL,
   description VARCHAR (255)
);

CREATE TABLE patron_group(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   name VARCHAR (100) UNIQUE NOT NULL,
   description VARCHAR (255),
   created_record_date TIMESTAMP,
   created_record_by_user_id UUID,
   created_record_by_username VARCHAR (100) UNIQUE,
   updated_record_date TIMESTAMP,
   updated_record_by_user_id UUID,
   updated_record_by_username VARCHAR (100) UNIQUE
);

CREATE TABLE users(
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   patron_group_id UUID,
   username VARCHAR (100) UNIQUE NOT NULL,
   external_system_id UUID,
   barcode VARCHAR (200) UNIQUE NOT NULL,
   active BOOLEAN NOT NULL,
   type VARCHAR (30) NOT NULL,
   enrollment_date TIMESTAMP NOT NULL,
   expiration_date TIMESTAMP,
   created_date TIMESTAMP NOT NULL,
   updated_date TIMESTAMP,
   lastname VARCHAR (100) NOT NULL,
   firstname VARCHAR (100) NOT NULL,
   middlename VARCHAR (100),
   email VARCHAR (100) UNIQUE NOT NULL,
   phone VARCHAR (30) UNIQUE NOT NULL,
   mobile_phone VARCHAR (30) UNIQUE,
   date_of_birth DATE NOT NULL,
   preferred_contact_type_id UUID,
   custom_fields JSONB,
   created_record_date TIMESTAMP,
   created_record_by_user_id UUID,
   created_record_by_username VARCHAR (100) UNIQUE,
   updated_record_date TIMESTAMP,
   updated_record_by_user_id UUID,
   updated_record_by_username VARCHAR (100) UNIQUE,
   CONSTRAINT patron_group_id_fk FOREIGN KEY (patron_group_id)
      REFERENCES patron_group (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE user_tag(
  user_id UUID NOT NULL,
  tag_id VARCHAR (50) NOT NULL,
  PRIMARY KEY (user_id, tag_id),
  CONSTRAINT tag_user_id_fk FOREIGN KEY (user_id)
      REFERENCES users (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE address(
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL,
  address_type_id UUID NOT NULL,
  country_id VARCHAR (10),
  address_line1 VARCHAR (100) NOT NULL,
  address_line2 VARCHAR (100) NOT NULL,
  city VARCHAR (100) NOT NULL,
  region VARCHAR (150) NOT NULL,
  postal_code VARCHAR (30) NOT NULL,
  primary_address BOOLEAN NOT NULL,
  CONSTRAINT address_user_id_fk FOREIGN KEY (user_id)
      REFERENCES users (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT address_type_id_fk FOREIGN KEY (address_type_id)
      REFERENCES address_type (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE proxy_for(
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL,
  proxy_user_id UUID NOT NULL,
  request_for_sponsor VARCHAR (3),
  created_date TIMESTAMP,
  notifications_to VARCHAR (100),
  accrue_to VARCHAR (100),
  expiration_date TIMESTAMP,
  status VARCHAR (10),
  created_record_date TIMESTAMP,
  created_record_by_user_id UUID ,
  created_record_by_username VARCHAR (100) UNIQUE,
  updated_record_date TIMESTAMP,
  updated_record_by_user_id UUID,
  updated_record_by_username VARCHAR (100) UNIQUE,
  unique (user_id, proxy_user_id),
  CONSTRAINT user_id_for_proxy_fk FOREIGN KEY (user_id)
      REFERENCES users (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT proxy_user_id_fk FOREIGN KEY (proxy_user_id)
      REFERENCES users (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
);
