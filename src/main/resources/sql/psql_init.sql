ALTER TABLE IF EXISTS ONLY public.products DROP CONSTRAINT IF EXISTS fk1_products CASCADE;
ALTER TABLE IF EXISTS ONLY public.products DROP CONSTRAINT IF EXISTS fk2_products CASCADE;
ALTER TABLE IF EXISTS ONLY public.products DROP CONSTRAINT IF EXISTS fk3_products CASCADE;
ALTER TABLE IF EXISTS ONLY public.orders DROP CONSTRAINT IF EXISTS fk4_orders CASCADE;
ALTER TABLE IF EXISTS ONLY public.orders DROP CONSTRAINT IF EXISTS fk5_orders CASCADE;
ALTER TABLE IF EXISTS ONLY public.orders DROP CONSTRAINT IF EXISTS fk6_orders CASCADE;
ALTER TABLE IF EXISTS ONLY public.user_orders DROP CONSTRAINT IF EXISTS fk7_user_orders CASCADE;
ALTER TABLE IF EXISTS ONLY public.user_orders DROP CONSTRAINT IF EXISTS fk8_user_orders CASCADE;
ALTER TABLE IF EXISTS ONLY public.delivery_addresses DROP CONSTRAINT IF EXISTS fk9_delivery_addresses CASCADE;
ALTER TABLE IF EXISTS ONLY public.order_products DROP CONSTRAINT IF EXISTS fk10_order_products CASCADE;
ALTER TABLE IF EXISTS ONLY public.order_products DROP CONSTRAINT IF EXISTS fk11_order_products CASCADE;

ALTER TABLE IF EXISTS ONLY public.suppliers DROP CONSTRAINT IF EXISTS pk1_suppliers CASCADE;
ALTER TABLE IF EXISTS ONLY public.product_category DROP CONSTRAINT IF EXISTS pk2_product_categories CASCADE;
ALTER TABLE IF EXISTS ONLY public.currencies DROP CONSTRAINT IF EXISTS pk3_currencies CASCADE;
ALTER TABLE IF EXISTS ONLY public.products DROP CONSTRAINT IF EXISTS pk4_products CASCADE;
ALTER TABLE IF EXISTS ONLY public.countries DROP CONSTRAINT IF EXISTS pk5_countries CASCADE;
ALTER TABLE IF EXISTS ONLY public.users DROP CONSTRAINT IF EXISTS pk6_users CASCADE;
ALTER TABLE IF EXISTS ONLY public.statuses DROP CONSTRAINT IF EXISTS pk7_statuses CASCADE;
ALTER TABLE IF EXISTS ONLY public.orders DROP CONSTRAINT IF EXISTS pk8_orders CASCADE;
ALTER TABLE IF EXISTS ONLY public.delivery_addresses DROP CONSTRAINT IF EXISTS pk9_delivery_addresses CASCADE;

DROP DOMAIN IF EXISTS validemail CASCADE ;
DROP FUNCTION IF EXISTS valid_email CASCADE;
DROP EXTENSION IF EXISTS plperlu CASCADE;


DROP TABLE IF EXISTS order_products;
DROP TABLE IF EXISTS user_orders;
DROP TABLE IF EXISTS delivery_addresses;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS statuses;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS currencies;
DROP TABLE IF EXISTS product_category;
DROP TABLE IF EXISTS suppliers;
DROP TABLE IF EXISTS countries;
DROP TABLE IF EXISTS users;

CREATE EXTENSION plperlu;
CREATE FUNCTION valid_email(text)
  RETURNS boolean
  LANGUAGE plperlu
  IMMUTABLE LEAKPROOF STRICT AS
$$
  use Email::Valid;
  my $email = shift;
  Email::Valid->address($email) or die "Invalid email address: $email\n";
  return 'true';
$$;

CREATE DOMAIN validemail AS text NOT NULL
  CONSTRAINT validemail_check CHECK (valid_email(VALUE));


CREATE TABLE suppliers (
  id SMALLSERIAL NOT NULL UNIQUE,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255),
  active BOOLEAN DEFAULT TRUE);

CREATE TABLE product_category (
  id SMALLSERIAL NOT NULL UNIQUE,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255),
  department VARCHAR(255) NOT NULL,
  active BOOLEAN DEFAULT TRUE);

CREATE TABLE currencies (
  id SMALLSERIAL NOT NULL UNIQUE,
  currency VARCHAR(5) NOT NULL);

CREATE TABLE products (
  id SMALLSERIAL NOT NULL UNIQUE,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255),
  price REAL NOT NULL,
  stock SMALLINT DEFAULT '0',
  active BOOLEAN DEFAULT TRUE,
  currency SMALLINT NOT NULL,
  supplier_id SMALLINT NOT NULL,
  product_category_id SMALLINT NOT NULL);

