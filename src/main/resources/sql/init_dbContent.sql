INSERT INTO public.suppliers (name, description) VALUES ('Nespresso', 'coffee machine');
INSERT INTO public.suppliers (name, description) VALUES ('KeepCup', 'Portable cups');
INSERT INTO public.suppliers (name) VALUES ('Bialetti');
INSERT INTO public.suppliers (name) VALUES ('Hario');

INSERT INTO public.product_category (name, description, department) VALUES ('Coffee Machine', 'Machines for making coffee.', 'Hardware');
INSERT INTO public.product_category (name, description, department) VALUES ('Cup', '', 'Cups');
INSERT INTO public.product_category (name, description, department) VALUES ('Accessories', 'Accessories for speciality coffee.', 'Alternative');

INSERT INTO public.currencies (currency) VALUES ('USD');

INSERT INTO public.countries (name) VALUES ('Hungary');
INSERT INTO public.countries (name) VALUES ('United States');

INSERT INTO public.products (name, description, price, currency, supplier_id, product_category_id) VALUES ('Nespresso Krups XN740B CitiZ', 'Kapsule coffee machine.', '120', '1', '1', '1');
INSERT INTO public.products (name, description, price, currency, supplier_id, product_category_id) VALUES ('KeepCup Tall', 'Ideal for take away coffee', '10', '1', '2', '2');
INSERT INTO public.products (name, description, price, currency, supplier_id, product_category_id) VALUES ('KeepCup Medium', 'Ideal for take away coffee', '8', '1', '2', '2');
INSERT INTO public.products (name, description, price, currency, supplier_id, product_category_id) VALUES ('Bialetti Moka pot', 'Old time classic', '15', '1', '3', '3');
INSERT INTO public.products (name, description, price, currency, supplier_id, product_category_id) VALUES ('Hario Chemex', 'Coolest way to do filter coffee.', '25', '1', '4', '3');
INSERT INTO public.products (name, description, price, currency, supplier_id, product_category_id) VALUES ('Hario Manual Grinder', 'Fine grinder for your favourite coffee.', '17', '1', '4', '3');
