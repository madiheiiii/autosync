/*CREATE UNIQUE INDEX UX_Reservation_OneActivePerVehicle
ON reservation (vehicle_id)
WHERE status = 'ACTIVE';
*/
/*
SELECT
    vehicle_id,
    COUNT(*) AS active_count
FROM dbo.reservation
WHERE status = 'ACTIVE'
GROUP BY vehicle_id
HAVING COUNT(*) > 1;

SELECT
    name,
    is_unique,
    filter_definition
FROM sys.indexes
WHERE object_id = OBJECT_ID('dbo.reservation')
  AND name = 'UX_Reservation_OneActivePerVehicle';

  INSERT INTO VEHICLE (MAKE, MODEL, VIN, YEAR) VALUES ('Toyota', 'Highlander', 'VIN345', 2000);
  INSERT INTO CUSTOMER (EMAIL, NAME) VALUES ('maddie@example.com','Maddie');
  INSERT INTO CUSTOMER (EMAIL, NAME) VALUES ('james@example.com', 'James');

  SELECT COUNT(*) AS active_count
FROM dbo.reservation
WHERE vehicle_id = 2
  AND status = 'ACTIVE';
  
  INSERT INTO VEHICLE (MAKE, MODEL, VIN, YEAR) VALUES ('Toyota', 'Camry', 'VIN354', 1980);
  

  SELECT
    id,
    customer_id,
    vehicle_id,
    status,
    created_at
FROM dbo.reservation
WHERE vehicle_id = 3
ORDER BY id DESC;
SELECT COUNT(*) AS active_count
FROM dbo.reservation
WHERE vehicle_id = 3
  AND status = 'ACTIVE';

  

  SELECT status, COUNT(*) AS reservation_count
FROM dbo.reservation
WHERE vehicle_id = 6
GROUP BY status;


CREATE SCHEMA [constraint];
GO

CREATE SCHEMA optimistic;
GO

CREATE SCHEMA pessimistic;
GO

-- VEHICLES

CREATE TABLE [constraint].vehicle (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    vin VARCHAR(17) NOT NULL UNIQUE,
    make VARCHAR(50),
    model VARCHAR(100),
    [year] INT,
    status VARCHAR(20) NOT NULL DEFAULT 'available'
        CHECK (status IN ('available', 'reserved'))
);

CREATE TABLE optimistic.vehicle (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    vin VARCHAR(17) NOT NULL UNIQUE,
    make VARCHAR(50),
    model VARCHAR(100),
    [year] INT,
    status VARCHAR(20) NOT NULL DEFAULT 'available'
        CHECK (status IN ('available', 'reserved')),
    version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE pessimistic.vehicle (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    vin VARCHAR(17) NOT NULL UNIQUE,
    make VARCHAR(50),
    model VARCHAR(100),
    [year] INT,
    status VARCHAR(20) NOT NULL DEFAULT 'available'
        CHECK (status IN ('available', 'reserved'))
);


-- CUSTOMERS

CREATE TABLE [constraint].customer (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    email VARCHAR(254) NOT NULL
);

CREATE TABLE optimistic.customer (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    email VARCHAR(254) NOT NULL
);

CREATE TABLE pessimistic.customer (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    email VARCHAR(254) NOT NULL
);


-- RESERVATIONS

CREATE TABLE [constraint].reservation (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,

    vehicle_id BIGINT NOT NULL
        REFERENCES [constraint].vehicle(id),

    customer_id BIGINT NOT NULL
        REFERENCES [constraint].customer(id),

    status VARCHAR(20) NOT NULL DEFAULT 'active'
        CHECK (status IN ('active', 'cancelled')),

    created_at DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME()
);

CREATE TABLE optimistic.reservation (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,

    vehicle_id BIGINT NOT NULL
        REFERENCES optimistic.vehicle(id),

    customer_id BIGINT NOT NULL
        REFERENCES optimistic.customer(id),

    status VARCHAR(20) NOT NULL DEFAULT 'active'
        CHECK (status IN ('active', 'cancelled')),

    created_at DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME()
);

CREATE TABLE pessimistic.reservation (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,

    vehicle_id BIGINT NOT NULL
        REFERENCES pessimistic.vehicle(id),

    customer_id BIGINT NOT NULL
        REFERENCES pessimistic.customer(id),

    status VARCHAR(20) NOT NULL DEFAULT 'active'
        CHECK (status IN ('active', 'cancelled')),

    created_at DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME()
);


-- CONSTRAINT STRATEGY:
-- At most one active reservation per vehicle.

CREATE UNIQUE INDEX UX_Reservation_OneActivePerVehicle
ON [constraint].reservation(vehicle_id)
WHERE status = 'active';

INSERT INTO optimistic.customer (name, email)
OUTPUT INSERTED.id
VALUES ('Alice', 'alice@example.com');

INSERT INTO optimistic.customer (name, email)
OUTPUT INSERTED.id
VALUES ('Bob', 'bob@example.com');

INSERT INTO optimistic.vehicle (vin, make, model, year, version)
OUTPUT INSERTED.id
VALUES ('TEST-OPT-001', 'Toyota', 'Corolla', 2024, 0);

SELECT * FROM optimistic.customer
Select * FROM optimistic.reservation
select * from optimistic.vehicle
SELECT id, customer_id, vehicle_id, status
FROM optimistic.reservation
WHERE vehicle_id = 2;

SELECT id, version
FROM optimistic.vehicle
WHERE id = 3;

SELECT vehicle_id, COUNT(*) AS active_reservations
FROM optimistic.reservation
WHERE status = 'ACTIVE'
GROUP BY vehicle_id;

INSERT INTO optimistic.vehicle (vin, make, model, year, version)
OUTPUT INSERTED.id
VALUES ('TEST-OPT-010', 'Mazda', 'one', 2024, 0);

INSERT INTO pessimistic.customer (name, email)
OUTPUT INSERTED.id
VALUES ('Alice', 'alice.pess@example.com');

INSERT INTO pessimistic.customer (name, email)
OUTPUT INSERTED.id
VALUES ('Bob', 'bob.pess@example.com');

INSERT INTO pessimistic.vehicle (vin, make, model, year)
OUTPUT INSERTED.id
VALUES ('TEST-PESS-008', 'Toyota', 'Corolla', 2024, 0);


SELECT id, customer_id, vehicle_id, status
FROM pessimistic.reservation
WHERE vehicle_id = 4;

   BEGIN TRAN;
   SELECT * FROM pessimistic.vehicle WITH (UPDLOCK, ROWLOCK) WHERE id = 8;
   ROLLBACK;
   -- leave this open, don't commit
   INSERT INTO pessimistic.vehicle (vin, make, model, year)
OUTPUT INSERTED.id
VALUES ('TEST-PESS-010', 'Toyota', 'Corolla', 2024);

SELECT id, customer_id, vehicle_id, status
FROM pessimistic.reservation
WHERE vehicle_id = 11
AND status = 'ACTIVE';

INSERT INTO optimistic.vehicle (vin, make, model, year, version)
OUTPUT INSERTED.id
VALUES ('TEST-OPT-EXPERIMENT-001', 'Honda', 'Civic', 2024, 0);

SELECT id, customer_id, vehicle_id, status
FROM optimistic.reservation
WHERE vehicle_id = 12
  AND status = 'ACTIVE';

  SELECT
    name,
    filter_definition
FROM sys.indexes
WHERE object_id = OBJECT_ID(N'[constraint].[reservation]')
  AND is_unique = 1;


ALTER TABLE [constraint].vehicle 
ALTER COLUMN vin VARCHAR(30);

  INSERT INTO [constraint].vehicle (vin, make, model, year)
OUTPUT INSERTED.id
VALUES ('TEST-CONSTRAINT-001', 'Toyota', 'Corolla', 2024);
*/

SELECT id, name
FROM [constraint].customer;

SELECT id
FROM [constraint].vehicle;
SELECT id, customer_id, vehicle_id, status
FROM [constraint].reservation
WHERE vehicle_id = 6
  AND status = 'ACTIVE';
