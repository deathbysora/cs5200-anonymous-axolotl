-- PM2 Table Creation
CREATE SCHEMA IF NOT EXISTS BeersApp;
USE BeersApp;

DROP TABLE IF EXISTS Food;
DROP TABLE IF EXISTS BeerComments;
DROP TABLE IF EXISTS BeerReviews;
DROP TABLE IF EXISTS ViewHistory;
DROP TABLE IF EXISTS Beers;
DROP TABLE IF EXISTS BeerStyles;
DROP TABLE IF EXISTS Brewers;
DROP TABLE IF EXISTS Administrators;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Persons;

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
  BeerName VARCHAR(255) NOT NULL,
  ABV FLOAT NOT NULL,
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
  ReviewId INT AUTO_INCREMENT,
  Appearance FLOAT NOT NULL,
  Aroma FLOAT NOT NULL,
  Palate FLOAT NOT NULL,
  Taste FLOAT NOT NULL,
  Overall FLOAT NOT NULL,
  Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  Text VARCHAR(1024),
  UserName VARCHAR(255),
  BeerID INT,
  CONSTRAINT ck_BeerReviews_Appearance
    CHECK (Appearance >= 0 and Appearance <= 10),
  CONSTRAINT ck_BeerReviews_Aroma
    CHECK (Aroma >= 0 and Aroma <= 10),
  CONSTRAINT ck_BeerReviews_Palate
    CHECK (Palate >= 0 and Palate <= 10),
  CONSTRAINT ck_BeerReviews_Taste
    CHECK (Taste >= 0 and Taste <= 10),
  CONSTRAINT ck_BeerReviews_Overall
    CHECK (Overall >= 0 and Overall <= 10),
  CONSTRAINT pk_BeerReviews_ReviewId
    PRIMARY KEY (ReviewId),
  CONSTRAINT fk_BeerReviews_BeerId
    FOREIGN KEY (BeerId)
    REFERENCES Beers(BeerId)
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

CREATE TABLE Food (
  FoodName VARCHAR(255),
  Style VARCHAR(255),
  CONSTRAINT pk_Food_FoodName_Style
    PRIMARY KEY (FoodName, Style)
);



