-- Create a schema for the BusData
CREATE SCHEMA IF NOT EXISTS bus_data_schema;

-- Create a table to store BusData
CREATE TABLE IF NOT EXISTS bus_data_schema.bus_data (
    time TIMESTAMP,
    lat DECIMAL(10, 6),
    lon DECIMAL(10, 6),
    head VARCHAR(255),
    fix VARCHAR(255),
    route VARCHAR(255),
    stop VARCHAR(255),
    next VARCHAR(255),
    code VARCHAR(255),
    PRIMARY KEY (time)
);

