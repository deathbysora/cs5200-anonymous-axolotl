CREATE DATABASE IF NOT EXISTS BeerApplication;
USE BeerApplication;

DROP TABLE IF EXISTS BeerComment;
DROP TABLE IF EXISTS BeerReview;
DROP TABLE IF EXISTS ViewHistory;
DROP TABLE IF EXISTS Beer;
DROP TABLE IF EXISTS Brewer;
DROP TABLE IF EXISTS Food;
DROP TABLE IF EXISTS BeerStyle;
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Administrator;
DROP TABLE IF EXISTS Person;

CREATE TABLE Person(
    Username VARCHAR(255),
    CONSTRAINT pk_Person_Username
        PRIMARY KEY (Username)
);

CREATE TABLE Administrator(
    Username VARCHAR(255),
    CONSTRAINT pk_Administrator_Username
        PRIMARY KEY (Username),
    CONSTRAINT fk_Administrator_Username
        FOREIGN KEY (Username)
        REFERENCES Person(Username)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE User(
    Username VARCHAR(255),
    CONSTRAINT pk_User_Username
        PRIMARY KEY (Username),
    CONSTRAINT fk_User_Username
        FOREIGN KEY (Username)
        REFERENCES Person(Username)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE BeerStyle(
    Style VARCHAR(255),
    CONSTRAINT pk_BeerStyle_Style
        PRIMARY KEY (Style)
);

CREATE TABLE Food(
    FoodName VARCHAR(255),
    Style VARCHAR(255),
    CONSTRAINT pk_Food_FoodName
        PRIMARY KEY (FoodName),
    CONSTRAINT fk_Food_Style
        FOREIGN KEY (Style)
        REFERENCES BeerStyle(Style)
        ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE Brewer (
    BrewerId INT,
    CONSTRAINT pk_Brewer_BrewId
        PRIMARY KEY (BrewerId)
);

CREATE TABLE Beer (
    BeerId INT NOT NULL,
    BeerName VARCHAR(255) NOT NULL,
    ABV FLOAT NOT NULL,
    Style VARCHAR(255),
    BrewerId INT,
    CONSTRAINT pk_Beer_BeerId
        PRIMARY KEY (BeerId),
    CONSTRAINT fk_Beer_BrewerId
        FOREIGN KEY (BrewerId)
        REFERENCES Brewer(BrewerId)
        ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE ViewHistory (
    ViewId INT AUTO_INCREMENT,
    Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Username VARCHAR(255),
    BeerId INT,
    CONSTRAINT pk_ViewHistory_ViewId
        PRIMARY KEY (ViewId),
    CONSTRAINT fk_ViewHistory_Username
        FOREIGN KEY (Username)
        REFERENCES Person(Username)
        ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT fk_ViewHistory_BeerId
        FOREIGN KEY (BeerId)
        REFERENCES Beer(BeerId)
        ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE BeerReview (
    ReviewID INT AUTO_INCREMENT,
    BeerID INT,
    Appearance FLOAT NOT NULL,
    Aroma FLOAT NOT NULL,
    Palate FLOAT NOT NULL,
    Taste FLOAT NOT NULL,
    Overall FLOAT NOT NULL,
    Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Text VARCHAR(255),
    Username VARCHAR(255),
    CONSTRAINT pk_BeerReview_ReviewID
        PRIMARY KEY (ReviewID),
    CONSTRAINT fk_BeerReview_BeerID FOREIGN KEY (BeerID)
        REFERENCES Beer(BeerID)
        ON UPDATE cascade ON DELETE SET NULL,
    CONSTRAINT fk_BeerReview_Username FOREIGN KEY (Username)
        REFERENCES Person(Username)
        ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE BeerComment (
    CommentID INT AUTO_INCREMENT,
    Text LONGTEXT NOT NULL,
    Created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Username VARCHAR(255),
    ReviewID INT,
    CONSTRAINT pk_BeerComment_CommentID
        PRIMARY KEY (CommentID),
    CONSTRAINT fk_BeerComment_Username
    FOREIGN KEY (Username)
        REFERENCES Person(Username)
        ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT fk_BeerComment_ReviewID
    FOREIGN KEY (ReviewID)
        REFERENCES BeerReview(ReviewID)
        ON UPDATE CASCADE ON DELETE CASCADE
);
