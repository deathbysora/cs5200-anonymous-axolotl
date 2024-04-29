import sys
import random 
from datetime import datetime, timedelta
# Appearance,Aroma,Palate,Taste,Overall,Created,Text,Username,BeerId
# Content Created ReviewId Username

PLACEHOLDER = "PLACEHOLDER"
DELIMITER = ","
MAX_COMMENTS = 4
MAX_DATE_OFFSET = 20

CREATED_INDEX = 5

contentTemplates = [
    "YES! Finally someone who understands.",
    "disagree.",
    "period.",
    "terrible rating",
    "dude did you even taste the beer",
]

def _createComment(reviewId: int, review: list[str], username: str) -> str:
    templateIndex = random.randint(0, len(contentTemplates) - 1)
    content = contentTemplates[templateIndex]

    reviewDate = datetime.strptime(review[CREATED_INDEX], "%Y-%m-%d %H:%M:%S")
    commentDateOffset = random.randint(0, MAX_DATE_OFFSET)
    commentDate = reviewDate + timedelta(days=commentDateOffset)

    return ",".join([f'"{content}"', commentDate.strftime("%Y-%m-%d %H:%M:%S"), str(reviewId), username])


def main(filePath: str, userPath: str, outDirectory: str) -> None:
    print("Reading review file...")
    with open(filePath, "r") as file:
        lines = file.readlines()

    print("Reading user file...")
    with open(userPath, "r") as file:
        users = file.readlines()

    comments = ["Content,Created,ReviewId,Username\n"] 

    print("Generating comments")
    for i in range(1, 1000000):
        if i % 100000 == 0:
            print(f"Commented on {i} reviews...")
        review = lines[i].split(DELIMITER)
        commentCount = random.randint(1, MAX_COMMENTS)
        for x in range(commentCount):
            user = users[random.randint(1, len(users) - 1)]
            comments.append(_createComment(i, review, user))

    print("Done creating comments")
    print("Writing file...", end="")
    if outDirectory[-1] != "/":
        outDirectory += "/"
    with open(f"{outDirectory}beercomments.csv", "w") as file:
        file.write("".join(comments))
    print("Done")

if __name__ == "__main__":
    if len(sys.argv) != 4:
        print("Please provide the data source path and output directory.")
        exit()
    main(sys.argv[1], sys.argv[2], sys.argv[3])
