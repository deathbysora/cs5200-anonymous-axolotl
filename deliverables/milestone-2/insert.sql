USE BeerApp;

LOAD DATA LOCAL INFILE "~/onedrive/me/northeastern/cs5200/anonymous-axolotl/data-parsing/users.csv" INTO TABLE Persons
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES;

LOAD DATA LOCAL INFILE "~/onedrive/me/northeastern/cs5200/anonymous-axolotl/data-parsing/users.csv" INTO TABLE Users
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES;

LOAD DATA LOCAL INFILE "~/onedrive/me/northeastern/cs5200/anonymous-axolotl/data-parsing/beerstyles.csv" INTO TABLE BeerStyles
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY "\r\n"
IGNORE 1 LINES;

LOAD DATA LOCAL INFILE "~/onedrive/me/northeastern/cs5200/anonymous-axolotl/data-parsing/brewers.csv" INTO TABLE Brewers
FIELDS TERMINATED BY ','
LINES TERMINATED BY "\r\n"
IGNORE 1 LINES;

LOAD DATA LOCAL INFILE "~/onedrive/me/northeastern/cs5200/anonymous-axolotl/data-parsing/beers.csv" INTO TABLE Beers
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY "\r\n"
IGNORE 1 LINES
(BeerId, BeerName, ABV, BrewerId, Style);

LOAD DATA LOCAL INFILE "~/onedrive/me/northeastern/cs5200/anonymous-axolotl/data-parsing/beerreviews.csv" INTO TABLE BeerReviews
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES
(Appearance, Aroma, Palate, Taste, Overall, Created, Text, Username, BeerId);

LOAD DATA LOCAL INFILE "~/onedrive/me/northeastern/cs5200/anonymous-axolotl/data-parsing/beercomments.csv" INTO TABLE BeerComments
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES
(Content, Created, ReviewId, Username);

LOAD DATA LOCAL INFILE "~/onedrive/me/northeastern/cs5200/anonymous-axolotl/data-parsing/food.csv" INTO TABLE Food
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES
(Style, FoodName);

LOAD DATA LOCAL INFILE "~/onedrive/me/northeastern/cs5200/anonymous-axolotl/data-parsing/ViewHistory.csv" INTO TABLE ViewHistory
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES
(ViewId, Created, Username, BeerId);
