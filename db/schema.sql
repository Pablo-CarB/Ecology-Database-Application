DROP DATABASE IF EXISTS ecosystem_db;
CREATE DATABASE ecosystem_db
    CHARACTER SET utf8
    COLLATE utf8_general_ci;
USE ecosystem_db;

-- Domains Table
CREATE TABLE Domain (
    domain_name VARCHAR(50) PRIMARY KEY
);

-- Kingdoms Table
CREATE TABLE Kingdom (
    kingdom_name VARCHAR(50) PRIMARY KEY,
    domain_name VARCHAR(50),
    FOREIGN KEY (domain_name) REFERENCES Domain(domain_name) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Phylum Table
CREATE TABLE Phylum (
    phylum_name VARCHAR(50) PRIMARY KEY,
    kingdom_name VARCHAR(50),
    FOREIGN KEY (kingdom_name) REFERENCES Kingdom(kingdom_name) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Genus Table
CREATE TABLE Genus (
    genus_name VARCHAR(50) PRIMARY KEY,
    phylum_name VARCHAR(50),
    FOREIGN KEY (phylum_name) REFERENCES Phylum(phylum_name) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Diet Table
CREATE TABLE Diet (
    diet_name VARCHAR(50) PRIMARY KEY
);

-- Feeding Strategy Table
CREATE TABLE FeedingStrategy (
    strategy_name VARCHAR(50) PRIMARY KEY
);

CREATE TABLE Species (
    genus_name VARCHAR(50),
    specific_name VARCHAR(50),
    common_name VARCHAR(100) NOT NULL, -- e.g., "Apple"
    conservation_status ENUM('Extinct', 'Endangered', 'Vulnerable', 'Least Concern', 'Not Evaluated') DEFAULT 'Not Evaluated',
    year_described INT CHECK (year_described > 0), -- Year the species was first described
    gregarious BOOLEAN DEFAULT FALSE, -- social?
    diet_name VARCHAR(50), 
    strategy_name VARCHAR(50), 
    PRIMARY KEY (genus_name, specific_name), 
    FOREIGN KEY (genus_name) REFERENCES Genus(genus_name) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (diet_name) REFERENCES Diet(diet_name) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (strategy_name) REFERENCES FeedingStrategy(strategy_name) ON DELETE SET NULL ON UPDATE CASCADE
);

/*
DELIMITER //
CREATE TRIGGER BeforeInsertSpecies
BEFORE INSERT ON Species
FOR EACH ROW
BEGIN
    IF NOT EXISTS (
        SELECT 1 
        FROM Genus 
        WHERE genus_name = NEW.genus_name
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Genus does not exist in the Genus table';
    END IF;
END //
DELIMITER ;
*/

-- Region Table
CREATE TABLE Region (
    region_id INT AUTO_INCREMENT PRIMARY KEY,
    region_name VARCHAR(100) UNIQUE,
    latitude DECIMAL(6,3), -- ranges from -90.000 to 90.000
    longitude DECIMAL(7,3) -- ranges from -180.000 to 180.000
);

-- Biome Table
CREATE TABLE Biome (
    biome_name VARCHAR(50) UNIQUE PRIMARY KEY,
    region_id INT,
    FOREIGN KEY (region_id) REFERENCES Region(region_id) ON DELETE CASCADE ON UPDATE CASCADE
);

DELIMITER //
CREATE TRIGGER BeforeInsertBiome
BEFORE INSERT ON Biome
FOR EACH ROW
BEGIN
    IF NOT EXISTS (
        SELECT 1 
        FROM Region
        WHERE region_id = NEW.region_id
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Region does not exist in the Region table';
    END IF;
END //

DELIMITER ;


--  Species_Biome Table
CREATE TABLE Species_Biome (
    genus_name VARCHAR(50),
    specific_name VARCHAR(50),
    biome_name VARCHAR(50),
    PRIMARY KEY (genus_name, specific_name, biome_name),
    FOREIGN KEY (genus_name, specific_name) REFERENCES Species(genus_name, specific_name) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (biome_name) REFERENCES Biome(biome_name) ON DELETE CASCADE ON UPDATE CASCADE
);

DELIMITER //
CREATE TRIGGER BeforeInsertSpeciesBiome
BEFORE INSERT ON Species_Biome
FOR EACH ROW
BEGIN
    IF NOT EXISTS (
        SELECT 1 
        FROM Species 
        WHERE genus_name = NEW.genus_name AND specific_name = NEW.specific_name
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Species does not exist in the Species table';
    END IF;
END //
DELIMITER ;

DELIMITER //
CREATE FUNCTION GetSpeciesInRegion(region_name VARCHAR(100)) RETURNS JSON
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE species_list JSON;
    SELECT JSON_ARRAYAGG(CONCAT(s.genus_name, ' ', s.specific_name))
    INTO species_list
    FROM Species s
    JOIN Species_Biome sb ON s.genus_name = sb.genus_name AND s.specific_name = sb.specific_name
    JOIN Biome b ON sb.biome_name = b.biome_name
    JOIN Region r ON b.region_id = r.region_id
    WHERE r.region_name = region_name;

    RETURN species_list;
END //
DELIMITER ;

-- Species Relationship Table
CREATE TABLE Species_Relationship (
    genus1_name VARCHAR(50),
    specific1_name VARCHAR(50),
    genus2_name VARCHAR(50),
    specific2_name VARCHAR(50),
    relationship_type ENUM('Predation', 'Mutualism', 'Parasitism', 'Competition', 'Commensalism'),
    PRIMARY KEY (genus1_name, specific1_name, genus2_name, specific2_name),
    FOREIGN KEY (genus1_name, specific1_name) REFERENCES Species(genus_name, specific_name) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (genus2_name, specific2_name) REFERENCES Species(genus_name, specific_name) ON DELETE CASCADE ON UPDATE CASCADE
);

DELIMITER //
CREATE TRIGGER BeforeInsertSpeciesRelationship
BEFORE INSERT ON Species_Relationship
FOR EACH ROW
BEGIN
    IF NOT EXISTS (
        SELECT 1 
        FROM Species
        WHERE genus_name = NEW.genus1_name AND specific_name = NEW.specific1_name
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'First species in the relationship does not exist in the Species table';
    END IF;
    IF NOT EXISTS (
        SELECT 1 
        FROM Species
        WHERE genus_name = NEW.genus2_name AND specific_name = NEW.specific2_name
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Second species in the relationship does not exist in the Species table';
    END IF;
END //
DELIMITER ;

-- Updated ConservationStatusLog Table
CREATE TABLE ConservationStatusLog (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    genus_name VARCHAR(50) NOT NULL,
    specific_name VARCHAR(50) NOT NULL,
    old_status ENUM('Extinct', 'Endangered', 'Vulnerable', 'Least Concern', 'Not Evaluated'),
    new_status ENUM('Extinct', 'Endangered', 'Vulnerable', 'Least Concern', 'Not Evaluated'),
    change_date DATETIME NOT NULL,
    FOREIGN KEY (genus_name, specific_name) REFERENCES Species(genus_name, specific_name) ON DELETE CASCADE ON UPDATE CASCADE
);

-- User-Defined Function: Get Conservation Status
DELIMITER //
CREATE FUNCTION GetConservationStatus(genus_name VARCHAR(50), specific_name VARCHAR(50)) 
RETURNS VARCHAR(50)
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE status VARCHAR(50);
    SELECT conservation_status 
    INTO status
    FROM Species
    WHERE genus_name = genus_name AND specific_name = specific_name;
    RETURN status;
END //
DELIMITER ;

-- Trigger: Update Conservation Status Log
DELIMITER //
CREATE TRIGGER UpdateConservationStatus 
AFTER UPDATE ON Species
FOR EACH ROW
BEGIN
    INSERT INTO ConservationStatusLog(genus_name, specific_name, old_status, new_status, change_date)
    VALUES (OLD.genus_name, OLD.specific_name, OLD.conservation_status, NEW.conservation_status, NOW());
END //
DELIMITER ;
