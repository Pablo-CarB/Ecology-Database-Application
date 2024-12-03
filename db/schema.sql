
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

CREATE TABLE Phylum(
	phylum_name VARCHAR(32) PRIMARY KEY,
    kingdom_name  VARCHAR(32) NULL,
    FOREIGN KEY (kingdom_name) REFERENCES Kingdom(kingdom_name) ON DELETE SET NULL
);

CREATE TABLE Class(
	class_name VARCHAR(32) PRIMARY KEY,
    phylum_name  VARCHAR(32) NULL,
    FOREIGN KEY (phylum_name) REFERENCES Phylum(phylum_name) ON DELETE SET NULL
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
	year_described INT , -- year was used because many organisms were first described before 1901
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

DELIMITER $$

CREATE TRIGGER before_insert_species
BEFORE INSERT ON Species
FOR EACH ROW
BEGIN
    -- Ensure the genus exists, if not, insert it
    IF NOT EXISTS (SELECT 1 FROM Genus WHERE genus_name = NEW.genus_name) THEN
        INSERT INTO Genus (genus_name) VALUES (NEW.genus_name);
    END IF;
END $$

DELIMITER ; 

-- species insertion helper procedure
DELIMITER $$

CREATE PROCEDURE InsertTaxonomicHistory(
    IN p_domain_name VARCHAR(32),
    IN p_kingdom_name VARCHAR(32),
    IN p_phylum_name VARCHAR(32),
    IN p_class_name VARCHAR(32),
    IN p_order_name VARCHAR(32),
    IN p_family_name VARCHAR(32),
    IN p_genus_name VARCHAR(32),
    IN p_species_specific_name VARCHAR(32),
    IN p_conservation_status ENUM('extinct', 'extinct in the wild', 'critically endangered', 'endangered',
                                  'vulnerable', 'near threatened', 'least concern', 'not evaluated'),
    IN p_year_described YEAR,
    IN p_common_name VARCHAR(32),
    IN p_gregarious BOOLEAN,
    IN p_diet_name VARCHAR(32),
    IN p_strategy_name VARCHAR(32)
)
BEGIN
    -- Insert Domain if it does not exist and if it's provided
    IF p_domain_name IS NOT NULL AND NOT EXISTS (SELECT 1 FROM Domain WHERE domain_name = p_domain_name) THEN
        INSERT INTO Domain(domain_name) VALUES (p_domain_name);
    END IF;

    -- Insert Kingdom if it does not exist and if it's provided
    IF p_kingdom_name IS NOT NULL AND NOT EXISTS (SELECT 1 FROM Kingdom WHERE kingdom_name = p_kingdom_name) THEN
        INSERT INTO Kingdom(kingdom_name, domain_name) 
        VALUES (p_kingdom_name, IFNULL(p_domain_name, (SELECT domain_name FROM Kingdom WHERE kingdom_name = p_kingdom_name)));
    END IF;

    -- Insert Phylum if it does not exist and if it's provided
    IF p_phylum_name IS NOT NULL AND NOT EXISTS (SELECT 1 FROM Phylum WHERE phylum_name = p_phylum_name) THEN
        INSERT INTO Phylum(phylum_name, kingdom_name) 
        VALUES (p_phylum_name, IFNULL(p_kingdom_name, (SELECT kingdom_name FROM Phylum WHERE phylum_name = p_phylum_name)));
    END IF;

    -- Insert Class if it does not exist and if it's provided
    IF p_class_name IS NOT NULL AND NOT EXISTS (SELECT 1 FROM Class WHERE class_name = p_class_name) THEN
        INSERT INTO Class(class_name, phylum_name) 
        VALUES (p_class_name, IFNULL(p_phylum_name, (SELECT phylum_name FROM Class WHERE class_name = p_class_name)));
    END IF;

    -- Insert Order if it does not exist and if it's provided
    IF p_order_name IS NOT NULL AND NOT EXISTS (SELECT 1 FROM `Order` WHERE order_name = p_order_name) THEN
        INSERT INTO `Order`(order_name, class_name) 
        VALUES (p_order_name, IFNULL(p_class_name, (SELECT class_name FROM `Order` WHERE order_name = p_order_name)));
    END IF;

    -- Insert Family if it does not exist and if it's provided
    IF p_family_name IS NOT NULL AND NOT EXISTS (SELECT 1 FROM Family WHERE family_name = p_family_name) THEN
        INSERT INTO Family(family_name, order_name) 
        VALUES (p_family_name, IFNULL(p_order_name, (SELECT order_name FROM Family WHERE family_name = p_family_name)));
    END IF;

    -- Insert Genus if it does not exist and if it's provided
    IF p_genus_name IS NOT NULL AND NOT EXISTS (SELECT 1 FROM Genus WHERE genus_name = p_genus_name) THEN
        INSERT INTO Genus(genus_name, family_name) 
        VALUES (p_genus_name, IFNULL(p_family_name, (SELECT family_name FROM Genus WHERE genus_name = p_genus_name)));
    END IF;

    -- Insert Species
    INSERT INTO Species(
        genus_name, specific_name, conservation_status, year_described, common_name, gregarious, diet_name, strategy_name
    ) 
    VALUES (
        p_genus_name, p_species_specific_name, p_conservation_status, p_year_described, p_common_name, p_gregarious, p_diet_name, p_strategy_name
    );

END $$

DELIMITER ;




-- species relationship/symbiosis tables

CREATE TABLE Predation (
    predator_genus_name VARCHAR(32),
    predator_specific_name VARCHAR(32),
    prey_genus_name VARCHAR(32),
    prey_specific_name VARCHAR(32),
    PRIMARY KEY (predator_genus_name, predator_specific_name, prey_genus_name, prey_specific_name),
    FOREIGN KEY (predator_genus_name, predator_specific_name) REFERENCES Species (genus_name, specific_name) ON DELETE CASCADE,
    FOREIGN KEY (prey_genus_name, prey_specific_name) REFERENCES Species (genus_name, specific_name) ON DELETE CASCADE
);

CREATE TABLE Mutualism (
    genus_name1 VARCHAR(32),
    specific_name1 VARCHAR(32),
    genus_name2 VARCHAR(32),
    specific_name2 VARCHAR(32),
    PRIMARY KEY (genus_name1, specific_name1, genus_name2, specific_name2),
    FOREIGN KEY (genus_name1, specific_name1) REFERENCES Species (genus_name, specific_name) ON DELETE CASCADE,
    FOREIGN KEY (genus_name2, specific_name2) REFERENCES Species (genus_name, specific_name) ON DELETE CASCADE
);

CREATE TABLE Parasitism (
    parasite_genus_name VARCHAR(32),
    parasite_specific_name VARCHAR(32),
    host_genus_name VARCHAR(32),
    host_specific_name VARCHAR(32),
    PRIMARY KEY (parasite_genus_name, parasite_specific_name, host_genus_name, host_specific_name),
    FOREIGN KEY (parasite_genus_name, parasite_specific_name) REFERENCES Species (genus_name,specific_name) ON DELETE CASCADE,
    FOREIGN KEY (host_genus_name, host_specific_name) REFERENCES Species (genus_name, specific_name) ON DELETE CASCADE
);

/*
BIOME x REGION dR Species ./vars edits
*/

-- Biome table
CREATE TABLE Biome (
    biome_name VARCHAR(32) PRIMARY KEY
);

-- Region table (linked to Biome)
CREATE TABLE Region (
    region_name VARCHAR(32),
    longitude FLOAT,
    latitude FLOAT,
    biome_name VARCHAR(32) NOT NULL,
    PRIMARY KEY (longitude, latitude),
    FOREIGN KEY (biome_name) REFERENCES Biome(biome_name) ON DELETE CASCADE
);

-- Link species to biome and region
CREATE TABLE SpeciesRegionBiome (
    genus_name VARCHAR(32),
    specific_name VARCHAR(32),
    longitude FLOAT,
    latitude FLOAT,
    biome_name VARCHAR(32),
    PRIMARY KEY (genus_name, specific_name, longitude, latitude),
    FOREIGN KEY (genus_name, specific_name) REFERENCES Species(genus_name, specific_name) ON DELETE CASCADE,
    FOREIGN KEY (longitude, latitude) REFERENCES Region(longitude, latitude) ON DELETE CASCADE,
    FOREIGN KEY (biome_name) REFERENCES Biome(biome_name) ON DELETE CASCADE
);

-- Procedure to add region and link it to a biome
DELIMITER $$

CREATE PROCEDURE InsertRegion(
    IN p_region_name VARCHAR(32),
    IN p_biome_name VARCHAR(32),
    IN p_longitude FLOAT,
    IN p_latitude FLOAT
)
BEGIN
    -- Ensure the Biome exists
    IF NOT EXISTS (SELECT 1 FROM Biome WHERE biome_name = p_biome_name) THEN
        INSERT INTO Biome (biome_name) VALUES (p_biome_name);
    END IF;

    -- Insert Region if it does not exist
    IF NOT EXISTS (SELECT 1 FROM Region WHERE longitude = p_longitude AND latitude = p_latitude) THEN
        INSERT INTO Region (region_name, longitude, latitude, biome_name) 
        VALUES (p_region_name, p_longitude, p_latitude, p_biome_name);
    END IF;
END $$

DELIMITER ;

-- Procedure to link species to a region and biome
DELIMITER $$

CREATE PROCEDURE LinkSpeciesToRegionBiome(
    IN p_genus_name VARCHAR(32),
    IN p_specific_name VARCHAR(32),
    IN p_longitude FLOAT,
    IN p_latitude FLOAT,
    IN p_biome_name VARCHAR(32)
)
BEGIN
    -- Ensure the Species exists
    IF NOT EXISTS (SELECT 1 FROM Species WHERE genus_name = p_genus_name AND specific_name = p_specific_name) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Species does not exist';
    END IF;

    -- Ensure the Region exists
    IF NOT EXISTS (SELECT 1 FROM Region WHERE longitude = p_longitude AND latitude = p_latitude AND biome_name = p_biome_name) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Region does not exist';
    END IF;

    -- Link Species to Region and Biome
    INSERT INTO SpeciesRegionBiome (genus_name, specific_name, longitude, latitude, biome_name)
    VALUES (p_genus_name, p_specific_name, p_longitude, p_latitude, p_biome_name);
END $$

DELIMITER ;

/* 
End region/biome edits
*/


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