CREATE TABLE countries (
  id SMALLSERIAL NOT NULL UNIQUE,
  name VARCHAR NOT NULL);

CREATE TABLE users (
  id SMALLSERIAL NOT NULL UNIQUE,
  user_name VARCHAR(50) NOT NULL,
  email validemail NOT NULL UNIQUE,
  password VARCHAR NOT NULL);

CREATE TABLE statuses (
  id SMALLSERIAL NOT NULL UNIQUE,
  status VARCHAR NOT NULL);

CREATE TABLE delivery_addresses (
  id SMALLSERIAL NOT NULL UNIQUE,
  country SMALLINT NOT NULL,
  state VARCHAR,
  postal_code VARCHAR NOT NULL,
  city VARCHAR NOT NULL,
  street VARCHAR NOT NULL,
  house_number VARCHAR NOT NULL,
  storey VARCHAR,
  door VARCHAR,
  first_name VARCHAR(50) NOT NULL,
  last_name VARCHAR(50) NOT NULL);

CREATE TABLE orders (
  id SMALLSERIAL NOT NULL UNIQUE,
  order_date TIMESTAMP NOT NULL DEFAULT now(),
  latest_update TIMESTAMP NOT NULL DEFAULT now(),
  status SMALLINT NOT NULL DEFAULT 1,
  user_id SMALLINT NOT NULL,
  delivery_address SMALLINT);

CREATE TABLE user_orders (
  user_id SMALLINT NOT NULL,
  order_id SMALLINT NOT NULL);

CREATE TABLE order_products (
  order_id SMALLINT NOT NULL,
  product_id SMALLINT NOT NULL,
  quantity SMALLINT NOT NULL DEFAULT '1',
  price DECIMAL NOT NULL);

ALTER TABLE suppliers ADD CONSTRAINT pk1_suppliers PRIMARY KEY (id);
ALTER TABLE product_category ADD CONSTRAINT pk2_product_categories PRIMARY KEY (id);
ALTER TABLE currencies ADD CONSTRAINT pk3_currencies PRIMARY KEY (id);
ALTER TABLE products ADD CONSTRAINT pk4_products PRIMARY KEY (id);
ALTER TABLE countries ADD CONSTRAINT pk5_countries PRIMARY KEY (id);
ALTER TABLE users ADD CONSTRAINT pk6_users PRIMARY KEY (id);
ALTER TABLE statuses ADD CONSTRAINT pk7_statuses PRIMARY KEY (id);
ALTER TABLE orders ADD CONSTRAINT pk8_orders PRIMARY KEY (id);
ALTER TABLE delivery_addresses ADD CONSTRAINT pk9_delivery_addresses PRIMARY KEY (id);

ALTER TABLE ONLY products ADD CONSTRAINT fk1_products FOREIGN KEY (supplier_id) REFERENCES suppliers(id);
ALTER TABLE ONLY products ADD CONSTRAINT fk2_products FOREIGN KEY (product_category_id) REFERENCES product_category(id);
ALTER TABLE ONLY products ADD CONSTRAINT fk3_products FOREIGN KEY (currency) REFERENCES currencies(id);
ALTER TABLE ONLY orders ADD CONSTRAINT fk4_orders FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE ONLY orders ADD CONSTRAINT fk5_orders FOREIGN KEY (status) REFERENCES statuses(id);
ALTER TABLE ONLY orders ADD CONSTRAINT fk6_orders FOREIGN KEY (delivery_address) REFERENCES delivery_addresses(id);
ALTER TABLE ONLY user_orders ADD CONSTRAINT fk7_user_orders FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE ONLY user_orders ADD CONSTRAINT fk8_user_orders FOREIGN KEY (order_id) REFERENCES orders(id);
ALTER TABLE ONLY delivery_addresses ADD CONSTRAINT fk9_delivery_addresses FOREIGN KEY (country) REFERENCES countries(id);
ALTER TABLE ONLY order_products ADD CONSTRAINT fk10_order_products FOREIGN KEY (order_id) REFERENCES orders(id);
ALTER TABLE ONLY order_products ADD CONSTRAINT fk11_order_products FOREIGN KEY (product_id) REFERENCES products(id);

INSERT INTO statuses (status) VALUES ('new');
INSERT INTO statuses (status) VALUES ('checked');
INSERT INTO statuses (status) VALUES ('paid');
INSERT INTO statuses (status) VALUES ('confirmed');
INSERT INTO statuses (status) VALUES ('shipped');
