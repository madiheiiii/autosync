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

  */

  SELECT status, COUNT(*) AS reservation_count
FROM dbo.reservation
WHERE vehicle_id = 6
GROUP BY status;