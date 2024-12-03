
/*
TODO
- create viable example data
- create a separate test file
*/

USE `ecosystem_db`;

-- Insert Domains
INSERT INTO Domain(domain_name)
VALUES ("eukarya"),("bacteria"),("archaea");

-- Insert Kingdoms
INSERT INTO Kingdom(kingdom_name,domain_name)
VALUES ("Animalia","eukarya"),("plantae","eukarya"),("protista","eukarya"),("fungi","eukarya");


-- Insert Phylums
INSERT INTO Phylum (phylum_name, kingdom_name) VALUES ('Angiosperms', 'Plantae');
INSERT INTO Phylum (phylum_name, kingdom_name) VALUES ('Chordata', 'Animalia');

-- Insert Classes
INSERT INTO Class (class_name, phylum_name) VALUES ('Dicotyledons', 'Angiosperms'),('Mammalia', 'Chordata'),('Aves', 'Chordata'),('Reptilia', 'Chordata');

-- Insert Orders
INSERT INTO `Order` (order_name, class_name) VALUES ('Rosales', 'Dicotyledons'), ('Carnivora', 'Mammalia'), ('Passeriformes', 'Aves'), ('Squamata', 'Reptilia');

-- Insert Families
INSERT INTO Family (family_name, order_name) VALUES ('Rosaceae', 'Rosales'),('Felidae', 'Carnivora'),('Fringillidae', 'Passeriformes'),('Viperidae', 'Squamata');

-- Insert Genera
INSERT INTO Genus (genus_name, family_name) VALUES ('Malus', 'Rosaceae');  
INSERT INTO Genus (genus_name, family_name) VALUES ('Panthera', 'Felidae'); 
INSERT INTO Genus (genus_name, family_name) VALUES ('Passer', 'Fringillidae'); 
INSERT INTO Genus (genus_name, family_name) VALUES ('Vipera', 'Viperidae'); 

-- Insert Genera
INSERT INTO Genus (genus_name, family_name) VALUES 
('Malus', 'Rosaceae'),  
('Panthera', 'Felidae'), 
('Passer', 'Fringillidae'), 
('Vipera', 'Viperidae'); 

/*
BEGIN biome and region edits
*/

