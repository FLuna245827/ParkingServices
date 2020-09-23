DROP TABLE IF EXISTS pricing_policy;
DROP TABLE IF EXISTS place;
DROP TABLE IF EXISTS parking;

CREATE TABLE pricing_policy(
	name VARCHAR(250) NOT NULL PRIMARY KEY,
	formule VARCHAR(250) NOT NULL
);

CREATE TABLE place(
	id INT AUTO_INCREMENT PRIMARY KEY,
	parking_id INT,
	car_type VARCHAR(30),
	slot_number INT,
	entrance_timestamp TIMESTAMP
);

CREATE TABLE parking(
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(250) NOT NULL,
	capacity_sedan INT NOT NULL,
	available_sedan VARCHAR(500),
	capacity_elow INT NOT NULL,
	available_elow VARCHAR(500),
	capacity_ehigh INT NOT NULL,
	available_ehigh VARCHAR(500),
	pricing_policy_name VARCHAR(250) NOT NULL,
	price_fixed_amount numeric(6,2),
	price_hour numeric(5,2)
);

INSERT INTO pricing_policy (name, formule)
	VALUES	('by_the_hour', 'hours*price_hour'),
			('fixed_and_by_the_hour', 'price_fixed_amount+hours*price_hour');

INSERT INTO parking (name, capacity_sedan, available_sedan, capacity_elow, available_elow, capacity_ehigh, available_ehigh, pricing_policy_name, price_fixed_amount, price_hour)
	VALUES	('P1', 150, NULL, 50, NULL, 30, NULL, 'fixed_and_by_the_hour', 29.99, 0.56),
			('P2', 0, NULL, 200, NULL, 100, NULL, 'by_the_hour', 0, 7.99);