
/*

TODO
- figure out representation for species name that maintains genus-species entity relationshop
	* should probably create genus entity if the one inserted doesn't exist
- ensure that names are case-insensitive (ie "Canis" and "CANIS" cannot exist as two seperate genera)
- add species-species relationship tables
- add biome and region info
*/

DROP DATABASE IF EXISTS ecosystem_db;
CREATE DATABASE ecosystem_db
	CHARACTER SET utf8
    COLLATE utf8_general_ci;
USE ecosystem_db;


-- taxonomic heirarchy

CREATE TABLE Domain(
	domain_name VARCHAR(32) PRIMARY KEY
);

CREATE TABLE Kingdom(
	kingdom_name VARCHAR(32) PRIMARY KEY,
    domain_name  VARCHAR(32) NULL,
    FOREIGN KEY (domain_name) REFERENCES Domain(domain_name) ON DELETE SET NULL
);

CREATE TABLE Class(
	class_name VARCHAR(32) PRIMARY KEY,
    kingdom_name  VARCHAR(32) NULL,
    FOREIGN KEY (kingdom_name) REFERENCES Kingdom(kingdom_name) ON DELETE SET NULL
);

CREATE TABLE `Order`(
	order_name VARCHAR(32) PRIMARY KEY,
    class_name  VARCHAR(32) NULL,
    FOREIGN KEY (class_name) REFERENCES Class(class_name) ON DELETE SET NULL
);

CREATE TABLE Family(
	family_name VARCHAR(32) PRIMARY KEY,
    order_name  VARCHAR(32) NULL,
    FOREIGN KEY (order_name) REFERENCES `Order`(order_name) ON DELETE SET NULL
);

CREATE TABLE Genus(
	genus_name VARCHAR(32) PRIMARY KEY,
    family_name  VARCHAR(32) NULL,
    FOREIGN KEY (family_name) REFERENCES Family(family_name) ON DELETE SET NULL
);

-- species characteristics/behaviours

CREATE TABLE DietaryPattern(
	diet_name VARCHAR(32) PRIMARY KEY
);

CREATE TABLE FeedingStrategy(
	strategy_name VARCHAR(32) PRIMARY KEY
);

-- species table

CREATE TABLE Species(
	genus_name VARCHAR(32) NOT NULL,
    specific_name VARCHAR(32) NOT NULL,
	conservation_status ENUM('extinct', 'extinct in the wild', 'critically endangered', 'endangered',
							 'vulnerable', 'near threatened', 'least concern', 'not evaluated'),
	date_described DATE ,
    common_name VARCHAR(32),
    gregarious BOOLEAN,
    diet_name VARCHAR(32),
    strategy_name VARCHAR(32),
    PRIMARY KEY (genus_name, specific_name),
    FOREIGN KEY (genus_name) REFERENCES Genus(genus_name) ON DELETE CASCADE,
    FOREIGN KEY (diet_name) REFERENCES DietaryPattern(diet_name) ON DELETE SET NULL,
    FOREIGN KEY (strategy_name) REFERENCES FeedingStrategy(strategy_name) ON DELETE SET NULL
);

-- species name insertion trigger
/*
DELIMITER $$

CREATE TRIGGER before_insert_species
BEFORE INSERT ON Species
FOR EACH ROW
BEGIN
    -- Declare variables to hold the genus and specific parts
    DECLARE inserted_genus_name VARCHAR(32);
    DECLARE inserted_specific_name VARCHAR(32);

    -- Split the species_name (e.g., "Canis lupus") into genus_name and specific_name
    SET inserted_genus_name = SUBSTRING_INDEX(NEW.species_name, ' ', 1);
    SET inserted_specific_name = SUBSTRING_INDEX(NEW.species_name, ' ', -1);

    -- Set the genus_name and specific_name for the new row
    SET NEW.genus_name = inserted_genus_name;
    SET NEW.specific_name = inserted_specific_name;

    -- Ensure the genus exists, if not, insert it
    IF NOT EXISTS (SELECT 1 FROM Genus WHERE genus_name = inserted_genus_name) THEN
        INSERT INTO Genus (genus_name) VALUES (inserted_genus_name);
    END IF;
END $$

DELIMITER ; */



-- species relationship/symbiosis tables

CREATE TABLE PredationRelation (
	relationship_type VARCHAR(32) NOT NULL,
	predator_genus_name VARCHAR(32) NOT NULL,
    predator_specific_name VARCHAR(32) NOT NULL,
    prey_genus_name VARCHAR(32) NOT NULL,
    prey_specific_name VARCHAR(32) NOT NULL,
    PRIMARY KEY (predator_genus_name, predator_specific_name, prey_genus_name, prey_specific_name),
    FOREIGN KEY (predator_genus_name, predator_specific_name) REFERENCES Species(genus_name, specific_name) ON DELETE CASCADE,
    FOREIGN KEY (prey_genus_name, prey_specific_name) REFERENCES Species(genus_name, specific_name) ON DELETE CASCADE
);

/*
CREATE TABLE ParasiticRelation (
	
);

CREATE TABLE MutualisticRelation (
	
); 

DELIMITER $$

CREATE TRIGGER before_insert_predation_relation
BEFORE INSERT ON PredationRelation
FOR EACH ROW
BEGIN
    -- Ensure the predator and prey species are not from the same genus
    IF NEW.predator_genus_name = NEW.prey_genus_name THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Predator cannot be from the same genus as the prey species';
    END IF;

    -- Optionally, check that both predator and prey species exist in the Species table
    IF NOT EXISTS (SELECT 1 FROM Species WHERE genus_name = NEW.predator_genus_name AND specific_name = NEW.predator_specific_name) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Predator species does not exist in the Species table';
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM Species WHERE genus_name = NEW.prey_genus_name AND specific_name = NEW.prey_specific_name) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Prey species does not exist in the Species table';
    END IF;
END $$

DELIMITER ;
*/