-- Insert Biomes (NASA's Seven Biomes)
INSERT INTO Biome(biome_name)
VALUES 
    ('Tundra'),
    ('Coniferous Forest'),
    ('Temperate Deciduous Forest'),
    ('Rainforest'),
    ('Grassland'),
    ('Shrubland'),
    ('Desert');

-- Insert Regions (linked to Biomes)
INSERT INTO Region(region_name, longitude, latitude, biome_name)
VALUES
    ('Arctic Tundra', 135.0, 71.0, 'Tundra'), -- Arctic region near Russia 
    ('East Siberian Taiga', 135.0, 60.0, 'Coniferous Forest'), -- Eastern Russia taiga region
    ('Appalachian Mountains', -81.0, 37.0, 'Temperate Deciduous Forest'), -- Appalachians in Eastern USA
    ('Amazon Rainforest', -63.0, -3.0, 'Rainforest'), -- Central Amazon, Brazil
    ('Great Plains', -100.0, 39.0, 'Grassland'), -- Central USA plains
    ('Mediterranean Shrubland', 10.0, 38.0, 'Shrubland'), -- Mediterranean region near Italy/Spain
    ('Sahara Desert', 13.0, 40.0, 'Desert'); -- Sahara near Niger/Chad border

-- Link Species to Biomes and Regions
INSERT INTO SpeciesRegionBiome(genus_name, specific_name, longitude, latitude, biome_name)
VALUES
    -- Plants
    ('Malus', 'domestica', -81.0, 37.0, 'Temperate Deciduous Forest'), -- Apple in Appalachian Mountains
    ('Pinus', 'sylvestris', 135.0, 60.0, 'Coniferous Forest'), -- Scots Pine in East Siberian Taiga

    -- Herbivores
    ('Cervus', 'elaphus', -81.0, 37.0, 'Temperate Deciduous Forest'), -- Red Deer in East Siberian Taiga
    ('Bos', 'taurus', -100.0, 39.0, 'Grassland'), -- Cow in Great Plains

    -- Carnivores
    ('Panthera', 'leo', 13.0, 40.0, 'Desert'), -- Lion in Sahara Desert
    ('Vipera', 'berus', 135.0, 60.0, 'Coniferous Forest'), -- European Adder in East Siberian Taiga

    -- Omnivores
    ('Homo', 'sapiens', 135.0, 71.0, 'Tundra'), -- Humans in Artic Tundra
    ('Passer', 'domesticus', -100.0, 39.0, 'Grassland'), -- House Sparrow in Great Plains

    -- Insects
    ('Bombus', 'terrestris', 10.0, 38.0, 'Shrubland'), -- Bumblebee in Mediterranean Shrubland
    ('Lucilia', 'sericata', -63.0, -3.0, 'Rainforest'); -- Green Bottle Fly in Amazong Rainforest

/*
END biome and region edits
*/

INSERT INTO DietaryPattern(diet_name)
VALUES ("carnivore"),("herbivore"),("omnivore"),("insectivore"),("piscivore"),("fungivore"),("bacterivore"),("algivore"),("scavenger"),("nectivores"),("saprotroph"),("parasite"),('photoautotroph');

INSERT INTO FeedingStrategy (strategy_name)
VALUES 
  ('ambush'),
  ('opportunistic'),
  ('symbiotic feeding'),
  ('parasitism'),
  ('eusocial'),
  ('grazer'),
  ('n/a'),
  ('predator'),
  ('scavenger'),
  ('hunter'),
  ('forager'),
  ('filter feeder'),
  ('pollinator'),
  ('pack hunter');

-- species

INSERT INTO Species (genus_name, specific_name, conservation_status, year_described, common_name, gregarious, diet_name, strategy_name) 
VALUES 
-- Plants (Autotrophs and Photoautotrophs)
('Malus', 'domestica', 'least concern', 1758, 'Apple', TRUE, 'photoautotroph', 'grazer'),
('Quercus', 'robur', 'least concern', 1758, 'English Oak', TRUE, 'photoautotroph', 'n/a'),
('Pinus', 'sylvestris', 'least concern', 1758, 'Scots Pine', TRUE, 'photoautotroph', 'n/a'),
('Cucumis', 'sativus', 'least concern', 1758, 'Cucumber', TRUE, 'photoautotroph', 'n/a'),
('Solanum', 'lycopersicum', 'least concern', 1758, 'Tomato', TRUE, 'photoautotroph', 'n/a'),
('Zea', 'mays', 'least concern', 1758, 'Maize (Corn)', TRUE, 'photoautotroph', 'n/a'),  -- Corn
('Triticum', 'aestivum', 'least concern', 1758, 'Wheat', TRUE, 'photoautotroph', 'n/a'),  -- Wheat
('Carya', 'illinoinensis', 'least concern', 1758, 'Pecan Tree', TRUE, 'photoautotroph', 'n/a'), -- Pecan Tree
('Ficus', 'carica', 'least concern', 1758, 'Fig Tree', TRUE, 'photoautotroph', 'n/a'), -- Fig

-- Herbivores
('Equus', 'ferus', 'least concern', 1758, 'Horse', TRUE, 'herbivore', 'grazer'),
('Bubalus', 'bubalis', 'least concern', 1758, 'Water Buffalo', TRUE, 'herbivore', 'grazer'),
('Elephas', 'maximus', 'endangered', 1758, 'Asian Elephant', TRUE, 'herbivore', 'n/a'),
('Cervus', 'elaphus', 'least concern', 1758, 'Red Deer', TRUE, 'herbivore', 'grazer'),
('Giraffa', 'camelopardalis', 'vulnerable', 1758, 'Giraffe', TRUE, 'herbivore', 'n/a'),
('Bos', 'taurus', 'least concern', 1758, 'Cow', TRUE, 'herbivore', 'grazer'),
('Antilocapra', 'americana', 'least concern', 1758, 'Pronghorn Antelope', TRUE, 'herbivore', 'grazer'),
('Crocuta', 'crocuta', 'least concern', 1758, 'Spotted Hyena', TRUE, 'omnivore', 'scavenger'),

-- Carnivores
('Panthera', 'leo', 'vulnerable', 1758, 'Lion', FALSE, 'carnivore', 'predator'),
('Panthera', 'tigris', 'endangered', 1758, 'Tiger', FALSE, 'carnivore', 'predator'),
('Vulpes', 'vulpes', 'least concern', 1758, 'Red Fox', TRUE, 'carnivore', 'hunter'),
('Canis', 'lupus', 'least concern', 1758, 'Wolf', TRUE, 'carnivore', 'pack hunter'),
('Aquila', 'chrysaetos', 'least concern', 1758, 'Golden Eagle', FALSE, 'carnivore', 'predator'),
('Vipera', 'berus', 'least concern', 1758, 'European Adder', FALSE, 'carnivore', 'predator'),
('Gavialis', 'gangeticus', 'critically endangered', 1758, 'Gharial', FALSE, 'carnivore', 'hunter'),  -- Gharial
('Dendroaspis', 'polylepis', 'least concern', 1758, 'Black Mamba', FALSE, 'carnivore', 'predator'),  -- Black Mamba
('Felis', 'catus','least concern',1758,'house cat',FALSE,'carnivore','predator'),

-- Omnivores
('Passer', 'domesticus', 'least concern', 1758, 'House Sparrow', TRUE, 'omnivore', 'grazer'),
('Homo', 'sapiens', 'least concern', 1758, 'Human', TRUE, 'omnivore', 'hunter'),
('Ursus', 'arctos', 'least concern', 1758, 'Brown Bear', TRUE, 'omnivore', 'forager'),
('Sus', 'scrofa', 'least concern', 1758, 'Wild Boar', TRUE, 'omnivore', 'forager'),
('Corvus', 'corax', 'least concern', 1758, 'Common Raven', TRUE, 'omnivore', 'scavenger'),
('Pica', 'pica', 'least concern', 1758, 'Magpie', TRUE, 'omnivore', 'forager'),
('Turdus', 'merula', 'least concern', 1758, 'European Blackbird', TRUE, 'omnivore', 'forager'),

-- Parasites
('Ctenocephalides', 'felis', 'least concern', 1758, 'Flea', TRUE, 'parasite', 'n/a'),
('Taenia', 'solium', 'least concern', 1758, 'Tapeworm', FALSE, 'parasite', 'n/a'),
('Pediculus', 'humanus', 'least concern', 1758, 'Lice', TRUE, 'parasite', 'n/a'),
('Ascaris', 'lumbricoides', 'least concern', 1758, 'Giant Roundworm', FALSE, 'parasite', 'n/a'),  -- Roundworm
('Trypanosoma', 'brucei', 'least concern', 1758, 'African Trypanosome', FALSE, 'parasite', 'n/a'),  -- African sleeping sickness

-- Mutualists (Autotrophs and Symbionts)
('Glomus', 'intraradices', 'least concern', 1758, 'Mycorrhizal Fungi', TRUE, 'saprotroph', 'n/a'),
('Acacia', 'spp.', 'least concern', 1758, 'Acacia Tree', TRUE, 'herbivore', 'n/a'),
('Pseudomyrmex', 'spp.', 'least concern', 1758, 'Ant', TRUE, 'omnivore', 'scavenger'),
('Labroides', 'dimidiatus', 'least concern', 1758, 'Cleaner Wrasse', TRUE, 'carnivore', 'n/a'),
('Carcharhinus', 'spp.', 'least concern', 1758, 'Shark', TRUE, 'carnivore', 'hunter'),
('Lysmata', 'amboinensis', 'least concern', 1758, 'Cleaner Shrimp', TRUE, 'omnivore', 'n/a'), -- Cleaner Shrimp (Mutualistic with fish)

-- Additional Species (for a broader food web)
-- Amphibians
('Rana', 'temporaria', 'least concern', 1758, 'European Brown Frog', TRUE, 'insectivore', 'hunter'),
('Hyla', 'arborea', 'least concern', 1758, 'European Tree Frog', TRUE, 'insectivore', 'hunter'),
('Bombina', 'orientalis', 'least concern', 1758, 'Oriental Fire-bellied Toad', TRUE, 'insectivore', 'n/a'),

-- Reptiles
('Chelonia', 'mydas', 'endangered', 1758, 'Green Sea Turtle', TRUE, 'herbivore', 'grazer'),
('Varanus', 'komodoensis', 'vulnerable', 1758, 'Komodo Dragon', FALSE, 'carnivore', 'predator'),
('Naja', 'naja', 'least concern', 1758, 'Indian Cobra', FALSE, 'carnivore', 'predator'),
('Chamaeleo', 'chamaeleon', 'least concern', 1758, 'Common Chameleon', TRUE, 'insectivore', 'hunter'),

-- Birds
('Aquila', 'nipalensis', 'least concern', 1758, 'Steppe Eagle', FALSE, 'carnivore', 'hunter'),
('Sturnus', 'vulgaris', 'least concern', 1758, 'European Starling', TRUE, 'omnivore', 'scavenger'),
('Accipiter', 'nisus', 'least concern', 1758, 'Eurasian Sparrowhawk', FALSE, 'carnivore', 'hunter'),
('Turdus', 'philomelos', 'least concern', 1758, 'Song Thrush', TRUE, 'omnivore', 'n/a'),

-- Insects
('Bombus', 'terrestris', 'least concern', 1758, 'Bumblebee', TRUE, 'omnivore', 'pollinator'),
('Apis', 'mellifera', 'least concern', 1758, 'Honeybee', TRUE, 'omnivore', 'pollinator'),
('Coleoptera', 'cucujidae', 'least concern', 1758, 'Cucujid Beetle', TRUE, 'herbivore', 'n/a'),
('Cimex', 'lectularius', 'least concern', 1758, 'Bedbug', TRUE, 'parasite', 'n/a'),
('Drosophila', 'melanogaster', 'least concern', 1758, 'Fruit Fly', TRUE, 'herbivore', 'n/a'),
('Tachina', 'fera', 'least concern', 1758, 'Tachina Fly', TRUE, 'parasite', 'n/a'),
('Gryllus', 'campestris', 'least concern', 1758, 'Field Cricket', TRUE, 'omnivore', 'n/a'),
('Lucilia', 'sericata', 'least concern', 1758, 'Green Bottle Fly', TRUE, 'saprotroph', 'n/a'),  -- Green Bottle Fly (feed on decaying matter);

('Ceratotherium', 'simum', 'vulnerable', 1758, 'White Rhinoceros', TRUE, 'herbivore', 'grazer'),
('Rhinoceros', 'unicornis', 'vulnerable', 1758, 'Indian Rhinoceros', TRUE, 'herbivore', 'grazer'),
('Vervet', 'monkey', 'least concern', 1758, 'Vervet Monkey', TRUE, 'omnivore', 'n/a'),
('Macaca', 'fascicularis', 'least concern', 1758, 'Crab-eating Macaque', TRUE, 'omnivore', 'n/a'),
('Balaenoptera', 'musculus', 'least concern', 1758, 'Blue Whale', TRUE, 'carnivore', 'filter feeder'), -- Blue Whale
('Orcinus', 'orca', 'least concern', 1758, 'Killer Whale', TRUE, 'carnivore', 'hunter'),
('Phocoena', 'phocoena', 'least concern', 1758, 'Harbor Porpoise', TRUE, 'carnivore', 'hunter'),
('Delphinus', 'delphis', 'least concern', 1758, 'Common Dolphin', TRUE, 'carnivore', 'hunter');


-- Predation
INSERT INTO Predation (predator_genus_name, predator_specific_name, prey_genus_name, prey_specific_name) 
VALUES
('Panthera', 'leo', 'Cervus', 'elaphus'),  -- Lion preys on Red Deer
('Panthera', 'leo', 'Bubalus', 'bubalis'),  -- Lion preys on Water Buffalo
('Panthera', 'tigris', 'Cervus', 'elaphus'),  -- Tiger preys on Red Deer
('Canis', 'lupus', 'Cervus', 'elaphus'),  -- Wolf preys on Red Deer
('Canis', 'lupus', 'Bubalus', 'bubalis'),  -- Wolf preys on Water Buffalo
('Aquila', 'chrysaetos', 'Cervus', 'elaphus'),  -- Golden Eagle preys on Red Deer
('Vulpes', 'vulpes', 'Rana', 'temporaria'),  -- Red Fox preys on European Brown Frog
('Vipera', 'berus', 'Rana', 'temporaria'),  -- European Adder preys on European Brown Frog
('Varanus', 'komodoensis', 'Cervus', 'elaphus'),  -- Komodo Dragon preys on Red Deer
('Varanus', 'komodoensis', 'Canis', 'lupus');  -- Komodo Dragon preys on Wolf

-- Mutualism
INSERT INTO Mutualism (genus_name1, specific_name1, genus_name2, specific_name2)
VALUES
('Acacia', 'spp.', 'Pseudomyrmex', 'spp.'),  -- Acacia Tree provides shelter to ants, ants protect acacia from herbivores
('Labroides', 'dimidiatus', 'Carcharhinus', 'spp.'),  -- Cleaner Wrasse and Shark
('Lysmata', 'amboinensis', 'Carcharhinus', 'spp.'),  -- Cleaner Shrimp and Shark
('Glomus', 'intraradices', 'Quercus', 'robur'),  -- Mycorrhizal fungi and Oak Tree (symbiotic relationship)
('Glomus', 'intraradices', 'Pinus', 'sylvestris'),  -- Mycorrhizal fungi and Scots Pine (symbiotic relationship)
('Apis', 'mellifera', 'Solanum', 'lycopersicum'),  -- Honeybees pollinate Tomato plants
('Bombus', 'terrestris', 'Solanum', 'lycopersicum'),  -- Bumblebees pollinate Tomato plants
('Apis', 'mellifera', 'Malus', 'domestica'),  -- Honeybees pollinate Apple trees
('Bombus', 'terrestris', 'Malus', 'domestica'),  -- Bumblebees pollinate Apple trees
('Pseudomyrmex', 'spp.', 'Acacia', 'spp.');  -- Ants protect Acacia Tree from herbivores

-- Parasitism
INSERT INTO Parasitism (parasite_genus_name, parasite_specific_name, host_genus_name, host_specific_name)
VALUES
('Ctenocephalides', 'felis', 'Canis', 'lupus'),  -- Fleas parasitize wolves
('Ctenocephalides', 'felis', 'Homo', 'sapiens'),  -- Fleas parasitize humans
('Taenia', 'solium', 'Homo', 'sapiens'),  -- Tapeworm parasitizes humans
('Pediculus', 'humanus', 'Homo', 'sapiens'),  -- Lice parasitize humans
('Ascaris', 'lumbricoides', 'Homo', 'sapiens'),  -- Roundworm parasitizes humans
('Trypanosoma', 'brucei', 'Homo', 'sapiens'),  -- African Trypanosome parasitizes humans
('Trypanosoma', 'brucei', 'Bos', 'taurus'),  -- African Trypanosome parasitizes cattle
('Pediculus', 'humanus', 'Sus', 'scrofa'),  -- Lice parasitize wild boars
('Taenia', 'solium', 'Sus', 'scrofa'),  -- Tapeworm parasitizes wild boars
('Ctenocephalides', 'felis', 'Felis', 'catus');  -- Fleas parasitize domestic cats
