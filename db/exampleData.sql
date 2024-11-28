
/*
TODO
- create viable example data
- create a separate test file
*/

USE `ecosystem_db`;

INSERT INTO DietaryPattern(diet_name)
VALUES ("carnivore");

INSERT INTO FeedingStrategy(strategy_name)
VALUES ("ambush");

-- Species Insertions

INSERT INTO Species (genus_name, specific_name, conservation_status, date_described, common_name, gregarious, diet_name, strategy_name)
VALUES ("canis", "lupus", 'least concern','1758-01-01', 'wolf', TRUE, 'carnivore', 'ambush');

INSERT INTO Species (genus_name, specific_name, conservation_status, date_described, common_name, gregarious, diet_name, strategy_name)
VALUES ("canis", "floofa", 'threatened','2020-01-01', 'lolf', FALSE, 'can', 'ava');
