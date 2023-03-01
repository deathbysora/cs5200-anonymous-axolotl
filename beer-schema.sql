-- PM2 Table Creation
CREATE SCHEMA IF NOT EXISTS BeersApp;
USE BeersApp;

DROP TABLE IF EXISTS ViewHistory;
DROP TABLE IF EXISTS Persons;
DROP TABLE IF EXISTS Administrators;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS BeerReviews;
DROP TABLE IF EXISTS BeerComments;
DROP TABLE IF EXISTS Brewers;
DROP TABLE IF EXISTS BeerStyles;
DROP TABLE IF EXISTS Beers;
DROP TABLE IF EXISTS Foods;

CREATE TABLE Persons (
  UserName VARCHAR(255),
  CONSTRAINT pk_Persons_UserName 
  PRIMARY KEY (UserName)
);

CREATE TABLE Administrators (
  UserName VARCHAR(255),
  CONSTRAINT pk_Administrators_UserName
    PRIMARY KEY (UserName),
  CONSTRAINT fk_Administrators_UserName
    FOREIGN KEY (UserName)
    REFERENCES Persons(UserName)
    ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Users (
  UserName VARCHAR(255),
  CONSTRAINT pk_Users_UserName
    PRIMARY KEY (UserName),
  CONSTRAINT fk_Users_UserName
    FOREIGN KEY (UserName)
    REFERENCES Persons(UserName)
    ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Brewers (
  BrewerId INT,
  CONSTRAINT pk_Brewers_BrewId
    PRIMARY KEY (BrewerId)
);

CREATE TABLE BeerStyles (
  Style VARCHAR(255),
  CONSTRAINT pk_BeerStyles_Style
    PRIMARY KEY (Style)
);

CREATE TABLE Beers (
  BeerId INT,
  BeerName VARCHAR(255),
  ABV FLOAT,
  BrewerId INT,
  Style VARCHAR(255),
  CONSTRAINT pk_Beers_BeerId
    PRIMARY KEY (BeerId),
  CONSTRAINT fk_Beers_BrewerId
    FOREIGN KEY (BrewerId)
    REFERENCES Brewers(BrewerId)
    ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT fk_Beers_Style
    FOREIGN KEY (Style)
    REFERENCES BeerStyles(Style)
    ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE ViewHistory (
  ViewId INT AUTO_INCREMENT,
  Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  BeerId INT,
  UserName VARCHAR(255),
  CONSTRAINT pk_ViewHistory_ViewId
    PRIMARY KEY (ViewId),
  CONSTRAINT fk_ViewHistory_UserName
    FOREIGN KEY (UserName)
    REFERENCES Persons(UserName)
    ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT fk_ViewHistory_BeerId
    FOREIGN KEY (BeerId)
    REFERENCES Beers(BeerId)
    ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE BeerReviews (
	ReviewID INT AUTO_INCREMENT,
	BeerID INT,
	Appearance FLOAT,
	Aroma FLOAT,
	Palate FLOAT,
	Taste FLOAT,
	Overall FLOAT,
	Time TIMESTAMP,
	Text VARCHAR(1024),
	UserName VARCHAR(255),
	CONSTRAINT pk_BeerReviews_reviewID 
    PRIMARY KEY (ReviewID),
	CONSTRAINT fk_BeerReviews_beerID 
    FOREIGN KEY (BeerID)
		REFERENCES Beers(BeerID)
		ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE BeerComments (
	CommentID INT AUTO_INCREMENT,
	Content VARCHAR(1024),
	Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	ReviewID INT,
	UserName VARCHAR(255),
	CONSTRAINT pk_BeerComments_CommentID 
    PRIMARY KEY (CommentID),
    CONSTRAINT fk_BeerComments_UserName 
    FOREIGN KEY (UserName)
		REFERENCES Persons(UserName)
        ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT fk_BeerComments_ReviewID 
    FOREIGN KEY (ReviewID)
		REFERENCES BeerReviews(ReviewID)
		ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE Foods (
	FoodName VARCHAR(255),
    Style VARCHAR(255),
	CONSTRAINT pk_Foods_FoodName_Style 
    PRIMARY KEY (FoodName, Style)
);



