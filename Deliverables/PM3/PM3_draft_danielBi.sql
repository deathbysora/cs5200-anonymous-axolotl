USE BeerApplication;

#1 What are the top 10 most reviewed beers from the reviews
SELECT Style, COUNT(*) AS Review_Counts
FROM Beer
GROUP BY Style
ORDER BY Review_Counts DESC
LIMIT 10;

#2 What are the top 10 most viewed beers by other users (will be populated as user using it)
SELECT UserName, COUNT(*) AS View_Counts
# need to populate this table
FROM ViewHistory
GROUP BY UserName
ORDER BY View_Counts DESC
LIMIT 10;

#3 What are the top 10 profile that reviewed the most amount of beers (like a leader board)
SELECT Username, COUNT(*) AS UserName_Counts
FROM BeerReview
GROUP BY UserName
ORDER BY UserName_Counts DESC
LIMIT 10;

#4 What are the most 10 popular reviews that has the most comment
SELECT ReviewId, COUNT(*) AS Review_Engagement
FROM BeerComment
GROUP BY ReviewId
ORDER BY Review_Engagement DESC
LIMIT 10;


#5 For a certain type of beerstyle, what are some recommended food that goes with it
SELECT FoodName 
from Food
WHERE Style = 'Ale'; # we can do an enum callout?


#6 Top 10 most active users in 2023 (cross review comments and user engagements/view history)
# Elsa working on it rn


#7 What are the top 5 most reviewed beers in the summer season (May-Sept)
# not working
# better to show the beer name
SELECT BeerID, Created as Review_time, COUNT(*) AS Review_Counts
FROM BeerReview
GROUP BY BeerID, Review_time
HAVING MONTH(Review_time) <= 9 and MONTH(Review_time) >= 5
ORDER BY Review_Counts DESC
LIMIT 10;


#8 What are the top 5 most VIEWED beers in the winter season (Oct-Dec)
SELECT UserName, COUNT(*) AS View_Counts
FROM ViewHistory
GROUP BY UserName
HAVING (MONTH(time) <= 12 and MONTH(time) >= 10)
ORDER BY View_Counts DESC
LIMIT 10;

#9 Which user have not created a review not viewed any beers
SELECT Users.UserName
FROM Users
  LEFT OUTER JOIN
    BeerReview
  ON BeerReview.UserName = Users.UserName
  LEFT OUTER JOIN
    ViewHistory
  ON ViewHistory.UserName = Users.UserName
  WHERE BeerReview.ReviewId is NULL and ViewHistory.ViewId is NULL;
  
#10 On average, how many food pairings options does one beer have?
SELECT AVG(T.PAIRING_COUNT) as AVERAGE_PAIRING
FROM(
  SELECT Food.Style, COUNT(*) AS PAIRING_COUNT
  FROM Food
  GROUP BY Food.Style
) AS T
