CREATE DATABASE eTicketschema;
USE eTicketschema;

CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(150) UNIQUE,
  phone VARCHAR(20) UNIQUE,
  password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE routes (
  id INT AUTO_INCREMENT PRIMARY KEY,
  source VARCHAR(100),
  destination VARCHAR(100)
);

-- mapping table: which transport types are allowed for each route
CREATE TABLE route_transports (
  id INT AUTO_INCREMENT PRIMARY KEY,
  route_id INT NOT NULL,
  transport_type VARCHAR(20) NOT NULL, -- 'BUS' / 'TRAIN'
  UNIQUE(route_id, transport_type),
  FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE
);

CREATE TABLE schedules (
  id INT AUTO_INCREMENT PRIMARY KEY,
  route_id INT,
  transport_type VARCHAR(20), -- 'BUS' / 'TRAIN'
  travel_date DATE,
  travel_time TIME,
  total_seats INT,
  seats_left INT,
  seat_map TEXT, -- CSV or JSON of reserved seats
  FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE
);

CREATE TABLE tickets (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  schedule_id INT,
  seats VARCHAR(255), -- comma separated seat numbers
  booking_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  payment_mode VARCHAR(50),
  status VARCHAR(20), -- 'BOOKED' / 'CANCELLED'
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (schedule_id) REFERENCES schedules(id) ON DELETE CASCADE
);

-- Seed users
INSERT INTO users (name, email, phone, password_hash) VALUES
  ('Haseeb Hassan', 'haseeb@gmail.com', '03001234567', '1234'),
  ('Akbar', 'akbar@gmail.com', '03008901234', '5678');

-- Seed routes (IDs will be 1..5 in this order)
INSERT INTO routes (source, destination) VALUES
  ('Karachi', 'Lahore'),       -- id = 1
  ('Karachi', 'Islamabad'),    -- id = 2
  ('Lahore', 'Islamabad'),     -- id = 3
  ('Islamabad', 'Peshawar'),   -- id = 4
  ('Multan', 'Karachi');       -- id = 5

-- Seed allowed transport types for each route
-- Adjust transport choices to reflect which route supports BUS and/or TRAIN
INSERT INTO route_transports (route_id, transport_type) VALUES
  (1, 'BUS'),    -- Karachi -> Lahore (bus)
  (1, 'TRAIN'),  -- Karachi -> Lahore (train)
  (2, 'BUS'),    -- Karachi -> Islamabad (bus only)
  (3, 'TRAIN'),  -- Lahore -> Islamabad (train only)
  (3, 'BUS'),    -- Lahore -> Islamabad (bus too)
  (4, 'BUS'),    -- Islamabad -> Peshawar (bus only)
  (5, 'BUS');    -- Multan -> Karachi (bus only)

-- Seed schedules (note: seat_map is empty initially)
INSERT INTO schedules (route_id, transport_type, travel_date, travel_time, total_seats, seats_left, seat_map) VALUES
  (1, 'BUS',   '2025-01-20', '09:00:00',  40, 40, ''),   -- Karachi -> Lahore bus
  (1, 'BUS',   '2025-01-20', '16:30:00',  40, 40, ''),
  (1, 'TRAIN', '2025-01-20', '06:00:00', 200,200, ''),
  (2, 'BUS',   '2025-01-21', '10:45:00',  40, 40, ''),   -- Karachi -> Islamabad bus
  (3, 'TRAIN', '2025-01-21', '19:15:00', 150,150, ''),   -- Lahore -> Islamabad train
  (3, 'BUS',   '2025-01-22', '14:00:00',  40, 40, ''),   -- Lahore -> Islamabad bus
  (4, 'BUS',   '2025-01-23', '11:00:00',  40, 40, ''),   -- Islamabad -> Peshawar bus
  (5, 'BUS',   '2025-01-20', '20:00:00',  40, 40, ''),   -- Multan -> Karachi bus
  (5, 'BUS',   '2025-01-21', '05:30:00',  40, 40, ''),
  (1, 'TRAIN', '2025-01-21', '08:30:00', 180,180, '');   -- additional Karachi->Lahore train

-- Seed tickets (existing bookings)
INSERT INTO tickets (user_id, schedule_id, seats, payment_mode, status) VALUES
  (1, 1, '5,6', 'Cash', 'BOOKED'),
  (1, 3, '12,13,14', 'BankTransfer', 'BOOKED'),
  (2, 2, '1', 'Cash', 'BOOKED');