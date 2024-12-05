USE ecosystem_db;

-- Insert Domains 
INSERT IGNORE INTO Domain(domain_name) VALUES ('Eukarya'), ('Bacteria'), ('Archaea');

-- Insert Kingdoms 
INSERT IGNORE INTO Kingdom(kingdom_name, domain_name) VALUES 
('Animalia', 'Eukarya'), ('Plantae', 'Eukarya'), ('Fungi', 'Eukarya');

-- Insert Phylums 
INSERT IGNORE INTO Phylum(phylum_name, kingdom_name) VALUES 
('Chordata', 'Animalia'), ('Magnoliophyta', 'Plantae');

-- Insert  Genera 
INSERT IGNORE INTO Genus(genus_name, phylum_name) VALUES 
('Panthera', 'Chordata'),
('Lucilia', 'Chordata'), 
('Giraffa', 'Chordata'),
('Quercus', 'Magnoliophyta'), 
('Canis', 'Chordata'),
('Homo', 'Chordata'),
('Equus', 'Chordata'),
('Bubalus', 'Chordata'),
('Bos', 'Chordata'),
('Elephas', 'Chordata'),
('Cervus', 'Chordata'),
('Antilocapra', 'Chordata'),
('Crocuta', 'Chordata'),
('Felis', 'Chordata'),
('Vipera', 'Chordata'),
('Aquila', 'Chordata'),
('Gavialis', 'Chordata'),
('Passer', 'Chordata'),
('Ursus', 'Chordata'),
('Sus', 'Chordata'),
('Corvus', 'Chordata'),
('Pica', 'Chordata'),
('Turdus', 'Chordata'),
('Chelonia', 'Chordata'),
('Varanus', 'Chordata'),
('Naja', 'Chordata'),
('Chamaeleo', 'Chordata'),
('Accipiter', 'Chordata'),
('Sturnus', 'Chordata'),
('Delphinus', 'Chordata'),
('Phocoena', 'Chordata'),
('Orcinus', 'Chordata'),
('Balaenoptera', 'Chordata'),
('Rana', 'Chordata'),
('Bombina', 'Chordata'),
('Hyla', 'Chordata'),
('Bombus', 'Chordata'),
('Apis', 'Chordata'),
('Coleoptera', 'Chordata'),
('Cimex', 'Chordata'),
('Drosophila', 'Chordata'),
('Gryllus', 'Chordata'),
('Malus', 'Magnoliophyta'),
('Triticum', 'Magnoliophyta'),
('Zea', 'Magnoliophyta'),
('Cucumis', 'Magnoliophyta'),
('Solanum', 'Magnoliophyta'),
('Carya', 'Magnoliophyta'),
('Ficus', 'Magnoliophyta'),
('Pinus', 'Magnoliophyta');

INSERT INTO Diet(diet_name) VALUES 
("carnivore"),("herbivore"),("omnivore"),("insectivore"),
("piscivore"),("fungivore"),("bacterivore"),("algivore"),
("scavenger"),("nectivores"),("saprotroph"),("parasite"),
('photoautotroph');

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

-- Insert Data into Species Table 
INSERT IGNORE INTO Species (genus_name, specific_name, conservation_status, year_described, common_name, gregarious, diet_name, strategy_name) 
VALUES 
-- Plants (Autotrophs and Photoautotrophs)
('Malus', 'domestica', 'least concern', 1758, 'Apple', TRUE, 'photoautotroph', 'grazer'),
('Quercus', 'robur', 'least concern', 1758, 'English Oak', TRUE, 'photoautotroph', 'n/a'),
('Pinus', 'sylvestris', 'least concern', 1758, 'Scots Pine', TRUE, 'photoautotroph', 'n/a'),
('Cucumis', 'sativus', 'least concern', 1758, 'Cucumber', TRUE, 'photoautotroph', 'n/a'),
('Solanum', 'lycopersicum', 'least concern', 1758, 'Tomato', TRUE, 'photoautotroph', 'n/a'),
('Zea', 'mays', 'least concern', 1758, 'Maize (Corn)', TRUE, 'photoautotroph', 'n/a'),
('Triticum', 'aestivum', 'least concern', 1758, 'Wheat', TRUE, 'photoautotroph', 'n/a'),
('Carya', 'illinoinensis', 'least concern', 1758, 'Pecan Tree', TRUE, 'photoautotroph', 'n/a'),
('Ficus', 'carica', 'least concern', 1758, 'Fig Tree', TRUE, 'photoautotroph', 'n/a'),

