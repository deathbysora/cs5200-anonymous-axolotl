# TODO fix floats being read in as integers.

LOAD DATA LOCAL INFILE "/Users/danielbi/git-repo/SP23-CS5200/data-parsing/users.csv" INTO TABLE Person
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES;

LOAD DATA LOCAL INFILE "/Users/danielbi/git-repo/SP23-CS5200/data-parsing/users.csv" INTO TABLE User
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES;

LOAD DATA LOCAL INFILE "/Users/danielbi/git-repo/SP23-CS5200/data-parsing/beerstyles.csv" INTO TABLE BeerStyle
FIELDS TERMINATED BY "," ENCLOSED BY '"'
LINES TERMINATED BY "\n"
IGNORE 1 LINES;

LOAD DATA LOCAL INFILE "/Users/danielbi/git-repo/SP23-CS5200/data-parsing/brewers.csv" INTO TABLE Brewer
FIELDS TERMINATED BY ","
LINES TERMINATED BY "\n"
IGNORE 1 LINES;

LOAD DATA LOCAL INFILE "/Users/danielbi/git-repo/SP23-CS5200/data-parsing/beers.csv" INTO TABLE Beer
FIELDS TERMINATED BY "," OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY "\n"
IGNORE 1 LINES
(BeerId, BeerName, ABV, BrewerId, Style);

LOAD DATA LOCAL INFILE "/Users/danielbi/git-repo/SP23-CS5200/data-parsing/beerreviews.csv" INTO TABLE BeerReview
FIELDS TERMINATED BY "," OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(Appearance, Aroma, Palate, Taste, Overall, Created, Text, Username, BeerId);