-- Herbivores
('Equus', 'ferus', 'least concern', 1758, 'Horse', TRUE, 'herbivore', 'grazer'),
('Bubalus', 'bubalis', 'least concern', 1758, 'Water Buffalo', TRUE, 'herbivore', 'grazer'),
('Elephas', 'maximus', 'endangered', 1758, 'Asian Elephant', TRUE, 'herbivore', 'n/a'),
('Cervus', 'elaphus', 'least concern', 1758, 'Red Deer', TRUE, 'herbivore', 'grazer'),
('Giraffa', 'camelopardalis', 'vulnerable', 1758, 'Giraffe', TRUE, 'herbivore', 'n/a'),
('Bos', 'taurus', 'least concern', 1758, 'Cow', TRUE, 'herbivore', 'grazer'),
('Antilocapra', 'americana', 'least concern', 1758, 'Pronghorn Antelope', TRUE, 'herbivore', 'grazer'),
('Ceratotherium', 'simum', 'vulnerable', 1758, 'White Rhinoceros', TRUE, 'herbivore', 'grazer'),
('Rhinoceros', 'unicornis', 'vulnerable', 1758, 'Indian Rhinoceros', TRUE, 'herbivore', 'grazer'),

-- Carnivores
('Panthera', 'leo', 'vulnerable', 1758, 'Lion', FALSE, 'carnivore', 'predator'),
('Panthera', 'tigris', 'endangered', 1758, 'Tiger', FALSE, 'carnivore', 'predator'),
('Orcinus', 'orca', 'least concern', 1758, 'Killer Whale', TRUE, 'carnivore', 'hunter'),
('Phocoena', 'phocoena', 'least concern', 1758, 'Harbor Porpoise', TRUE, 'carnivore', 'hunter'),
('Delphinus', 'delphis', 'least concern', 1758, 'Common Dolphin', TRUE, 'carnivore', 'hunter'),
('Balaenoptera', 'musculus', 'least concern', 1758, 'Blue Whale', TRUE, 'carnivore', 'filter feeder'),

-- Omnivores
('Passer', 'domesticus', 'least concern', 1758, 'House Sparrow', TRUE, 'omnivore', 'grazer'),
('Homo', 'sapiens', 'least concern', 1758, 'Human', TRUE, 'omnivore', 'hunter'),
('Vervet', 'monkey', 'least concern', 1758, 'Vervet Monkey', TRUE, 'omnivore', 'n/a'),
('Macaca', 'fascicularis', 'least concern', 1758, 'Crab-eating Macaque', TRUE, 'omnivore', 'n/a'),

-- Parasites
('Ctenocephalides', 'felis', 'least concern', 1758, 'Flea', TRUE, 'parasite', 'n/a'),
('Taenia', 'solium', 'least concern', 1758, 'Tapeworm', FALSE, 'parasite', 'n/a'),
('Cimex', 'lectularius', 'least concern', 1758, 'Bedbug', TRUE, 'parasite', 'n/a'),

-- Insects
('Bombus', 'terrestris', 'least concern', 1758, 'Bumblebee', TRUE, 'omnivore', 'pollinator'),
('Apis', 'mellifera', 'least concern', 1758, 'Honeybee', TRUE, 'omnivore', 'pollinator'),
('Coleoptera', 'cucujidae', 'least concern', 1758, 'Cucujid Beetle', TRUE, 'herbivore', 'n/a'),
('Drosophila', 'melanogaster', 'least concern', 1758, 'Fruit Fly', TRUE, 'herbivore', 'n/a'),
('Tachina', 'fera', 'least concern', 1758, 'Tachina Fly', TRUE, 'parasite', 'n/a'),
('Gryllus', 'campestris', 'least concern', 1758, 'Field Cricket', TRUE, 'omnivore', 'n/a'),
('Lucilia', 'sericata', 'least concern', 1758, 'Green Bottle Fly', TRUE, 'saprotroph', 'n/a');

-- Insert Regions 
INSERT IGNORE INTO Region(region_name, latitude, longitude) VALUES
('Amazon Rainforest', -3.4653, -62.2159),
('Sahara Desert', 23.4162, 25.6628);

-- Insert Biomes 
INSERT INTO Biome(biome_name, region_id) VALUES
('Rainforest', 1),
('Desert', 2)
ON DUPLICATE KEY UPDATE region_id = VALUES(region_id);

-- Insert Species_Biome 
INSERT INTO Species_Biome (genus_name, specific_name, biome_name) VALUES
('Lucilia', 'sericata', 'Rainforest'),
('Giraffa', 'camelopardalis', 'Desert'),
('Panthera', 'tigris', 'Rainforest'),
('Quercus', 'robur', 'Desert')
ON DUPLICATE KEY UPDATE biome_name = VALUES(biome_name);

